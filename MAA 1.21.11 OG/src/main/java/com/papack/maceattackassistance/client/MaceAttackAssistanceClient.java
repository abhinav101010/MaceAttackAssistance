/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ClientModInitializer
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
 *  net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
 *  net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
 *  net.fabricmc.fabric.api.event.player.AttackBlockCallback
 *  net.fabricmc.fabric.api.event.player.AttackEntityCallback
 *  net.fabricmc.fabric.api.event.player.UseBlockCallback
 *  net.fabricmc.fabric.api.event.player.UseEntityCallback
 *  net.fabricmc.fabric.api.event.player.UseItemCallback
 *  net.minecraft.util.Hand
 *  net.minecraft.util.ActionResult
 *  net.minecraft.util.TypedActionResult
 *  net.minecraft.entity.passive.PassiveEntity
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EquipmentSlot
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.mob.AmbientEntity
 *  net.minecraft.entity.passive.IronGolemEntity
 *  net.minecraft.entity.mob.WaterCreatureEntity
 *  net.minecraft.entity.mob.Monster
 *  net.minecraft.entity.mob.HostileEntity
 *  net.minecraft.entity.passive.VillagerEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.AxeItem
 *  net.minecraft.item.BlockItem
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.item.SwordItem
 *  net.minecraft.world.World
 *  net.minecraft.block.Block
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.hit.HitResult$class_240
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.block.BlockState
 *  net.minecraft.client.option.KeyBinding
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.block.ShapeContext
 *  net.minecraft.world.RaycastContext
 *  net.minecraft.world.RaycastContext$class_242
 *  net.minecraft.world.RaycastContext$class_3960
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.world.WorldView
 *  net.minecraft.client.option.Perspective
 *  net.minecraft.client.network.ClientPlayerInteractionManager
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.item.MaceItem
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.AutoRefill;
import com.papack.maceattackassistance.client.BeamRenderHandler;
import com.papack.maceattackassistance.client.ColorData;
import com.papack.maceattackassistance.client.Debug;
import com.papack.maceattackassistance.client.DebugScreen;
import com.papack.maceattackassistance.client.ElytraBoost;
import com.papack.maceattackassistance.client.FlappingSuppression;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.JumpController;
import com.papack.maceattackassistance.client.MaceParticle;
import com.papack.maceattackassistance.client.MacroController;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.SwitchAssist;
import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.ToggleElytra;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.ZoomState;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.client.config.ConfigOperation;
import com.papack.maceattackassistance.client.config.ModConfigScreen;
import com.papack.maceattackassistance.mixin.KeyBindingInvoker;
import com.papack.maceattackassistance.mixin.MinecraftClientAccessor;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.PlayerInput;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.block.ShapeContext;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.WorldView;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.MaceItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(value=EnvType.CLIENT)
public class MaceAttackAssistanceClient
implements ClientModInitializer {
    public static useEntityEventRefillStatus useEventRefillStatus;
    public static final String MOD_ID = "maceattackassistance";
    public static Logger LOGGER;
    public static boolean shouldShieldBreak;
    public static int ex_previous_slot;
    public static int ex_preStun_slot;
    public static boolean flag_attack_canceled;
    public static boolean flag_charging_particle;
    public static boolean should_attack_interval;
    public static int waiting_tick_counter;
    public static int jumpCooldown;
    private static boolean shouldPressJump;
    private static Entity targetMob;
    private float targetYaw;
    private float targetPitch;
    private static int target_life_counter;
    private static final int TARGET_LIFE_COUNT = 20;
    public static int nonEventRefills;
    private static int useEntityEventRefillCounter;
    private boolean instant_attack;
    private static boolean wasEating;
    public static boolean requireChargeJump;
    private static int counterAfterChargeJump;
    private static int worldCheckCounter;

    public static Entity getTargetMob() {
        return targetMob;
    }

    public void onInitializeClient() {
        Config.initialize();
        JobManager.init();
        TickScheduler.init();
        ZoomState.init();
        ColorData.initialize();
        try {
            ConfigOperation.existFile();
            ConfigOperation.loadFile();
            ConfigOperation.saveFile();
            LOGGER.info("load config : completed");
        }
        catch (Exception e) {
            LOGGER.error("load config : failed");
        }
        HudRenderCallback.EVENT.register((drawContext, delta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.currentScreen == null) {
                if (Config.DEBUG_SCREEN) {
                    DebugScreen.debugOverlay(drawContext);
                }
                if (Config.ZOOM_CAMERA && Config.PERSPECTIVE_BACK_CROSSHAIR && client.options.getPerspective() == Perspective.THIRD_PERSON_BACK) {
                    ZoomState.renderCrosshair(drawContext);
                }
            }
        });
        WorldRenderEvents.START_MAIN.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity clientPlayer = client.player;
            if (clientPlayer != null && client.currentScreen == null) {
                ZoomState.moveCamera(client, clientPlayer);
            }
        });
        BeamRenderHandler.register();
        WorldRenderEvents.END_MAIN.register(context -> this.onRenderTick(MinecraftClient.getInstance()));
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (world.isClient() && !player.isCreative() && Utils.checkPlayerUUID((Entity)player) && flag_attack_canceled) {
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient() && !player.isCreative() && Utils.checkPlayerUUID((Entity)player)) {
                ItemStack stack = player.getStackInHand(hand);
                Item item = stack.getItem();
                if (Config.PRIORITIZE_WIND_CHARGE && !ElytraBoost.isElytraBoostIdle() && !stack.isOf(Items.WIND_CHARGE)) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    ClientPlayerInteractionManager interactionManager = client.interactionManager;
                    Hand windChargeHand = Utils.getHandHoldingWindCharge(MinecraftClient.getInstance(), (ClientPlayerEntity)player);
                    if (interactionManager != null && windChargeHand != null) {
                        player.swingHand(windChargeHand);
                        interactionManager.interactItem(player, windChargeHand);
                    }
                    return ActionResult.FAIL;
                }
                if (item instanceof BlockItem) {
                    boolean canPlace;
                    BlockItem blockItem = (BlockItem)item;
                    Block block = blockItem.getBlock();
                    BlockPos placedPos = hitResult.getBlockPos().offset(hitResult.getSide());
                    BlockState state = block.getDefaultState();
                    boolean bl = canPlace = state.canPlaceAt((WorldView)world, placedPos) && world.canPlace(state, placedPos, ShapeContext.absent());
                    if (canPlace && player instanceof ClientPlayerEntity) {
                        ClientPlayerEntity clientPlayer = (ClientPlayerEntity)player;
                        MaceAttackAssistanceClient.autoRefillStackableItems(hand, clientPlayer, false);
                    }
                }
            }
            return ActionResult.PASS;
        });
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ClientPlayerEntity clientPlayer;
            ItemStack activeStack;
            if (world.isClient() && !player.isCreative() && Config.AUTO_REFILL && Utils.checkPlayerUUID((Entity)player) && (activeStack = (clientPlayer = (ClientPlayerEntity)player).getStackInHand(hand)).isStackable()) {
                if (activeStack.getCount() < 2) {
                    MaceAttackAssistanceClient.refillInAdvance(clientPlayer);
                }
                useEventRefillStatus = new useEntityEventRefillStatus(hand, activeStack.getCount());
                useEntityEventRefillCounter = 3;
            }
            return ActionResult.PASS;
        });
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack itemStack;
            if (Config.PRIORITIZE_WIND_CHARGE && world.isClient() && !ElytraBoost.isElytraBoostIdle() && Utils.checkPlayerUUID((Entity)player) && !(itemStack = player.getStackInHand(hand)).isOf(Items.WIND_CHARGE)) {
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!Config.EXTREME && world.isClient() && !player.isCreative() && Utils.checkPlayerUUID((Entity)player) && entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                if (player instanceof ClientPlayerEntity) {
                    ClientPlayerEntity clientPlayer = (ClientPlayerEntity)player;
                    if (!JobManager.checkOrderIsEmpty() && JobManager.checkStatus(StatusType.AIR_BREACH) && JobManager.checkValue(StatusType.AIR_BREACH) == 5) {
                        return ActionResult.FAIL;
                    }
                    return MacroController.macroController(clientPlayer, world, livingEntity, 1);
                }
            }
            return ActionResult.PASS;
        });
        ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            Entity cameraEntity = ((MinecraftClientAccessor)MinecraftClient.getInstance()).accessorCameraEntity();
            if (world.isClient() && cameraEntity != null && entity.getUuid().equals(cameraEntity.getUuid())) {
                KeyBinding.unpressAll();
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerInteractionManager interactionManager = client.interactionManager;
            ClientPlayerEntity clientPlayer = client.player;
            if (client.currentScreen == null && clientPlayer != null && interactionManager != null) {
                boolean canZoom;
                int slot;
                if (counterAfterChargeJump > 0) {
                    --counterAfterChargeJump;
                }
                if (Config.FLAP_SUPPRESSION) {
                    FlappingSuppression.tick(clientPlayer);
                    if (FlappingSuppression.isRecentlyFluttering() && FlappingSuppression.jumpSuppressionCounter == 0) {
                        FlappingSuppression.jumpSuppressionCounter = Config.FLAP_SUPPRESSION_TICK;
                    }
                }
                if (worldCheckCounter > 0) {
                    --worldCheckCounter;
                }
                if (client.world != null && worldCheckCounter == 0) {
                    boolean allow;
                    BeamRenderHandler.clearTargetList();
                    boolean bl = allow = Config.DEBUG_SCREEN || Config.TARGET_SEARCH_MODE;
                    if (allow || clientPlayer.isGliding()) {
                        BeamRenderHandler.targetList = BeamRenderHandler.getWorldEntityList(client, clientPlayer, (World)client.world);
                    }
                    if (targetMob != null && BeamRenderHandler.targetList != null) {
                        BeamRenderHandler.targetList.add(targetMob);
                    }
                    worldCheckCounter = Config.RADAR_UPDATE_INTERVAL;
                }
                if (Config.EXTREME && ex_previous_slot > -1 && !client.options.attackKey.isPressed()) {
                    if (Config.RETURN_TO_PREV_SLOT) {
                        clientPlayer.getInventory().setSelectedSlot(ex_previous_slot);
                    }
                    if (should_attack_interval) {
                        if (clientPlayer.isOnGround()) {
                            targetMob = null;
                        }
                        clientPlayer.onLanding();
                        if (clientPlayer.isOnGround()) {
                            ((KeyBindingInvoker)client.options.attackKey).invokerReset();
                        }
                    }
                    ex_previous_slot = -1;
                    ex_preStun_slot = -1;
                    waiting_tick_counter = -1;
                    should_attack_interval = false;
                }
                if (requireChargeJump) {
                    MaceAttackAssistanceClient.chargeJump(clientPlayer, interactionManager);
                    requireChargeJump = false;
                }
                JumpController.tick();
                ElytraBoost.elytraBoost(client, clientPlayer);
                JobManager.tick(client, clientPlayer);
                if (useEntityEventRefillCounter > 0 && useEventRefillStatus != null && --useEntityEventRefillCounter == 0 && MaceAttackAssistanceClient.useEventRefillStatus.beforeCount > clientPlayer.getStackInHand(MaceAttackAssistanceClient.useEventRefillStatus.hand).getCount()) {
                    MaceAttackAssistanceClient.autoRefillStackableItems(MaceAttackAssistanceClient.useEventRefillStatus.hand, clientPlayer, true);
                    useEventRefillStatus = null;
                }
                if (Config.AUTO_REFILL) {
                    ItemStack itemStack = clientPlayer.getMainHandStack();
                    if (itemStack.get(DataComponentTypes.FOOD) != null) {
                        if (client.player.isUsingItem()) {
                            if (!wasEating) {
                                wasEating = true;
                            }
                        } else if (wasEating) {
                            slot = clientPlayer.getInventory().getSelectedSlot();
                            if (AutoRefill.checkStockQuantity(clientPlayer, slot)) {
                                AutoRefill.autoRefill(interactionManager, clientPlayer, 36 + slot);
                            }
                            wasEating = false;
                        }
                    } else {
                        wasEating = false;
                    }
                }
                if (nonEventRefills > -1) {
                    client.execute(() -> {
                        if (AutoRefill.checkStockQuantity(clientPlayer, nonEventRefills)) {
                            AutoRefill.autoRefill(interactionManager, clientPlayer, 36 + nonEventRefills);
                        }
                        nonEventRefills = -1;
                    });
                }
                this.jumpSpam(client, clientPlayer);
                if (targetMob != null) {
                    if (target_life_counter > 0) {
                        --target_life_counter;
                    } else {
                        targetMob = null;
                    }
                    should_attack_interval = false;
                }
                if (waiting_tick_counter > 0) {
                    --waiting_tick_counter;
                }
                this.onClientTick(client, clientPlayer);
                boolean bl = flag_charging_particle = !flag_charging_particle;
                if (flag_charging_particle && Config.ATTACK_ASSISTANCE && MaceAttackAssistanceClient.playerCheck(client, clientPlayer) && clientPlayer.getVelocity().getY() < Config.VELOCITY_BY_DISTANCE[Config.HEIGHT_THRESHOLD] && MaceAttackAssistanceClient.isAllowedItem(clientPlayer.getMainHandStack()) && Config.MACE_PARTICLE != Config.WeaponParticle.off && !client.options.hudHidden) {
                    MaceParticle.maceParticleHandler(client, clientPlayer, clientPlayer.getVelocity().getY() <= Config.VELOCITY_BY_DISTANCE[Config.PARTICLE_TRANSITION_THRESHOLD]);
                }
                client.execute(() -> {
                    if (flag_attack_canceled && !client.options.attackKey.isPressed()) {
                        ((KeyBindingInvoker)client.options.attackKey).invokerReset();
                        flag_attack_canceled = false;
                    }
                });
                while (Config.BULK_REFILL_KEY.wasPressed()) {
                    if (client.currentScreen != null || !JobManager.checkOrderIsEmpty() || !JobManager.keyReleased(client)) continue;
                    for (int i = 0; i < 10; ++i) {
                        if (!AutoRefill.checkStockQuantity(clientPlayer, i)) continue;
                        AutoRefill.autoRefill(interactionManager, clientPlayer, 36 + i);
                    }
                }
                boolean bl2 = canZoom = !clientPlayer.isOnGround() && clientPlayer.isGliding();
                while (Config.ZOOM_CAMERA_KEY.wasPressed()) {
                    if (client.currentScreen != null) continue;
                    ZoomState.zoomCamera(client, clientPlayer, canZoom);
                }
                if (Config.ZOOM_CAMERA && !canZoom) {
                    ZoomState.zoomCamera(client, clientPlayer, false);
                }
                if (ZoomState.gazeCounter > 0) {
                    --ZoomState.gazeCounter;
                }
                while (Config.ENDER_PEARL_KEY.wasPressed()) {
                    if (client.currentScreen != null || JobManager.checkStatus(StatusType.ENDER_PEARL)) continue;
                    slot = clientPlayer.getInventory().getSelectedSlot();
                    JobManager.setOrder(StatusType.ENDER_PEARL, slot);
                }
                while (Config.TOGGLE_ELYTRA_KEY.wasPressed()) {
                    if (client.currentScreen != null) continue;
                    ToggleElytra.toggleElytra(client, clientPlayer);
                }
            }
            while (Config.CONFIG_SCREEN_KEY.wasPressed()) {
                if (client.currentScreen != null) continue;
                if (client.options.sneakKey.isPressed()) {
                    Config.DEBUG_SCREEN = !Config.DEBUG_SCREEN;
                    continue;
                }
                try {
                    client.setScreen(ModConfigScreen.getConfigScreen(null));
                } catch (NoClassDefFoundError ignored) {
                }
            }
            while (Config.ATTACK_SETTING_KEY.wasPressed()) {
                SwitchAssist.changeSettings(SwitchAssist.SwitchKey.ATTACK);
            }
            while (Config.AIM_SETTING_KEY.wasPressed()) {
                SwitchAssist.changeSettings(SwitchAssist.SwitchKey.AIM);
            }
            while (Config.JUMP_SETTING_KEY.wasPressed()) {
                SwitchAssist.changeSettings(SwitchAssist.SwitchKey.JUMP);
            }
            while (Config.SEARCH_SETTING_KEY.wasPressed()) {
                SwitchAssist.changeSettings(SwitchAssist.SwitchKey.TARGET_SEARCH);
            }
            while (Config.RETURN_TO_PREV_SLOT_SETTING_KEY.wasPressed()) {
                SwitchAssist.changeSettings(SwitchAssist.SwitchKey.RETURN_TO_PREV_SLOT);
            }
        });
    }

    public static void autoRefillStackableItems(Hand hand, ClientPlayerEntity clientPlayer, boolean isEntityInteract) {
        ItemStack handStack;
        ItemStack handStackAlias = handStack = hand == Hand.MAIN_HAND ? clientPlayer.getMainHandStack() : clientPlayer.getOffHandStack();
        if (Config.AUTO_REFILL && handStack.isStackable() && (handStack.get(DataComponentTypes.FOOD) == null || isEntityInteract) && !clientPlayer.getItemCooldownManager().isCoolingDown(handStack)) {
            int n = nonEventRefills = hand == Hand.MAIN_HAND ? clientPlayer.getInventory().getSelectedSlot() : 9;
            if (handStack.getCount() < 2) {
                MaceAttackAssistanceClient.refillInAdvance(clientPlayer);
            }
        }
    }

    public static void refillInAdvance(ClientPlayerEntity clientPlayer) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerInteractionManager interactionManager = client.interactionManager;
        if (interactionManager != null && AutoRefill.checkStockQuantity(clientPlayer, nonEventRefills)) {
            AutoRefill.autoRefill(interactionManager, clientPlayer, 36 + nonEventRefills);
        }
    }

    private static boolean playerCheck(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        if (!client.options.attackKey.isPressed()) {
            return false;
        }
        if (clientPlayer.isOnGround()) {
            return false;
        }
        if (clientPlayer.isSubmergedInWater()) {
            return false;
        }
        if (clientPlayer.isSwimming()) {
            return false;
        }
        return !clientPlayer.isSpectator();
    }

    private void onClientTick(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        if (!Config.ATTACK_ASSISTANCE || clientPlayer.getVelocity().getY() > Config.VELOCITY_BY_DISTANCE[Config.HEIGHT_THRESHOLD] || clientPlayer.getVelocity().getY() > 0.0) {
            targetMob = null;
            return;
        }
        targetMob = this.findNearestMob(client, clientPlayer);
        if (targetMob != null) {
            target_life_counter = 20;
            client.execute(() -> this.calculateTargetYawPitch(clientPlayer, targetMob));
        }
    }

    public Entity findNearestMob(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        double downDistance = Math.max(50.0, (double)Config.RADAR_DOWNWARD);
        double closestDistance = Double.MAX_VALUE;
        Entity nearestMob = null;
        Vec3d playerEyePos = clientPlayer.getEyePos();
        Vec3d playerPos = new Vec3d(clientPlayer.getX(), clientPlayer.getY(), clientPlayer.getZ());
        Box pBox = clientPlayer.getBoundingBox();
        Vec3d start = new Vec3d(pBox.minX, playerEyePos.getY(), pBox.minZ);
        Vec3d end = new Vec3d(pBox.maxX, playerEyePos.getY() - downDistance, pBox.maxZ);
        Box searchArea = new Box(start, end).expand(3.5, 0.0, 3.5);
        if (client.world != null) {
            List<Entity> nearbyEntities = client.world.getOtherEntities((Entity)clientPlayer, searchArea, entity -> entity.isLiving() && entity.isAlive() && MaceAttackAssistanceClient.isAllowedTarget(entity) && MaceAttackAssistanceClient.isVisibleFromPlayer(client, clientPlayer, entity));
            for (Entity entity2 : nearbyEntities) {
                double distance = playerPos.distanceTo(new Vec3d(entity2.getX(), entity2.getY(), entity2.getZ()));
                if (Config.AIM_ASSIST && (Config.AIM_MODE == Config.AimMode.Track || Config.AIM_MODE == Config.AimMode.Auto && nearbyEntities.size() < Config.AUTO_MODE_THRESHOLD) && targetMob != null && targetMob.getUuid().equals(entity2.getUuid()) && Utils.isInAttackableRange(clientPlayer, targetMob)) {
                    return targetMob;
                }
                if (!(distance < closestDistance)) continue;
                closestDistance = distance;
                nearestMob = entity2;
            }
        }
        return nearestMob;
    }

    private void calculateTargetYawPitch(ClientPlayerEntity clientPlayer, Entity target) {
        Vec3d playerPos = clientPlayer.getEyePos();
        Vec3d targetPos = Utils.getTargetPos(target, true);
        double dx = targetPos.x - playerPos.x;
        double dy = targetPos.y - playerPos.y;
        double dz = targetPos.z - playerPos.z;
        double distance = Math.sqrt(dx * dx + dz * dz);
        double angle = (Math.toDegrees(Math.atan2(dz, dx)) + 180.0) % 360.0 - 270.0;
        double pitchCorrectionFactor = 0.9 * Math.min(clientPlayer.getVelocity().getY() / 2.0, 1.0);
        this.targetYaw = MaceAttackAssistanceClient.normalizeAngle((float)angle);
        this.targetPitch = (float)(-Math.toDegrees(Math.atan2(dy + pitchCorrectionFactor, distance)));
    }

    public static float normalizeAngle(float angle) {
        if ((angle %= 360.0f) > 180.0f) {
            angle -= 360.0f;
        } else if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    private void onRenderTick(MinecraftClient client) {
        ClientPlayerEntity clientPlayer = client.player;
        if (targetMob == null || clientPlayer == null || !client.options.attackKey.isPressed()) {
            return;
        }
        if (waiting_tick_counter > 0) {
            return;
        }
        float currentYaw = clientPlayer.getYaw();
        float currentPitch = clientPlayer.getPitch();
        float angleDiffYaw = Math.abs(this.targetYaw - currentYaw);
        float angleDiffPitch = Math.abs(this.targetPitch - currentPitch);
        if (clientPlayer.getVelocity().getY() < Config.VELOCITY_BY_DISTANCE[1]) {
            float setValueYaw = this.getMovingDelta(client, clientPlayer, angleDiffYaw, true);
            float setValuePitch = this.getMovingDelta(client, clientPlayer, angleDiffPitch, false);
            if (Config.AIM_ASSIST && !clientPlayer.isGliding()) {
                clientPlayer.setYaw(Utils.lerpYaw(currentYaw, this.targetYaw, setValueYaw));
                clientPlayer.setPitch(Utils.lerpPitch(currentPitch, this.targetPitch, setValuePitch));
            }
        }
        if (this.instant_attack) {
            ClientPlayerInteractionManager interactionManager = client.interactionManager;
            Entity targetedEntity = client.targetedEntity;
            if (targetedEntity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)targetedEntity;
                if (interactionManager != null && client.world != null && !JobManager.checkStatus(StatusType.INSTANT_ATTACK_INTERVAL) && client.options.attackKey.isPressed()) {
                    JobManager.setOrder(StatusType.INSTANT_ATTACK_INTERVAL, -1);
                    clientPlayer.swingHand(clientPlayer.getActiveHand());
                    interactionManager.attackEntity((PlayerEntity)clientPlayer, (Entity)livingEntity);
                }
            }
            this.instant_attack = false;
        }
        if (targetMob != null && client.targetedEntity != null && client.targetedEntity.getUuid().equals(targetMob.getUuid()) && (clientPlayer.isOnGround() || clientPlayer.getVelocity().getY() > Config.VELOCITY_BY_DISTANCE[1])) {
            targetMob = null;
        }
    }

    public static boolean isSimpleVisibleFromPlayer(MinecraftClient client, ClientPlayerEntity clientPlayer, Entity target) {
        Vec3d playerEyePos = clientPlayer.getEyePos();
        Vec3d targetEyePos = target.getEyePos();
        ClientWorld world = client.world;
        if (world != null) {
            BlockHitResult hitResultEye = world.raycast(new RaycastContext(playerEyePos, targetEyePos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)clientPlayer));
            return hitResultEye.getType() != HitResult.Type.BLOCK;
        }
        return false;
    }

    public static boolean isVisibleFromPlayer(MinecraftClient client, ClientPlayerEntity clientPlayer, Entity target) {
        Vec3d playerEyePos = clientPlayer.getEyePos();
        Vec3d targetEyePos = target.getEyePos();
        Vec3d targetCenterPos = new Vec3d(target.getX(), target.getY(), target.getZ());
        ClientWorld world = client.world;
        if (world != null) {
            BlockHitResult hitResultEye = world.raycast(new RaycastContext(playerEyePos, targetEyePos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)clientPlayer));
            BlockHitResult hitResultCenter = world.raycast(new RaycastContext(playerEyePos, targetCenterPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)clientPlayer));
            return hitResultEye.getType() != HitResult.Type.BLOCK || hitResultCenter.getType() != HitResult.Type.BLOCK;
        }
        return false;
    }

    private float getMovingDelta(MinecraftClient client, ClientPlayerEntity clientPlayer, float angleDiff, boolean isYaw) {
        float deltaTime = client.getRenderTickCounter().getTickProgress(true);
        double fallSpeed = Math.abs(clientPlayer.getVelocity().getY());
        double distanceToTarget = clientPlayer.distanceTo(targetMob);
        if (waiting_tick_counter <= 0 && distanceToTarget <= 3.0) {
            this.instant_attack = true;
            return 1.0f;
        }
        if (waiting_tick_counter <= 0 && distanceToTarget <= 4.0 && clientPlayer.getVelocity().getY() < Config.VELOCITY_BY_DISTANCE[10]) {
            return 1.0f;
        }
        double normalizedFallSpeed = Math.abs(Math.min(fallSpeed / 1.5, 1.0));
        double visionMoveSpeed = MaceAttackAssistanceClient.getVisionMoveSpeed(angleDiff, isYaw, normalizedFallSpeed);
        return (float)visionMoveSpeed * deltaTime;
    }

    private static double getVisionMoveSpeed(float angleDiff, boolean isYaw, double normalizedFallSpeed) {
        double visionMoveSpeed;
        double normalizedAngleDiff = Math.min((double)(angleDiff / (float)(isYaw ? 180 : 90)), 1.0);
        if (isYaw) {
            double minSpeed = 0.03 + (double)Config.MAX_SPEED_YAW * 0.04;
            double maxSpeed = 0.07 + (double)Config.MAX_SPEED_YAW * 0.04;
            double sineFactor = 0.5 + Math.sin(Math.PI * normalizedAngleDiff) * 0.5;
            visionMoveSpeed = minSpeed + (maxSpeed - minSpeed) * sineFactor;
        } else {
            double minSpeed = 0.35;
            double maxSpeed = 0.8;
            double factor = Math.pow(normalizedFallSpeed, 0.5);
            visionMoveSpeed = minSpeed + (maxSpeed - minSpeed) * factor;
        }
        return visionMoveSpeed;
    }

    public static boolean isAllowedItem(ItemStack itemStack) {
        if (itemStack != null) {
            return MaceAttackAssistanceClient.isAllowedItem(itemStack.getItem());
        }
        return false;
    }

    public static boolean isAllowedItem(Item item) {
        if (item instanceof MaceItem) {
            return true;
        }
        if (item.getTranslationKey().contains("sword")) {
            return true;
        }
        return item instanceof AxeItem;
    }

    public static boolean isAllowedTarget(Entity entity) {
        if (!Config.AIM_ASSIST) {
            return true;
        }
        if (entity instanceof VillagerEntity && Config.ALLOWED_VILLAGER) {
            return true;
        }
        if (entity instanceof IronGolemEntity && Config.ALLOWED_IRON_GOLEM) {
            return true;
        }
        if (entity instanceof HostileEntity && Config.ALLOWED_HOSTILE) {
            return true;
        }
        if (entity instanceof Monster && Config.ALLOWED_HOSTILE) {
            return true;
        }
        if (entity instanceof PassiveEntity && Config.ALLOWED_PASSIVE) {
            return true;
        }
        if (entity instanceof AmbientEntity && Config.ALLOWED_AMBIENT) {
            return true;
        }
        if (entity instanceof WaterCreatureEntity && Config.ALLOWED_AMBIENT) {
            return true;
        }
        if (entity instanceof PlayerEntity && Config.ALLOWED_PLAYER) {
            return true;
        }
        return Config.ALLOWED_AMBIENT;
    }

    private void jumpSpam(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        if (!Config.JUMP_SPAM) {
            return;
        }
        if (shouldPressJump) {
            PlayerInput oldInput = clientPlayer.input.playerInput;
            clientPlayer.input.playerInput = new PlayerInput(oldInput.forward(), oldInput.backward(), oldInput.left(), oldInput.right(), false, oldInput.sneak(), oldInput.sprint());
            shouldPressJump = false;
        }
        if (this.spamKeysIsPressed(client, clientPlayer) && ElytraBoost.isElytraBoostIdle() && this.verifyPlayerCondition(clientPlayer) && (Utils.verifyGround(clientPlayer, 2) || counterAfterChargeJump > 0) && jumpCooldown == 0) {
            JumpController.setChargeJumpCounter(1);
            shouldPressJump = true;
            jumpCooldown = Config.JUMP_SPAM_TICK;
        }
        if (jumpCooldown > 0) {
            --jumpCooldown;
        }
    }

    private boolean spamKeysIsPressed(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        boolean wearingElytra = ToggleElytra.isElytra(clientPlayer.getEquippedStack(EquipmentSlot.CHEST));
        boolean isFalling = clientPlayer.getVelocity().getY() < (Utils.verifyGround(clientPlayer, 2) ? 0.0 : Config.VELOCITY_BY_DISTANCE[2]);
        boolean isSpacePressed = client.options.jumpKey.isPressed();
        boolean isCtrlPressed = client.options.sprintKey.isPressed();
        boolean isShiftPressed = client.options.sneakKey.isPressed();
        return isSpacePressed && (isCtrlPressed || isShiftPressed || wearingElytra && isFalling);
    }

    private boolean verifyPlayerCondition(ClientPlayerEntity clientPlayer) {
        if (clientPlayer != null) {
            if (clientPlayer.isGliding()) {
                return false;
            }
            if (clientPlayer.isCreative()) {
                return false;
            }
            if (clientPlayer.isSpectator()) {
                return false;
            }
            if (clientPlayer.getVelocity().getY() <= 0.0 && clientPlayer.isGliding()) {
                return false;
            }
            if (!clientPlayer.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA.asItem())) {
                return false;
            }
            return clientPlayer.getEquippedStack(EquipmentSlot.MAINHAND).isOf(Items.WIND_CHARGE) || clientPlayer.getEquippedStack(EquipmentSlot.OFFHAND).isOf(Items.WIND_CHARGE);
        }
        return false;
    }

    public static void chargeJump(ClientPlayerEntity clientPlayer, ClientPlayerInteractionManager interactionManager) {
        if (clientPlayer != null && interactionManager != null) {
            int windChargeSlot = Utils.findWindChargeSlot(clientPlayer);
            if (windChargeSlot >= 0) {
                JumpController.setChargeJumpCounter(1);
                int prevSlot = clientPlayer.getInventory().getSelectedSlot();
                clientPlayer.getInventory().setSelectedSlot(windChargeSlot);
                float prevPitch = clientPlayer.getPitch();
                clientPlayer.setPitch(90.0f);
                clientPlayer.swingHand(Hand.MAIN_HAND);
                interactionManager.interactItem((PlayerEntity)clientPlayer, Hand.MAIN_HAND);
                clientPlayer.setPitch(prevPitch);
                clientPlayer.getInventory().setSelectedSlot(prevSlot);
                counterAfterChargeJump = 20;
                DebugScreen.lastY = clientPlayer.getY();
                Debug.previous_y = -64.0;
            }
        }
        requireChargeJump = false;
    }

    public static void sneakChargeJump(ClientPlayerEntity clientPlayer, int windChargeSlot) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerInteractionManager interactionManager = client.interactionManager;
        if (clientPlayer != null && windChargeSlot >= 0 && interactionManager != null) {
            int prevSlot = clientPlayer.getInventory().getSelectedSlot();
            clientPlayer.getInventory().setSelectedSlot(windChargeSlot);
            float prevPitch = clientPlayer.getPitch();
            clientPlayer.setPitch(90.0f);
            clientPlayer.swingHand(Hand.MAIN_HAND);
            interactionManager.interactItem((PlayerEntity)clientPlayer, Hand.MAIN_HAND);
            clientPlayer.setPitch(prevPitch);
            clientPlayer.getInventory().setSelectedSlot(prevSlot);
            counterAfterChargeJump = 20;
            DebugScreen.lastY = clientPlayer.getY();
            Debug.previous_y = -64.0;
        }
    }

    static {
        LOGGER = LogManager.getLogger((String)MOD_ID);
        shouldShieldBreak = false;
        ex_previous_slot = -1;
        ex_preStun_slot = -1;
        jumpCooldown = 0;
        shouldPressJump = false;
        targetMob = null;
        nonEventRefills = -1;
        useEntityEventRefillCounter = 0;
        requireChargeJump = false;
        counterAfterChargeJump = 0;
        worldCheckCounter = 0;
    }

    public record useEntityEventRefillStatus(Hand hand, int beforeCount) {
    }
}
