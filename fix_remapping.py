#!/usr/bin/env python3
"""
Fix remaining intermediary names and corrupted imports in decompiled Fabric mod source.

Remaps:
1. Corrupted imports (e.g. import net.minecraft.KeyBinding -> import net.minecraft.client.option.KeyBinding)
2. Inner class references (e.g. CustomPayload.class_9154 -> CustomPayload.Id)
3. Remaining method_XXXX references
4. Remaining field_XXXX references
5. Does NOT touch variable names that happen to contain class_ + digits
"""

import os
import re
import glob

PROJECT_ROOT = os.path.dirname(os.path.abspath(__file__))
SRC_ROOT = os.path.join(PROJECT_ROOT, "src", "main", "java")
MAPPINGS_FILE = os.path.expanduser(
    "~/.gradle/caches/fabric-loom/1.21.11/"
    "net.fabricmc.yarn.1_21_11.1.21.11+build.6-v2/mappings.tiny"
)


def parse_mappings(path):
    """Parse tiny v2 mappings file."""
    # intermediary_full -> yarn_full (e.g. net/minecraft/class_304 -> net/minecraft/client/option/KeyBinding)
    class_full_map = {}

    # intermediary_short -> yarn_short (only top-level classes)
    class_short_map = {}

    # For inner classes: intermediary_inner_short (the part after $) -> yarn_inner_short
    inner_class_map = {}

    # intermediary_method -> yarn_method
    method_map = {}

    # intermediary_field -> yarn_field
    field_map = {}

    # Also build: yarn_short -> yarn_full (for fixing corrupted imports)
    # If multiple classes have the same short name, we need to be careful.
    yarn_short_to_full = {}

    with open(path) as f:
        for line in f:
            line = line.rstrip("\n")
            if not line or line.startswith("tiny"):
                continue

            parts = line.split("\t")
            if len(parts) < 2:
                continue

            first = parts[0]

            if first == "c" and len(parts) >= 4:
                # Class record: c<TAB>official<TAB>intermediary<TAB>named
                intermediary_full = parts[2]
                yarn_full = parts[3]

                class_full_map[intermediary_full] = yarn_full

                # Get simple short name (last segment after /)
                intermediary_simple = (
                    intermediary_full.rsplit("/", 1)[-1]
                    if "/" in intermediary_full
                    else intermediary_full
                )
                yarn_simple = (
                    yarn_full.rsplit("/", 1)[-1] if "/" in yarn_full else yarn_full
                )

                if "$" not in intermediary_simple:
                    # Top-level class
                    class_short_map[intermediary_simple] = yarn_simple
                    # Build reverse map: yarn_short -> yarn_full
                    if yarn_simple not in yarn_short_to_full:
                        yarn_short_to_full[yarn_simple] = yarn_full
                    else:
                        # Ambiguous short name - mark as ambiguous
                        yarn_short_to_full[yarn_simple] = None
                else:
                    # Inner class - the part after $ is the inner class short name
                    inner_part = intermediary_simple.split("$")[-1]
                    yarn_inner_part = yarn_simple.split("$")[-1]
                    inner_class_map[inner_part] = yarn_inner_part

            elif first == "" and len(parts) >= 3:
                second = parts[1]

                if second == "m" and len(parts) >= 6:
                    intermediary_method = parts[4]
                    yarn_method = parts[5]
                    if intermediary_method != yarn_method:
                        method_map[intermediary_method] = yarn_method

                elif second == "f" and len(parts) >= 6:
                    intermediary_field = parts[4]
                    yarn_field = parts[5]
                    if intermediary_field != yarn_field:
                        field_map[intermediary_field] = yarn_field

    return class_full_map, class_short_map, inner_class_map, method_map, field_map, yarn_short_to_full


def fix_imports(content, class_full_map, yarn_short_to_full):
    """
    Fix corrupted imports like:
      import net.minecraft.KeyBinding;
    to:
      import net.minecraft.client.option.KeyBinding;

    Strategy: For each import net.minecraft.X; check if X is in our mapping.
    If so, replace the import with the correct full path.
    """
    lines = content.split("\n")
    new_lines = []
    changed = False

    for line in lines:
        # Match import statements for net.minecraft classes (no subpackage)
        m = re.match(
            r"^(import\s+net\.minecraft\.)([A-Za-z_][A-Za-z0-9_]*);\s*$", line
        )
        if m:
            prefix = m.group(1)  # "import net.minecraft."
            simple_name = m.group(2)  # e.g. "KeyBinding"

            # Check if this simple name has a known unique full path
            if simple_name in yarn_short_to_full and yarn_short_to_full[simple_name] is not None:
                full_path = yarn_short_to_full[simple_name]  # e.g. "net/minecraft/client/option/KeyBinding"
                # Convert / to . and build the import
                full_import = full_path.replace("/", ".")
                new_line = f"import {full_import};"
                if new_line != line:
                    changed = True
                    print(f"  FIX IMPORT: {line.strip()} -> {new_line}")
                    new_lines.append(new_line)
                    continue
            elif simple_name not in yarn_short_to_full:
                # This is probably an already-correct import or a non-mapped class
                # Leave it as is, but check if it could be fixed via class_full_map
                pass

        # Also handle import statements that have wrong subpackage but correct class name
        # e.g. import net.minecraft.X; where X should be in a subpackage
        # This is already handled above

        # Handle static imports with broken paths
        m_static = re.match(
            r"^(import\s+static\s+net\.minecraft\.)([A-Za-z_][A-Za-z0-9_]*\.[A-Za-z_][A-Za-z0-9_]*);\s*$",
            line,
        )
        if m_static:
            prefix = m_static.group(1)
            rest = m_static.group(2)
            parts = rest.split(".")
            if len(parts) == 2:
                simple_name = parts[0]
                member = parts[1]
                if simple_name in yarn_short_to_full and yarn_short_to_full[simple_name] is not None:
                    full_path = yarn_short_to_full[simple_name]
                    full_import = f"import static {full_path.replace('/', '.')}.{member};"
                    if full_import != line:
                        changed = True
                        print(f"  FIX STATIC IMPORT: {line.strip()} -> {full_import}")
                        new_lines.append(full_import)
                        continue

        new_lines.append(line)

    if changed:
        return "\n".join(new_lines), True
    return content, False


def fix_inner_class_refs(content, inner_class_map):
    """
    Fix inner class references like:
      CustomPayload.class_9154 -> CustomPayload.Id
      InputUtil.class_306 -> InputUtil.Key
      HitResult.class_240 -> HitResult.Type
    """
    # Pattern: ClassName.class_XXXX (where class_XXXX is an intermediary inner class name)
    def replace_inner_class(match):
        before = match.group(1)  # whatever comes before .class_XXXX
        inner_name = match.group(2)  # class_XXXX
        if inner_name in inner_class_map:
            yarn_name = inner_class_map[inner_name]
            return f"{before}.{yarn_name}"
        return match.group(0)  # no change

    # Match patterns like: SomeIdentifier.class_XXXX
    # But NOT inside import statements (those should be fixed by fix_imports)
    pattern = r"([A-Za-z_][A-Za-z0-9_.]*)\.(class_\d+)"

    new_content, count = re.subn(pattern, replace_inner_class, content)
    if count > 0:
        print(f"  FIX INNER CLASS: {count} replacements")
    return new_content, count > 0


def fix_methods(content, method_map):
    """
    Fix remaining method_XXXX references.
    Only replace method names that appear as method calls (followed by '(') or
    in contexts where they're used as identifiers.
    """
    # Build a pattern that matches any method_XXXX that's in the map
    if not method_map:
        return content, False

    # Sort by length (longest first) to avoid partial matches
    sorted_methods = sorted(method_map.keys(), key=len, reverse=True)

    def replace_method(match):
        full = match.group(0)
        if full in method_map:
            return method_map[full]
        return full

    # Match method_ followed by digits as word boundaries
    pattern = r"(method_\d+)"

    count = 0
    def do_replace(m):
        nonlocal count
        name = m.group(1)
        if name in method_map:
            count += 1
            return method_map[name]
        return name

    new_content = re.sub(pattern, do_replace, content)
    if count > 0:
        print(f"  FIX METHOD: {count} replacements")
    return new_content, count > 0


def fix_fields(content, field_map):
    """
    Fix remaining field_XXXX references.
    """
    if not field_map:
        return content, False

    count = 0
    def do_replace(m):
        nonlocal count
        name = m.group(1)
        if name in field_map:
            count += 1
            return field_map[name]
        return name

    pattern = r"(field_\d+)"
    new_content = re.sub(pattern, do_replace, content)
    if count > 0:
        print(f"  FIX FIELD: {count} replacements")
    return new_content, count > 0


def fix_variable_names(content):
    """
    Handle variable names that accidentally look like class_XXXX identifiers.
    Variables like `class_17992` and `class_12972` are local variable names
    that should be renamed to something meaningful.

    These appear in contexts like:
      ItemStack class_17992 = ...
      Entity class_12972 = ...
    """
    # Pattern: type followed by class_XXXX as variable name
    # We'll rename them to descriptive names based on the type
    replacements = {
        "class_17992": "handStack",  # ItemStack class_17992 = handStack = ...
        "class_12972": "nearestTarget",  # Entity class_12972 = AutoElytraSwap.findNearestTarget(...)
    }

    count = 0
    for old_name, new_name in replacements.items():
        if old_name in content:
            # Only replace as a variable name (not preceded by a dot)
            pattern = r"(?<!\.)(\b)" + re.escape(old_name) + r"(\b)"
            new_content, c = re.subn(pattern, f"{new_name}", content)
            if c > 0:
                count += c
                content = new_content
                print(f"  FIX VARIABLE: {old_name} -> {new_name} ({c} occurrences)")

    return content, count > 0


def fix_type_casts(content, inner_class_map):
    """
    Fix type casts like:
      (InputUtil.class_306) something
    These should become:
      (InputUtil.Key) something
    """
    # This is handled by fix_inner_class_refs since it matches
    # ClassName.class_XXXX patterns including in cast context
    return content, False


def process_file(filepath, class_full_map, inner_class_map, method_map, field_map, yarn_short_to_full):
    """Process a single Java file."""
    with open(filepath, "r") as f:
        content = f.read()

    original = content
    filename = os.path.relpath(filepath, PROJECT_ROOT)

    # 1. Fix corrupted imports
    content, changed = fix_imports(content, class_full_map, yarn_short_to_full)
    if changed:
        print(f"[{filename}]")

    # 2. Fix inner class references
    content2, changed2 = fix_inner_class_refs(content, inner_class_map)
    if changed2:
        if not changed:
            print(f"[{filename}]")
        content = content2

    # 3. Fix method references
    content2, changed3 = fix_methods(content, method_map)
    if changed3:
        if not changed and not changed2:
            print(f"[{filename}]")
        content = content2

    # 4. Fix field references
    content2, changed4 = fix_fields(content, field_map)
    if changed4:
        if not changed and not changed2 and not changed3:
            print(f"[{filename}]")
        content = content2

    # 5. Fix variable names
    content2, changed5 = fix_variable_names(content)
    if changed5:
        if not changed and not changed2 and not changed3 and not changed4:
            print(f"[{filename}]")
        content = content2

    # Write back if changed
    if content != original:
        with open(filepath, "w") as f:
            f.write(content)
        return True
    return False


def main():
    print("=" * 60)
    print("Fixing intermediary names and corrupted imports...")
    print("=" * 60)

    # 1. Parse mappings
    print("\n[1] Parsing mappings file...")
    class_full_map, class_short_map, inner_class_map, method_map, field_map, yarn_short_to_full = parse_mappings(
        MAPPINGS_FILE
    )
    print(f"  Classes: {len(class_full_map)}")
    print(f"  Inner classes: {len(inner_class_map)}")
    print(f"  Methods: {len(method_map)}")
    print(f"  Fields: {len(field_map)}")
    print(f"  Yarn short->full: {len(yarn_short_to_full)}")

    # 2. Find all Java files
    java_files = glob.glob(os.path.join(SRC_ROOT, "**", "*.java"), recursive=True)
    print(f"\n[2] Found {len(java_files)} Java files")

    # 3. Process each file
    print("\n[3] Processing files...")
    modified_count = 0
    for filepath in sorted(java_files):
        if process_file(filepath, class_full_map, inner_class_map, method_map, field_map, yarn_short_to_full):
            modified_count += 1

    print(f"\n[4] Modified {modified_count} files")

    # 4. Verify remaining issues
    print("\n[5] Checking remaining intermediary references...")
    remaining = {"class_": 0, "method_": 0, "field_": 0}

    for filepath in java_files:
        with open(filepath) as f:
            content = f.read()

        # Count remaining class_XXXX references (only in code, not variable names)
        for m in re.finditer(r"[^a-zA-Z0-9_]class_(\d+)", content):
            # Check if this is a variable name (starts a word boundary after space/;/, etc.)
            pos = m.start()
            if pos == 0 or content[pos - 1] in " \t\n\r;(,=":
                # This could be a variable declaration - but class_ + 5+ digits is likely a variable
                num_digits = len(m.group(1))
                if num_digits < 5:
                    remaining["class_"] += 1
            else:
                remaining["class_"] += 1

        for m in re.finditer(r"method_\d+", content):
            remaining["method_"] += 1

        for m in re.finditer(r"field_\d+", content):
            remaining["field_"] += 1

    for key, count in remaining.items():
        status = "✓ CLEAN" if count == 0 else f"⚠ {count} remaining"
        print(f"  {key}XXXX: {status}")

    print("\nDone!")


if __name__ == "__main__":
    main()
