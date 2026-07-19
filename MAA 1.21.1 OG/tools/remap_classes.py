import re, os

tiny_path = "tools/yarn/mappings/mappings.tiny"
src_root = "tmpsrc"

# Build class map: intermediary simple name -> yarn fully qualified
# tiny format: c<T>net/minecraft/class_XXX<T>net/minecraft/client/MinecraftClient
class_map = {}
with open(tiny_path, encoding="utf-8") as f:
    for line in f:
        parts = line.rstrip("\n").split("\t")
        if parts[0] == "c" and len(parts) >= 3:
            inter_fqn = parts[1]
            named_fqn = parts[2]
            # Extract simple name from intermediary (e.g. class_310)
            inter_simple = inter_fqn.rsplit("/", 1)[-1]
            named_simple = named_fqn.rsplit("/", 1)[-1]
            named_pkg = "/".join(named_fqn.split("/")[:-1])
            class_map[inter_simple] = (named_fqn, named_simple, named_pkg)

# Count how many class_X entries we have
class_entries = {k: v for k, v in class_map.items() if re.match(r'class_\d+', k)}
print(f"Total class mappings: {len(class_entries)}")

# Build import map: intermediary FQN -> named FQN
import_map = {}
for k, v in class_entries.items():
    import_map[f"net/minecraft/{k}"] = v[0]
    # Also handle other packages
    for prefix in ["net/fabricmc", "org/apache/logging/log4j", "com/mojang/brigadier"]:
        inter_fqn = f"{prefix}/{k}"
        if k in class_map:
            import_map[inter_fqn] = class_map[k][0]

# Now remap: in Java source, class_310 appears as simple name
# Need to also fix import statements and fully-qualified references
pattern = re.compile(r'\b(class_(\d+))\b')

def repl(m):
    key = "class_" + m.group(2)
    if key in class_map:
        return class_map[key][1]  # Return simple named name
    return key

# Also remap fully qualified references like net.minecraft.class_310
fqn_pattern = re.compile(r'net\.minecraft\.class_(\d+)')

def fqn_repl(m):
    key = "class_" + m.group(1)
    if key in class_map:
        return class_map[key][0].replace("/", ".")
    return m.group(0)

# Remap import statements
import_pattern = re.compile(r'import\s+net\.minecraft\.class_(\d+);')

def import_repl(m):
    key = "class_" + m.group(1)
    if key in class_map:
        return f"import {class_map[key][0].replace('/', '.')};"
    return m.group(0)

# Remap import net.fabricmc... patterns - these should already be named
# since fabric API classes are in the classpath

count = 0
for dirpath, _, files in os.walk(src_root):
    for fn in files:
        if not fn.endswith(".java"):
            continue
        path = os.path.join(dirpath, fn)
        with open(path, encoding="utf-8", errors="replace") as fh:
            text = fh.read()
        if not text:
            continue

        # First remap import statements
        new_text = import_pattern.sub(import_repl, text)
        # Then remap fully qualified references
        new_text = fqn_pattern.sub(fqn_repl, new_text)
        # Then remap unqualified class_X references
        new_text = pattern.sub(repl, new_text)
        
        # Fix Fabric API imports that use class_X (shouldn't happen but just in case)
        # net.fabricmc.api.XXX should already be correct in the decompiled source
        
        if new_text != text:
            with open(path, "w", encoding="utf-8") as fh:
                fh.write(new_text)
            count += 1

print(f"Remapped {count} files")
