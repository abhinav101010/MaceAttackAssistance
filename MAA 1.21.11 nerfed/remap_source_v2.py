#!/usr/bin/env python3
"""Remap decompiled source from intermediary to yarn.
Handles both import paths (full qualified) and short references."""
import os
import re

SRC_DIR = "src/main/java"
MAPPINGS_FILE = os.path.expanduser("~/.gradle/caches/fabric-loom/1.21.11/net.fabricmc.yarn.1_21_11.1.21.11+build.6-v2/mappings.tiny")


def parse_tiny(filepath):
    """Parse tiny v2 mappings. Returns full maps and short maps."""
    class_full = {}  # intermediary_full -> yarn_full
    class_short = {}  # intermediary_short -> yarn_full
    method_short = {}  # method_intermediary -> method_yarn
    field_short = {}  # field_intermediary -> field_yarn
    inner_class = {}  # class_inter -> {inner_inter -> inner_yarn}

    current_class_inter_full = None

    with open(filepath, 'r') as f:
        for line in f:
            line = line.rstrip('\n')
            if not line.strip():
                continue
            parts = line.split('\t')

            rec_type = None
            idx = 0
            for j, p in enumerate(parts):
                if p in ('c', 'm', 'f', 'p'):
                    rec_type = p
                    idx = j
                    break

            if rec_type is None:
                continue

            if rec_type == 'c' and len(parts) > idx + 3:
                inter_full = parts[idx + 2]
                yarn_full = parts[idx + 3]
                class_full[inter_full] = yarn_full
                inter_short = inter_full.rsplit('/', 1)[-1]
                yarn_short = yarn_full.rsplit('/', 1)[-1]
                class_short[inter_short] = yarn_full  # map short to FULL yarn path
                current_class_inter_full = inter_full

            elif rec_type == 'm' and len(parts) > idx + 4:
                m_inter = parts[idx + 3]
                m_yarn = parts[idx + 4]
                if m_inter != m_yarn:
                    method_short[m_inter] = m_yarn

            elif rec_type == 'f' and len(parts) > idx + 4:
                f_inter = parts[idx + 3]
                f_yarn = parts[idx + 4]
                if f_inter != f_yarn:
                    field_short[f_inter] = f_yarn

    return class_full, class_short, method_short, field_short


def build_import_map(class_full):
    """Build a map from intermediary full path to yarn full path for imports."""
    # e.g. "net/minecraft/class_304" -> "net/minecraft/client/option/KeyBinding"
    return class_full


def remap_imports(content, import_map):
    """Remap import statements from intermediary full paths to yarn full paths."""
    # Match: import net.minecraft.class_XXXX;
    # Also: import net.minecraft.class_XXXX$InnerClass;
    def replacer(m):
        pkg = m.group(1)
        cls = m.group(2)
        full_inter = pkg + '/' + cls
        if full_inter in import_map:
            return 'import ' + import_map[full_inter].replace('/', '.') + ';'
        # Try without inner class
        return m.group(0)

    # Handle simple imports: import net.minecraft.class_XXXX;
    content = re.sub(
        r'import\s+(net/minecraft/(?:[a-z_]+/)*)(class_\d+);',
        replacer,
        content
    )

    # Handle $ imports: import net.minecraft.class_XXXX$InnerClass;
    def replacer_inner(m):
        pkg = m.group(1)
        cls = m.group(2)
        inner = m.group(3)
        full_inter = pkg + '/' + cls
        if full_inter in import_map:
            yarn_path = import_map[full_inter]
            return 'import ' + yarn_path.replace('/', '.') + '.' + inner + ';'
        return m.group(0)

    content = re.sub(
        r'import\s+(net/minecraft/(?:[a-z_]+/)*)(class_\d+)\$(class_\d+);',
        replacer_inner,
        content
    )

    return content


# Pattern: class_XXXX (4+ digits), method_XXXXX (5+ digits), field_XXXXX (5+ digits)
PAT = re.compile(r'\b(class_\d{4,}|method_\d{5,}|field_\d{5,})\b')


def remap_short_names(content, class_short, method_short, field_short):
    """Replace short intermediary names with full yarn paths or short yarn names."""
    def replacer(m):
        name = m.group(0)
        if name.startswith('class_'):
            # Return the SHORT yarn name (last segment of full path)
            if name in class_short:
                return class_short[name].rsplit('/', 1)[-1]
            return name
        elif name.startswith('method_'):
            return method_short.get(name, name)
        elif name.startswith('field_'):
            return field_short.get(name, name)
        return name

    return PAT.sub(replacer, content)


def main():
    print("Parsing yarn mappings...")
    class_full, class_short, method_short, field_short = parse_tiny(MAPPINGS_FILE)
    import_map = build_import_map(class_full)
    print(f"  class_full: {len(class_full)}, class_short: {len(class_short)}")
    print(f"  method: {len(method_short)}, field: {len(field_short)}")

    # Debug: show some import mappings
    for inter, yarn in sorted(import_map.items()):
        inter_s = inter.rsplit('/', 1)[-1]
        if inter_s.startswith('class_304') or inter_s.startswith('class_8710'):
            print(f"  IMPORT: {inter} -> {yarn}")

    changed = 0
    total = 0
    for root, dirs, files in os.walk(SRC_DIR):
        for fname in files:
            if fname.endswith('.java'):
                filepath = os.path.join(root, fname)
                total += 1
                with open(filepath, 'r') as f:
                    content = f.read()

                # Step 1: Remap import statements (full path replacement)
                content = remap_imports(content, import_map)

                # Step 2: Remap short references in code
                content = remap_short_names(content, class_short, method_short, field_short)

                with open(filepath, 'w') as f:
                    f.write(content)
                changed += 1

    print(f"\nDone: {changed}/{total} files processed")


if __name__ == '__main__':
    main()
