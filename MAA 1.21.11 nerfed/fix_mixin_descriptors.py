#!/usr/bin/env python3
"""
Fix incorrect class paths in mixin annotation descriptor strings.

The problem: imports were remapped to yarn (e.g., net.minecraft.client.option.KeyBinding),
but annotation strings still have partial/wrong paths like Lnet/minecraft/KeyBinding; instead
of Lnet/minecraft/client/option/KeyBinding;.

This script:
1. Parses yarn mappings to build simple_name -> full_yarn_path mapping
2. For each Java file, also reads imports to build a reliable simple_name -> full_path map
3. Scans for Lnet/minecraft/ClassName; patterns (missing subpackages) and fixes them
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


def parse_yarn_mappings(path):
    """
    Parse tiny v2 mappings to get:
    - yarn_simple_name -> yarn_full_path (for fixing descriptors)
    - intermediary_full_path -> yarn_full_path (for fixing intermediary names)
    """
    yarn_simple_to_full = {}  # simple_name -> full_yarn_path (with / separators)
    intermediary_to_yarn = {}  # intermediary_full -> yarn_full

    with open(path) as f:
        for line in f:
            line = line.rstrip("\n")
            if not line or line.startswith("tiny"):
                continue

            parts = line.split("\t")
            if len(parts) < 4:
                continue

            first = parts[0]
            if first == "c":
                intermediary_full = parts[2]
                yarn_full = parts[3]

                intermediary_to_yarn[intermediary_full] = yarn_full

                # Extract simple name (last component, without inner class parts)
                yarn_simple = yarn_full.rsplit("/", 1)[-1] if "/" in yarn_full else yarn_full

                # For top-level classes, store simple_name -> full_path
                if "$" not in intermediary_full:
                    if yarn_simple not in yarn_simple_to_full:
                        yarn_simple_to_full[yarn_simple] = yarn_full
                    else:
                        # Multiple classes with same simple name - can't use simple name
                        yarn_simple_to_full[yarn_simple] = None  # ambiguous

    return yarn_simple_to_full, intermediary_to_yarn


def build_import_descriptor_map(filepath):
    """
    Build a mapping from simple class name to full descriptor path
    by reading import statements. This is the most reliable source.
    """
    import_map = {}
    with open(filepath) as f:
        for line in f:
            line = line.strip()
            # Match: import net.minecraft.something.ClassName;
            # or: import com.something.ClassName;
            m = re.match(r"^import\s+([\w.]+);\s*$", line)
            if m:
                dotted_path = m.group(1)
                # Convert to descriptor format (dots -> slashes)
                descriptor_path = dotted_path.replace(".", "/")
                simple_name = dotted_path.rsplit(".", 1)[-1]
                # Only store if not ambiguous
                if simple_name not in import_map:
                    import_map[simple_name] = descriptor_path
                else:
                    import_map[simple_name] = None  # ambiguous
    return import_map


def fix_file(filepath, yarn_simple_to_full, intermediary_to_yarn):
    """
    Fix descriptor strings in a single Java file.
    Returns (new_content, list_of_changes).
    """
    with open(filepath, "r") as f:
        content = f.read()

    # Build import map for this specific file (most reliable)
    import_map = build_import_descriptor_map(filepath)

    changes = []

    def replace_class_ref(match):
        """Replace a class reference in a descriptor with the correct yarn path."""
        full_match = match.group(0)  # e.g., Lnet/minecraft/KeyBinding;

        # Extract the path between L and ; (group 1)
        inner_path = match.group(1)  # e.g., net/minecraft/KeyBinding or net/minecraft/FrameGraphBuilder$Profiler

        # Handle inner classes (with $)
        if "$" in inner_path:
            # Split into parent and inner parts
            parent_path, inner_name = inner_path.rsplit("$", 1)
            parent_simple = parent_path.rsplit("/", 1)[-1] if "/" in parent_path else parent_path
            parent_prefix = parent_path.rsplit("/", 1)[0] if "/" in parent_path else ""

            if parent_prefix == "net/minecraft":
                # Parent is directly under net/minecraft - need to fix
                # Look up parent class
                correct_parent = None
                if parent_simple in import_map and import_map[parent_simple] is not None:
                    correct_parent = import_map[parent_simple]
                elif parent_simple in yarn_simple_to_full and yarn_simple_to_full[parent_simple] is not None:
                    correct_parent = yarn_simple_to_full[parent_simple]

                if correct_parent:
                    new_ref = f"L{correct_parent}${inner_name};"
                    if new_ref != full_match:
                        changes.append(f"  {full_match} -> {new_ref}")
                        return new_ref

                # Try intermediary
                if parent_simple.startswith("class_") and parent_simple[6:].isdigit():
                    interm_full = parent_path
                    if interm_full in intermediary_to_yarn:
                        yarn_full = intermediary_to_yarn[interm_full]
                        new_ref = f"L{yarn_full}${inner_name};"
                        changes.append(f"  INTERMEDIARY: {full_match} -> {new_ref}")
                        return new_ref

            return full_match

        # Simple name (no inner class)
        simple_name = inner_path.rsplit("/", 1)[-1] if "/" in inner_path else inner_path

        # Skip if it's already a full path (has subpackages beyond net/minecraft)
        prefix = inner_path.rsplit("/", 1)[0] if "/" in inner_path else ""
        if prefix != "net/minecraft":
            # This class is in a deeper package already - might be correct
            # Check if it's a known intermediary
            if simple_name.startswith("class_") and simple_name[6:].isdigit():
                # Intermediary name - try to fix
                interm_full = inner_path
                if interm_full in intermediary_to_yarn:
                    yarn_full = intermediary_to_yarn[interm_full]
                    new_ref = f"L{yarn_full};"
                    changes.append(f"  INTERMEDIARY: {full_match} -> {new_ref}")
                    return new_ref
            return full_match

        # The class is directly under net/minecraft (missing subpackage) - FIX IT

        # First, check import map (most reliable for this file)
        if simple_name in import_map and import_map[simple_name] is not None:
            correct_path = import_map[simple_name]
            new_ref = f"L{correct_path};"
            if new_ref != full_match:
                changes.append(f"  {full_match} -> {new_ref}")
                return new_ref

        # Then check yarn mappings
        if simple_name in yarn_simple_to_full and yarn_simple_to_full[simple_name] is not None:
            correct_path = yarn_simple_to_full[simple_name]
            new_ref = f"L{correct_path};"
            if new_ref != full_match:
                changes.append(f"  {full_match} -> {new_ref}")
                return new_ref

        # Intermediary class name (class_XXX)
        if simple_name.startswith("class_") and simple_name[6:].isdigit():
            interm_full = inner_path
            if interm_full in intermediary_to_yarn:
                yarn_full = intermediary_to_yarn[interm_full]
                new_ref = f"L{yarn_full};"
                changes.append(f"  INTERMEDIARY: {full_match} -> {new_ref}")
                return new_ref

        # Couldn't fix
        changes.append(f"  WARNING: Could not fix {full_match} (simple_name={simple_name})")
        return full_match

    # Pattern: L<package_path>/<ClassName>;
    # We want to match Lnet/minecraft/ClassName; where ClassName is a simple name
    # This catches both incorrect short paths and intermediary names
    # The key: the path between L and ; starts with net/minecraft/ but may be missing subpackages
    # Capture group 1 is the path between L and ; (e.g., net/minecraft/KeyBinding)
    pattern = r"L(net/minecraft/[A-Za-z_$][\w$]*(?:\$[A-Za-z_$][\w$]*)*);"

    new_content = re.sub(pattern, replace_class_ref, content)

    return new_content, changes


def main():
    print("=" * 70)
    print("Fixing mixin annotation descriptor strings...")
    print("=" * 70)

    # 1. Parse mappings
    print("\n[1] Parsing yarn mappings...")
    yarn_simple_to_full, intermediary_to_yarn = parse_yarn_mappings(MAPPINGS_FILE)
    print(f"  Yarn simple->full mappings: {len(yarn_simple_to_full)}")
    print(f"  Intermediary->Yarn mappings: {len(intermediary_to_yarn)}")

    # 2. Find all Java files
    java_files = glob.glob(os.path.join(SRC_ROOT, "**", "*.java"), recursive=True)
    print(f"\n[2] Found {len(java_files)} Java files")

    # 3. Process each file
    print("\n[3] Processing files...")
    modified_count = 0

    for filepath in sorted(java_files):
        filename = os.path.relpath(filepath, PROJECT_ROOT)

        new_content, changes = fix_file(filepath, yarn_simple_to_full, intermediary_to_yarn)

        if changes:
            print(f"\n[{filename}]")
            for change in changes:
                print(change)

        with open(filepath, "r") as f:
            original = f.read()

        if new_content != original:
            with open(filepath, "w") as f:
                f.write(new_content)
            modified_count += 1

    print(f"\n[4] Modified {modified_count} files")

    # 5. Verify remaining issues
    print("\n[5] Verifying no remaining issues...")
    mixin_dir = os.path.join(SRC_ROOT, "com", "papack", "maceattackassistance", "mixin")
    mixin_files = glob.glob(os.path.join(mixin_dir, "*.java"))

    issues = 0
    for filepath in sorted(mixin_files):
        filename = os.path.basename(filepath)
        with open(filepath) as f:
            content = f.read()

        # Check for Lnet/minecraft/ClassName; (missing subpackage)
        # This includes inner classes with $
        for match in re.finditer(r"Lnet/minecraft/([A-Za-z_$][\w$]*(?:\$[A-Za-z_$][\w$]*)*);", content):
            name = match.group(1)
            full_match = match.group(0)

            # Handle inner classes
            if "$" in name:
                parent_name = name.split("$")[0]
                # Check if parent is correctly mapped
                if parent_name in yarn_simple_to_full and yarn_simple_to_full[parent_name] is not None:
                    full_path = yarn_simple_to_full[parent_name]
                    if full_path.startswith("net/minecraft/"):
                        # Parent is directly in net/minecraft - might be correct
                        # But FrameGraphBuilder is NOT in net/minecraft directly
                        # So check if it's really at net/minecraft/ParentName
                        if full_path == f"net/minecraft/{parent_name}":
                            continue
                # Wrong - needs fix
                issues += 1
                print(f"  [{filename}] STILL HAS: {full_match}")
                continue

            # Simple class name
            # Check if it's already correct (the class IS directly in net/minecraft)
            if name in yarn_simple_to_full and yarn_simple_to_full[name] is not None:
                full_path = yarn_simple_to_full[name]
                if full_path == f"net/minecraft/{name}":
                    continue  # Correct - class really is in net/minecraft

            # It's wrong
            issues += 1
            print(f"  [{filename}] STILL HAS: {full_match}")

        # Check for intermediary names
        for match in re.finditer(r"Lnet/minecraft/(class_\d+);", content):
            issues += 1
            print(f"  [{filename}] INTERMEDIARY: {match.group(0)}")

    if issues == 0:
        print("  ✓ All descriptor strings are correct!")

    print("\nDone!")


if __name__ == "__main__":
    main()
