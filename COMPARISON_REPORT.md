# MAA 1.21.11 OG vs Nerfed — Complete Comparison Report

## Project Versions
- **Project A (OG)**: v3.0 — 28 Java files + 14 Mixin files + 8 resource files
- **Project B (Nerfed)**: v4.0.5-beta3 — 68 Java files + 17 Mixin files + 10 resource files

---

## SECTION 1: NEW FILES IN PROJECT B (28 files)

### 1.1 New Client Classes (21 files)

| # | File | Classification | Summary |
|---|------|---------------|---------|
| 1 | `AimCondition.java` | NEW FEATURE | Fall-threshold state machine gating aim assist activation |
| 2 | `ApproachSupportLine.java` | NEW FEATURE | Visual fan/cone guide lines for optimal mace drop angles + nearest player finder |
| 3 | `AutoElytraSwap.java` | NEW FEATURE | Auto elytra-to-chestplate swap with landing simulation timing |
| 4 | `AutoZoomInOut.java` | CODE REFACTOR | Combat-triggered auto zoom-in/out extracted from tick handler |
| 5 | `EnderPearlManager.java` | CODE REFACTOR | Pearl cooldown tracker extracted from inline code |
| 6 | `FriendCheckBoxData.java` | NEW FEATURE | Friend system UI data record |
| 7 | `FriendCheckBoxManager.java` | NEW FEATURE | Friend list UI helper |
| 8 | `FriendData.java` | NEW FEATURE | Friend data model (name + UUID) |
| 9 | `FriendKeyHandler.java` | NEW FEATURE | Multi-action keybind for friend system |
| 10 | `FriendListScreen.java` | NEW FEATURE | Full friend management GUI with search, scroll, skins, checkboxes |
| 11 | `FriendManager.java` | NEW FEATURE | Persistent JSON-based friend storage with CRUD |
| 12 | `KeyInput.java` | CODE REFACTOR + NEW | Centralized keybind dispatcher for all systems |
| 13 | `PearlCatch.java` | NEW FEATURE | Ender pearl interception using wind charge trajectory simulation |
| 14 | `PredictedLandingPosition.java` | NEW FEATURE | Physics-based landing position prediction (rendering stubbed) |
| 15 | `PrevSlotManager.java` | CODE REFACTOR | Stacked slot return with type-based filtering |
| 16 | `RefillManager.java` | CODE REFACTOR | Refill queue management with retry logic |
| 17 | `ScheduleKey.java` | CODE REFACTOR | Enum for scheduled task identification |
| 18 | `SpearAttacks.java` | NEW FEATURE | Complete spear combat: slam, swap, lunge |
| 19 | `SpearSlamEffects.java` | NEW FEATURE | Particle + sound effects for spear slam |
| 20 | `WorldPlayerData.java` | NEW FEATURE | World player data record (name, uuid, skin, team, friend) |
| 21 | `WorldPlayerManager.java` | NEW FEATURE | World player registry for friend system |

### 1.2 New Network Classes (2 files)

| # | File | Classification | Summary |
|---|------|---------------|---------|
| 22 | `MaaNetWorkConstants.java` | NEW FEATURE | Network packet channel identifier |
| 23 | `MaaPayload.java` | NEW FEATURE | UUID-based S2C/C2S network payload |

### 1.3 New Mixin Classes (5 files)

| # | File | Classification | Summary |
|---|------|---------------|---------|
| 24 | `MinecraftClientInvoker.java` | NEW FEATURE | Invoker for doAttack() — direct attack invocation |
| 25 | `MixinClientPlayNetworkHandler.java` | NEW FEATURE | Anti-cheat handshake protocol intercept |
| 26 | `MixinDrawContext.java` | CODE REFACTOR | Custom crosshair texture replacement |
| 27 | `MixinPlayerListHud.java` | NEW FEATURE | Friend heart icon in Tab player list |
| 28 | `MixinWorldRenderer.java` | CODE REFACTOR | Beam/marker rendering hook |

---

## SECTION 2: FILES ONLY IN PROJECT A (2 files, must keep)

| # | File | Reason |
|---|------|--------|
| 1 | `mixin/ClientPlayerEntityAccessor.java` | OG accessor for lastPitchClient setter — must preserve |
| 2 | `mixin/MinecraftClientAccessor.java` | OG accessor for cameraEntity getter — must preserve |

---

## SECTION 3: MODIFIED SHARED FILES

### 3.1 `MaceAttackAssistanceFabric.java`

| Change | Classification | Details |
|--------|---------------|---------|
| Server networking handshake | NEW FEATURE | Registers S2C/C2S MaaPayload, sends UUID on join |

### 3.2 `MaceAttackAssistanceClient.java` (MAJOR CHANGES)

| Change | Classification | Details |
|--------|---------------|---------|
| Friend protection checks | NEW FEATURE | isFriend() checks in target filtering |
| Spear attack support | NEW FEATURE | SPEAR_SLAM_ACTIVE checks |
| Auto elytra swap | NEW FEATURE | AutoElytraSwap integration |
| Auto zoom in/out | NEW FEATURE | AutoZoomInOut.autoZoomInOut() call |
| Ender pearl manager | NEW FEATURE | EnderPearlManager.tick() |
| Prev slot manager | NEW FEATURE | PrevSlotManager integration |
| Refill manager | NEW FEATURE | RefillManager replaces nonEventRefills |
| Key input centralization | NEW FEATURE | KeyInput.keyInputInGame() |
| Friend manager init | NEW FEATURE | FriendManager.init() |
| Network client receiver | NEW FEATURE | MaaPayload receiver for JUMP_MODE |
| HUD render API | CODE REFACTOR | HudElementRegistry instead of HudRenderCallback |
| World render events | CODE REFACTOR | BEFORE_ENTITIES/AFTER_ENTITIES instead of START/END_MAIN |
| Method moves to Utils | CODE REFACTOR | isSimpleVisibleFromPlayer, normalizeAngle moved |
| Search range 3.5→5.0 | BEHAVIOR CHANGE | Config RANGE default changed |
| AttackEntityCallback removed | BEHAVIOR CHANGE | Attack logic moved to START_CLIENT_TICK |
| isAllowedItem tag-based | CODE REFACTOR | Uses ItemTags.SWORDS instead of string check |
| isAllowedTarget PlayerEntity check removed | ⚠️ NERF | PlayerEntity + ALLOWED_PLAYER check removed in Nerfed |
| verifyPlayerCondition wind charge removed | ⚠️ NERF | Wind charge in hand requirement removed |
| ex_previous_slot/ex_preStun_slot removed | ⚠️ NERF | Replaced by PrevSlotManager |
| findNearestMob horizontal 3.5→5.0 | BEHAVIOR CHANGE | Wider search area |

### 3.3 `JobManager.java` (MAJOR CHANGES)

| Change | Classification | Details |
|--------|---------------|---------|
| Server-synced jump flags | NEW FEATURE | JUMP_OPTION, JUMP_MODE, QUICK_JUMP |
| Pearl catch status | NEW FEATURE | PEARL_CATCH → JobManager.pearlCatch() |
| Double tap status | NEW FEATURE | DOUBLE_TAP → JobManager.doubleTap() |
| Elytra manual switch | NEW FEATURE | ELYTRA_MANUAL_SWITCH_MODE |
| Auto refill status | NEW FEATURE | AUTO_REFILL |
| automaticRewear | NEW FEATURE | Elytra re-wear after double-tap |
| elytraSwap | NEW FEATURE | Hotbar slot selection + equipment swap |
| PrevSlotManager integration | CODE REFACTOR | Replaces inline prev-slot tracking |
| instantAttackInterval removed | ⚠️ FEATURE REMOVAL | Instant attack logic moved elsewhere |
| doAttack via MinecraftClientInvoker | CODE REFACTOR | Direct invocation vs interactionManager |
| STUN_HIGH/STUN_LOW configurable | NEW FEATURE | Config-driven instead of hardcoded |

### 3.4 `MacroController.java`

| Change | Classification | Details |
|--------|---------------|---------|
| STUN→STUN_SLAM at moderate fall | ⚠️ NERF | Changes combat behavior at velocity > -0.447 |
| Axe check for high-speed combos | ⚠️ NERF | Requires holding axe for VH_SPEED/H_SPEED |
| DOUBLE_TAP alternative to HOT_SWAP | NEW FEATURE | When gliding + double tap config |
| BREACH_SWAP removed | ⚠️ FEATURE REMOVAL | Never sets StatusType.BREACH from macro |
| isBlocking() replaces ShieldItem check | CODE REFACTOR | Broader shield detection |

### 3.5 `StatusType.java`

| Change | Classification | Details |
|--------|---------------|---------|
| PEARL_CATCH added | NEW FEATURE | pearlCatch() |
| DOUBLE_TAP added | NEW FEATURE | doubleTap() |
| ELYTRA_MANUAL_SWITCH_MODE added | NEW FEATURE | elytraManualSwitchMode() |
| AUTO_REFILL added | NEW FEATURE | autoRefill() |
| INSTANT_ATTACK_INTERVAL removed | ⚠️ FEATURE REMOVAL | Logic moved to START_CLIENT_TICK |

### 3.6 `Utils.java` (MAJOR ADDITIONS)

| Change | Classification | Details |
|--------|---------------|---------|
| 30+ new utility methods | NEW FEATURE | canDoubleTap, findCrosshairClosestTarget, simulateFuturePos, etc. |
| Method moves from Client | CODE REFACTOR | isSimpleVisibleFromPlayer, normalizeAngle |
| Spear weapon checks | NEW FEATURE | isSwordOrAxe, isSpear, rangeByWeapons |
| Hotbar/inventory search | NEW FEATURE | findItemInHotbar, findItemInInventory |
| Crosshair entity | NEW FEATURE | getCrosshairEntity |
| Landing simulation | NEW FEATURE | simulateFuturePos |
| Enchantment lookup | NEW FEATURE | getEnchantLevel |

### 3.7 `Config.java` (MAJOR ADDITIONS)

| Change | Classification | Details |
|--------|---------------|---------|
| 50+ new config fields | NEW FEATURE | For all new systems |
| 4 new enums | NEW FEATURE | Behavior, DisplayAt, CrosshairMode, JumpMode |
| 15 new keybindings | NEW FEATURE | For new features |
| Many default changes | BEHAVIOR CHANGE | See table below |
| VecData record removed | CODE REFACTOR | No longer needed |
| ATTACK_RANGE_DIFF removed | ⚠️ FEATURE REMOVAL | Config option removed |
| Config file path changed | BEHAVIOR CHANGE | v3 → v4.0.5 |

**Default Value Changes:**

| Field | OG | Nerfed | Classification |
|-------|-----|--------|---------------|
| RANGE | 3.5 | 5.0 | BEHAVIOR CHANGE |
| DISPLAY_ACTION_BAR | boolean | DisplayAt enum | UI CHANGE |
| WEAPON_SWING | false | true | BEHAVIOR CHANGE |
| BREACH_LIMITED | false | true | BEHAVIOR CHANGE |
| BREACH_ON_GROUND | false | true | BEHAVIOR CHANGE |
| SNAPBACK_THRESHOLD | 8 | 6 | BEHAVIOR CHANGE |
| SNAPBACK_TOLERANCE | 200 | 500 | BEHAVIOR CHANGE |
| ZOOM_STEP | 7 | 4 | BEHAVIOR CHANGE |
| PRIORITIZE_WIND_CHARGE | false | true | BEHAVIOR CHANGE |
| PRIORITIZE_ROCKET | false | true | BEHAVIOR CHANGE |
| AUTO_REFILL | false | true | BEHAVIOR CHANGE |
| EXTREME | false | true | BEHAVIOR CHANGE |
| PROP_FALL_VELOCITY[1] | 34 | 1 | ⚠️ NERF |
| PROP_ATTACK_RANGE | 300 | 281 | BEHAVIOR CHANGE |

### 3.8 `ConfigOperation.java`

| Change | Classification | Details |
|--------|---------------|---------|
| 60+ new config properties | NEW FEATURE | For all new systems |
| Default value changes | BEHAVIOR CHANGE | Mirrors Config.java changes |
| StunSlumming→StunSlam label fix | BUG FIX | Typo fix |

### 3.9 `ModConfigScreen.java`

| Change | Classification | Details |
|--------|---------------|---------|
| 13 categories with subcategories | UI CHANGE | Complete reorganization |
| Color-styled categories | UI CHANGE | Green/Aqua/Gray formatting |
| Global expand/collapse | UI CHANGE | setGlobalizedExpanded |
| Entries for all new configs | NEW FEATURE | New subcategories for each system |

### 3.10 `MixinMinecraft.java` (MAJOR CHANGES)

| Change | Classification | Details |
|--------|---------------|---------|
| Anti-cheat guard | NEW FEATURE | ZoomState.MAAClientState.antiCheat |
| Double-tap support | NEW FEATURE | canDT logic |
| Target resolution via getCrosshairEntity | CODE REFACTOR | Replaces mixin accessor |
| Shield detection via isBlocking() | CODE REFACTOR | Broader detection |
| Range check 7.0→20.0 | ⚠️ NERF | Significantly larger range |
| Position prediction to Utils | CODE REFACTOR | simulateFuturePos |
| Spear slam check | NEW FEATURE | SPEAR_SLAM_ACTIVE |
| getDistance naming | CODE REFACTOR | Variable rename |

### 3.11 `MixinClientPlayerInteractionManager.java`

| Change | Classification | Details |
|--------|---------------|---------|
| Anti-cheat + KeyManager guards | NEW FEATURE | ZoomState checks |
| AutoElytraSwap check | NEW FEATURE | Early return on elytra swap |
| Spear slam pass-through | NEW FEATURE | Allows attack during spear slam |
| Friend protection | NEW FEATURE | Skip attack on friends |
| Breach swap behavior enum | NEW FEATURE | SWORD_SWAP_OR_BREACH_SWAP |
| Hand swing animation removed | ⚠️ FEATURE REMOVAL | clientPlayer hand swing code removed |
| PrevSlotManager integration | CODE REFACTOR | Replaces static fields |
| RefillManager integration | CODE REFACTOR | Replaces TickScheduler refill |

### 3.12 `MixinKeyboardInput.java`

| Change | Classification | Details |
|--------|---------------|---------|
| Anti-cheat guard | NEW FEATURE | early return |
| Double-tap bypass | NEW FEATURE | Check for DOUBLE_TAP status |
| Wind charge auto-select | NEW FEATURE | findToSetWindCharge fallback |
| Jump mode system | NEW FEATURE | checkJumpMode() replaces simple check |
| Refill on wall jump | NEW FEATURE | RefillManager + afterJump |

### 3.13 `MixinHeldItemRenderer.java`

| Change | Classification | Details |
|--------|---------------|---------|
| Different method overload | BEHAVIOR CHANGE | Targets different render method |
| Cancels all perspectives | BEHAVIOR CHANGE | Not just first person |

### 3.14 `MixinLivingEntityRenderer.java`

| Change | Classification | Details |
|--------|---------------|---------|
| Different entity capture | CODE REFACTOR | Field vs IdentityHashMap |
| Different render API | CODE REFACTOR | BufferBuilders vs commandQueue |
| Constructor added | CODE REFACTOR | Required by newer MC API |

### 3.15 `MixinAbstractClientPlayerEntity.java`

| Change | Classification | Details |
|--------|---------------|---------|
| AUTO_WIND_CHARGE_SELECT gate | BEHAVIOR CHANGE | Additional requirement for FOV suppression |

### 3.16 `MixinMouse.java`

| Change | Classification | Details |
|--------|---------------|---------|
| setDelayTask replaces schedule | CODE REFACTOR | Method rename |

### 3.17 `BeamRenderHandler.java`

| Change | Classification | Details |
|--------|---------------|---------|
| External markerRenderer method | CODE REFACTOR | Called from MixinWorldRenderer |
| Parallel entity search | PERFORMANCE IMPROVEMENT | parallelStream() |
| Trail position optimization | PERFORMANCE IMPROVEMENT | MIN_STEP filter |
| Render layer change | BEHAVIOR CHANGE | debugQuads vs lightning |
| AnimalEntity check | CODE REFACTOR | Before PassiveEntity in getColor() |

### 3.18 `ElytraBoost.java`

| Change | Classification | Details |
|--------|---------------|---------|
| Separate ELYTRA_BOOST config | NEW FEATURE | Dedicated config flag |
| Auto wind charge selection | NEW FEATURE | AUTO_WIND_CHARGE_SELECT |
| Cooldown check optimization | PERFORMANCE IMPROVEMENT | isCooldown method |
| Tag-based armor detection | CODE REFACTOR | ItemTags instead of string check |
| JobManager guard | NEW FEATURE | checkOrderIsEmpty |

### 3.19 `HotSwap.java`

| Change | Classification | Details |
|--------|---------------|---------|
| Utils.findItemInHotbar delegation | CODE REFACTOR | Replaces manual loops |
| Simplified enchantment lookup | CODE REFACTOR | Utils.getEnchantLevel |
| isMace made public | CODE REFACTOR | Visibility change |

### 3.20 `RocketBlitz.java`

| Change | Classification | Details |
|--------|---------------|---------|
| Utils.findItemInHotbar delegation | CODE REFACTOR | Replaces manual loop |

### 3.21 `StunSlam.java`

| Change | Classification | Details |
|--------|---------------|---------|
| Returns boolean | CODE REFACTOR | Was void |
| Falling-only guard | NEW FEATURE | Velocity Y < 0 check |
| isBlocking() detection | CODE REFACTOR | Broader shield detection |
| flagPreAxe tracking | NEW FEATURE | Sets MaceAttackAssistanceClient.flagPreAxe |
| PrevSlotManager integration | CODE REFACTOR | Replaces static fields |
| Shield draining fallback | NEW FEATURE | Returns mace when draining |
| CHK_PLAYER_AGE, lastSlot | NEW FEATURE | Player age tracking |

### 3.22 `SwitchAssist.java`

| Change | Classification | Details |
|--------|---------------|---------|
| 5→16 switch keys | NEW FEATURE | All new feature toggles |
| DisplayAt enum | UI CHANGE | Replaces boolean |
| 3-way sword swap mode | NEW FEATURE | SWORD_SWAP_OR_BREACH_SWAP |
| Helper methods | CODE REFACTOR | valueToOnOff, valueToLowHigh |

### 3.23 `TickScheduler.java`

| Change | Classification | Details |
|--------|---------------|---------|
| Thread-safe staging | PERFORMANCE IMPROVEMENT | synchronized staging lists |
| Keyed condition tasks | NEW FEATURE | setConditionTaskWithKey, cancelPending |
| Query methods | NEW FEATURE | hasPending*, hasOther* |
| API rename | CODE REFACTOR | schedule→setDelayTask |

### 3.24 `ToggleElytra.java`

| Change | Classification | Details |
|--------|---------------|---------|
| Complete state-machine rewrite | CODE REFACTOR | Detects equipped chest item |
| Inventory search | NEW FEATURE | ALSO_SEARCH_INVENTORY |
| Manual mode | NEW FEATURE | ELYTRA_MANUAL_MODE |
| Returns slot ID | CODE REFACTOR | Was void |
| Tag-based detection | CODE REFACTOR | ItemTags.CHEST_ARMOR |

### 3.25 `ZoomState.java`

| Change | Classification | Details |
|--------|---------------|---------|
| MAAClientState inner class | NEW FEATURE | Server-side feature tracking |
| KeyManager inner class | NEW FEATURE | Jump mode key management |
| RenderTickCounter parameter | CODE REFACTOR | Delta from parameter |
| Custom crosshair rendering | NEW FEATURE | Configurable crosshair textures |
| AutoZoomInOut integration | NEW FEATURE | isCancel() check |
| FOV in/out tick 5→2 | PERFORMANCE IMPROVEMENT | Faster transition |

### 3.26 `AutoRefill.java`

| Change | Classification | Details |
|--------|---------------|---------|
| autoRefillInventory method | NEW FEATURE | Cross-slot inventory-to-hotbar refill |
| equipmentSwap method | NEW FEATURE | Chest slot swap |

### 3.27 `DebugScreen.java`

| Change | Classification | Details |
|--------|---------------|---------|
| ~30 new debug lines | UI CHANGE | Config state, job state, scheduler state |

### 3.28 Identical Files (no changes needed)

ColorData, Debug, FlappingSuppression, JumpController, MaceParticle, ModMenuIntegration, StatusEntry, WallClimbing, CameraInvoker, KeyBindingInvoker, ClientPlayerInteractionManagerInvoker, MixinGameRenderer, MixinPlayerEntityRenderer

---

## SECTION 4: RESOURCE DIFFERENCES

### 4.1 New Resources in B

| File | Classification | Summary |
|------|---------------|---------|
| `icon.png` | UI CHANGE | Mod icon (replaces logo_maa.png reference) |
| `textures/gui/sprites/hud/custom_crosshair.png` | NEW FEATURE | Custom crosshair texture |
| `textures/gui/sprites/hud/friend.png` | NEW FEATURE | Friend heart icon for Tab list |

### 4.2 Mixin JSON

| Change | Classification | Details |
|--------|---------------|---------|
| Common mixins empty | CODE REFACTOR | MixinMinecraft moved to client |
| +5 new client mixins | NEW FEATURE | MinecraftClientInvoker, MixinClientPlayNetworkHandler, MixinDrawContext, MixinPlayerListHud, MixinWorldRenderer |
| -2 removed mixins | CODE REFACTOR | ClientPlayerEntityAccessor, MinecraftClientAccessor (replaced by new approach) |
| refmap removed | CODE REFACTOR | No longer specified |

### 4.3 fabric.mod.json

| Change | Classification | Details |
|--------|---------------|---------|
| Version templated | CODE REFACTOR | ${version} |
| Name updated | UI CHANGE | 1.21 → 1.21.11 |
| Environment client→* | BEHAVIOR CHANGE | Now runs on both sides |
| Icon path changed | UI CHANGE | icon.png |
| Loader version relaxed | CODE REFACTOR | >=0.16.0 |

### 4.4 Language Files (en_us.json, ja_jp.json)

| Change | Classification | Details |
|--------|---------------|---------|
| 30+ new translation keys | NEW FEATURE | For all new features |
| Category key renamed | CODE REFACTOR | mace_attack_assistance → maceattackassistance.main |
| on_weapon value changed | UI CHANGE | "On Allowed Weapon" → "Falling Attack Particles" |
| snapback_tolerance range | UI CHANGE | 0-200 → 0-500 |

### 4.5 Build Files

| Change | Classification | Details |
|--------|---------------|---------|
| Loom version from properties | CODE REFACTOR | ${loom_version} |
| Loader: 0.19.3→0.16.14 | ⚠️ DOWNGRADE | Older fabric loader |
| Fabric API: 0.141.5→0.141.4 | ⚠️ DOWNGRADE | Slightly older |
| Cloth-config: compileOnly→impl | CODE REFACTOR | Now a runtime dependency |
| ModMenu: compileOnly→impl | CODE REFACTOR | Now a runtime dependency |
| Publishing block added | CODE REFACTOR | Empty publishing config |
| Archives name handling | CODE REFACTOR | Different approach |

---

## SECTION 5: CLASSIFICATION SUMMARY

| Classification | Count | Auto-Merge? |
|---------------|-------|-------------|
| NEW FEATURE | ~80+ | ✅ Yes |
| BUG FIX | 1 (typo) | ✅ Yes |
| PERFORMANCE IMPROVEMENT | ~8 | ✅ Yes |
| CODE REFACTOR | ~40+ | ✅ Yes (carefully) |
| UI CHANGE | ~15 | ✅ Yes (carefully) |
| BEHAVIOR CHANGE | ~20 | ⚠️ Review needed |
| NERF | ~12 | ❌ Requires approval |
| FEATURE REMOVAL | ~5 | ❌ Requires approval |
