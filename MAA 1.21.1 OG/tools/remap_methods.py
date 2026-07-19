import re, os

tiny_path = "tools/yarn/mappings/mappings.tiny"
src_root = "tmpsrc"

# Parse tiny v2 mappings
# Format:
# c<TAB>intermediary_class<TAB>named_class
# 	<TAB>m<TAB>descriptor<TAB>intermediary_method<TAB>named_method
# 	<TAB>f<TAB>field_type<TAB>intermediary_field<TAB>named_field

# Build maps:
# 1. class_intermediary_simple -> named_fqn
# 2. (class_intermediary_fqn, method_intermediary) -> method_named
# 3. (class_intermediary_fqn, field_intermediary) -> field_named

class_map = {}  # intermediary_simple -> (intermediary_fqn, named_simple, named_fqn)
method_map = {}  # (intermediary_class_fqn, intermediary_method) -> named_method
field_map = {}  # (intermediary_class_fqn, intermediary_field) -> named_field

current_class = None
with open(tiny_path, encoding="utf-8") as f:
    for line in f:
        stripped = line.rstrip("\n")
        if stripped.startswith("c\t"):
            parts = stripped.split("\t")
            if len(parts) >= 3:
                inter_fqn = parts[1]  # e.g. net/minecraft/class_310
                named_fqn = parts[2]  # e.g. net/minecraft/client/MinecraftClient
                inter_simple = inter_fqn.rsplit("/", 1)[-1]
                named_simple = named_fqn.rsplit("/", 1)[-1]
                class_map[inter_simple] = (inter_fqn, named_simple, named_fqn)
                current_class = inter_fqn
        elif stripped.startswith("\tm\t") and current_class:
            parts = stripped.split("\t")
            # \t m \t descriptor \t intermediary \t named
            if len(parts) >= 5:
                inter_method = parts[3]
                named_method = parts[4]
                method_map[(current_class, inter_method)] = named_method
        elif stripped.startswith("\tf\t") and current_class:
            parts = stripped.split("\t")
            # \t f \t type \t intermediary \t named
            if len(parts) >= 5:
                inter_field = parts[3]
                named_field = parts[4]
                field_map[(current_class, inter_field)] = named_field

print(f"Classes: {len(class_map)}, Methods: {len(method_map)}, Fields: {len(field_map)}")

# Build reverse lookup: intermediary_simple -> list of (class_fqn, named_simple)
# For ambiguous names, we'll use all possible mappings
simple_to_classes = {}
for inter_simple, (inter_fqn, named_simple, named_fqn) in class_map.items():
    if inter_simple not in simple_to_classes:
        simple_to_classes[inter_simple] = []
    simple_to_classes[inter_simple].append((inter_fqn, named_simple))

# For method/field remapping, we need to know what class each usage belongs to.
# Since the code already has class names (after class remapping), we can look up:
# YarnClassName.method_XXXXX -> YarnClassName.yarnMethod()
# But we need the intermediary class FQN to look up in method_map.
# Build: yarn_simple -> intermediary_fqn
yarn_to_intermediary = {}
for inter_simple, (inter_fqn, named_simple, named_fqn) in class_map.items():
    if named_simple not in yarn_to_intermediary:
        yarn_to_intermediary[named_simple] = inter_fqn

# Method remap pattern: look for KnownClass.method_XXXXX or KnownClass.field_XXXXX
method_pattern = re.compile(r'(?<!\w)([A-Z][A-Za-z0-9_]*(?:\.[A-Z][A-Za-z0-9_]*)*)\.((?:method|field)_(\d+))(?=[\s(;\[,.])')
# Also handle direct: ClassName.method_XXXXX() without space before (
method_pattern2 = re.compile(r'([A-Z][A-Za-z0-9_]*)\.(method_(\d+))\(')
field_pattern2 = re.compile(r'([A-Z][A-Za-z0-9_]*)\.(field_(\d+))(?!\w)')

# Also handle: variable.method_XXXXX where variable type is known
# e.g., clientPlayer.method_18798() where clientPlayer is ClientPlayerEntity
# We can't reliably determine this without type analysis, so we focus on:
# 1. Static class references: ClassName.method_XXXXX()
# 2. All method_XXXXX/field_XXXXX names (build global lookup)

# Build global method/field lookup: intermediary_name -> set of named_names
global_method_lookup = {}
for (cls_fqn, inter_method), named_method in method_map.items():
    if inter_method not in global_method_lookup:
        global_method_lookup[inter_method] = set()
    global_method_lookup[inter_method].add(named_method)

global_field_lookup = {}
for (cls_fqn, inter_field), named_field in field_map.items():
    if inter_field not in global_field_lookup:
        global_field_lookup[inter_field] = set()
    global_field_lookup[inter_field].add(named_field)

print(f"Unique intermediary methods: {len(global_method_lookup)}")
print(f"Unique intermediary fields: {len(global_field_lookup)}")

# Count how many have unique mappings
unique_methods = sum(1 for v in global_method_lookup.values() if len(v) == 1)
unique_fields = sum(1 for v in global_field_lookup.values() if len(v) == 1)
print(f"Unique-mapped methods: {unique_methods}, Unique-mapped fields: {unique_fields}")

# For uniquely mapped names, build direct lookup
direct_method_lookup = {}
for inter, named_set in global_method_lookup.items():
    if len(named_set) == 1:
        direct_method_lookup[inter] = next(iter(named_set))

direct_field_lookup = {}
for inter, named_set in global_field_lookup.items():
    if len(named_set) == 1:
        direct_field_lookup[inter] = next(iter(named_set))

# Apply remapping
# Method pattern: any.method_XXXXX( or any.method_XXXXX;
# Field pattern: any.field_XXXXX (not followed by alphanumeric)
all_names_pattern = re.compile(r'(method|field)_(\d+)')

def remap_name(m):
    prefix = m.group(1)
    num = m.group(2)
    key = f"{prefix}_{num}"
    if prefix == "method":
        return direct_method_lookup.get(key, key)
    else:
        return direct_field_lookup.get(key, key)

count = 0
for dirpath, _, files in os.walk(src_root):
    for fn in files:
        if not fn.endswith(".java"):
            continue
        path = os.path.join(dirpath, fn)
        with open(path, encoding="utf-8", errors="replace") as fh:
            text = fh.read()
        if not text or "method_" not in text and "field_" not in text:
            continue
        
        new_text = all_names_pattern.sub(remap_name, text)
        
        if new_text != text:
            with open(path, "w", encoding="utf-8") as fh:
                fh.write(new_text)
            count += 1

print(f"Remapped methods/fields in {count} files")
