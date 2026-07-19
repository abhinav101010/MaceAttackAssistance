#!/usr/bin/env python3
"""Fast remap: scan source for method_XXXXX / field_XXXXX / class_XXXXX patterns, replace via dict lookup."""
import os
import re
import sys

SRC_DIR = "src/main/java"
MAPPINGS_FILE = os.path.expanduser("~/.gradle/caches/fabric-loom/1.21.11/net.fabricmc.yarn.1_21_11.1.21.11+build.6-v2/mappings.tiny")

def parse_tiny(filepath):
    class_short = {}  # class_XXXX -> YarnName
    method_short = {}  # method_XXXX -> yarnName
    field_short = {}   # field_XXXX -> yarnFieldName

    current_class_inter = None

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
                inter_short = inter_full.rsplit('/', 1)[-1]
                yarn_short = yarn_full.rsplit('/', 1)[-1]
                if inter_short != yarn_short:
                    class_short[inter_short] = yarn_short
                current_class_inter = inter_full

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

    return class_short, method_short, field_short


# Combined pattern: match class_XXXXX, method_XXXXX, field_XXXXX, argXXXXX
# class_XXXXX = 6+ digits, method_XXXXX = 5+ digits, field_XXXXX = 5+ digits
PAT = re.compile(r'\b(class_\d{4,}|method_\d{5,}|field_\d{5,}|arg\d+)\b')


def remap_content(content, class_short, method_short, field_short):
    """Replace intermediary identifiers using a single regex pass."""
    def replacer(m):
        name = m.group(0)
        if name.startswith('class_'):
            return class_short.get(name, name)
        elif name.startswith('method_'):
            return method_short.get(name, name)
        elif name.startswith('field_'):
            return field_short.get(name, name)
        elif name.startswith('arg'):
            return name  # arg names aren't in mappings
        return name

    return PAT.sub(replacer, content)


def main():
    print("Parsing yarn mappings...")
    class_short, method_short, field_short = parse_tiny(MAPPINGS_FILE)
    print(f"  class: {len(class_short)}, method: {len(method_short)}, field: {len(field_short)}")

    # Collect all replacement patterns into one lookup
    all_replacements = {}
    all_replacements.update(class_short)
    all_replacements.update(method_short)
    all_replacements.update(field_short)
    print(f"  Total replacements: {len(all_replacements)}")

    changed = 0
    total = 0
    for root, dirs, files in os.walk(SRC_DIR):
        for fname in files:
            if fname.endswith('.java'):
                filepath = os.path.join(root, fname)
                total += 1
                with open(filepath, 'r') as f:
                    content = f.read()
                new_content = remap_content(content, class_short, method_short, field_short)
                if new_content != content:
                    with open(filepath, 'w') as f:
                        f.write(new_content)
                    changed += 1
                    print(f"  Remapped: {filepath}")

    print(f"\nDone: {changed}/{total} files remapped")


if __name__ == '__main__':
    main()
