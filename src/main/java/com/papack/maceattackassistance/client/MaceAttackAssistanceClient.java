/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ClientModInitializer
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
 *  net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
 *  net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents
 *  net.fabricmc.fabric.api.event.player.AttackBlockCallback
 *  net.fabricmc.fabric.api.event.player.UseBlockCallback
 *  net.fabricmc.fabric.api.event.player.UseEntityCallback
 *  net.fabricmc.fabric.api.event.player.UseItemCallback
 *  net.minecraft.PlayerInput
 *  net.minecraft.Hand
 *  net.minecraft.ActionResult
 *  net.minecraft.PassiveEntity
 *  net.minecraft.Entity
 *  net.minecraft.EquipmentSlot
 *  net.minecraft.LivingEntity
 *  net.minecraft.AmbientEntity
 *  net.minecraft.AnimalEntity
 *  net.minecraft.IronGolemEntity
 *  net.minecraft.WaterCreatureEntity
 *  net.minecraft.Monster
 *  net.minecraft.HostileEntity
 *  net.minecraft.VillagerEntity
 *  net.minecraft.PlayerEntity
 *  net.minecraft.AxeItem
 *  net.minecraft.BlockItem
 *  net.minecraft.Item
 *  net.minecraft.ItemStack
 *  net.minecraft.Items
 *  net.minecraft.World
 *  net.minecraft.Block
 *  net.minecraft.BlockPos
 *  net.minecraft.Box
 *  net.minecraft.Vec3d
 *  net.minecraft.BlockState
 *  net.minecraft.Identifier
 *  net.minecraft.KeyBinding
 *  net.minecraft.MinecraftClient
 *  net.minecraft.ItemTags
 *  net.minecraft.ShapeContext
 *  net.minecraft.WorldView
 *  net.minecraft.Perspective
 *  net.minecraft.ClientPlayerInteractionManager
 *  net.minecraft.ClientPlayerEntity
 *  net.minecraft.DataComponentTypes
 *  net.minecraft.MaceItem
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.AimCondition;
import com.papack.maceattackassistance.client.AutoElytraSwap;
import com.papack.maceattackassistance.client.AutoZoomInOut;
import com.papack.maceattackassistance.client.BeamRenderHandler;
import com.papack.maceattackassistance.client.ColorData;
import com.papack.maceattackassistance.client.Debug;
import com.papack.maceattackassistance.client.DebugScreen;
import com.papack.maceattackassistance.client.ElytraBoost;
import com.papack.maceattackassistance.client.EnderPearlManager;
import com.papack.maceattackassistance.client.FlappingSuppression;
import com.papack.maceattackassistance.client.FriendKeyHandler;
import com.papack.maceattackassistance.client.FriendManager;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.JumpController;
import com.papack.maceattackassistance.client.KeyInput;
import com.papack.maceattackassistance.client.MaceParticle;
import com.papack.maceattackassistance.client.PrevSlotManager;
import com.papack.maceattackassistance.client.RefillManager;
import com.papack.maceattackassistance.client.SpearAttacks;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.ToggleElytra;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.ZoomState;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.client.config.ConfigOperation;
import com.papack.maceattackassistance.mixin.KeyBindingInvoker;
import com.papack.maceattackassistance.mixin.MinecraftClientInvoker;
import com.papack.maceattackassistance.network.MaaPayload;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.passive.AnimalEntity;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.block.ShapeContext;
import net.minecraft.world.WorldView;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.MaceItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(value=EnvType.CLIENT)
public class MaceAttackAssistanceClient
implements ClientModInitializer {
    private static boolean MAA_ALLOWED = false;
    private static boolean ALLOW_SERVER = false;
    private static boolean ALLOW_AIM = false;
    public static useEntityEventRefillStatus useEventRefillStatus;
    public static final String MOD_ID = "maceattackassistance";
    public static Logger LOGGER;
    public static boolean flagPreAxe;
    public static boolean shouldShieldBreak;
    public static boolean flag_attack_canceled;
    public static boolean flag_charging_particle;
    public static boolean should_attack_interval;
    public static int waiting_tick_counter;
    public static int jumpCooldown;
    private static boolean shouldPressJump;
    private static Entity targetMob;
    private static float targetYaw;
    private static float targetPitch;
    private static int target_life_counter;
    private static final int TARGET_LIFE_COUNT = 20;
    private static int useEntityEventRefillCounter;
    private static boolean instant_attack;
    private static boolean wasEating;
    public static boolean requireChargeJump;
    private static int counterAfterChargeJump;
    private static int worldCheckCounter;
    private static boolean tempExtremeFlag;

    public static boolean getTempExtremeFlag() {
        return tempExtremeFlag;
    }

    public static Entity getTargetMob() {
        return targetMob;
    }

    public static void setTargetMob(Entity entity) {
        targetMob = entity;
    }

    public static boolean stopRightNow_ImDisappointedInYou() {
        return !MAA_ALLOWED;
    }

    public void onInitializeClient() {
        Config.initialize();
        JobManager.init();
        TickScheduler.init();
        ZoomState.init();
        ColorData.initialize();
        FriendManager.init();
        try {
            ConfigOperation.existFile();
            ConfigOperation.loadFile();
            ConfigOperation.saveFile();
            LOGGER.info("load config : completed");
        }
        catch (Exception e) {
            LOGGER.error("load config : failed");
        }
        ClientPlayConnectionEvents.INIT.register((handler, client) -> {
            JobManager.JUMP_MODE = false;
            ZoomState.MAAClientState.clear();
        });
        ClientPlayNetworking.registerGlobalReceiver(MaaPayload.ID, (payload, context) -> TickScheduler.setConditionTask(() -> MinecraftClient.getInstance().player != null, () -> {
            ClientPlayerEntity patt0$temp = context.client().player;
            if (patt0$temp instanceof ClientPlayerEntity) {
                ClientPlayerEntity clientPlayer = patt0$temp;
                if (payload.uuid().equals(clientPlayer.getUuid())) {
                    JobManager.JUMP_MODE = true;
                    LOGGER.info("packet received");
                }
            }
        }));
        HudElementRegistry.addLast((Identifier)Identifier.of((String)MOD_ID), (context, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.currentScreen == null) {
                if (Config.DEBUG_SCREEN) {
                    DebugScreen.debugOverlay(context);
                }
                if (Config.ZOOM_CAMERA && Config.PERSPECTIVE_BACK_CROSSHAIR && client.options.getPerspective() == Perspective.THIRD_PERSON_BACK) {
                    ZoomState.renderCrosshair(context);
                }
            }
        });
        WorldRenderEvents.BEFORE_ENTITIES.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity clientPlayer = client.player;
            if (clientPlayer != null) {
                try {
                    ZoomState.moveCamera(client, clientPlayer, client.getRenderTickCounter());
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        WorldRenderEvents.AFTER_ENTITIES.register(context -> MaceAttackAssistanceClient.onRenderTick(MinecraftClient.getInstance()));
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (world.isClient() && !player.isCreative() && Utils.checkPlayerUUID((Entity)player) && flag_attack_canceled) {
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient() && !player.isCreative() && Utils.checkPlayerUUID((Entity)player)) {
                boolean canPlace;
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
                    BlockItem blockItem = (BlockItem)item;
                    Block block = blockItem.getBlock();
                    BlockPos placedPos = hitResult.getBlockPos().offset(hitResult.getSide());
                    BlockState state = block.getDefaultState();
                    canPlace = state.canPlaceAt((WorldView)world, placedPos) && world.canPlace(state, placedPos, ShapeContext.absent());
                } else {
                    canPlace = stack.isStackable();
                }
                if (canPlace && player instanceof ClientPlayerEntity) {
                    ClientPlayerEntity clientPlayer = (ClientPlayerEntity)player;
                    MaceAttackAssistanceClient.autoRefillStackableItems(hand, clientPlayer, false);
                }
            }
            return ActionResult.PASS;
        });
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClient() && !player.isCreative() && Config.AUTO_REFILL && Utils.checkPlayerUUID((Entity)player)) {
                int refillSlot;
                ClientPlayerEntity clientPlayer = (ClientPlayerEntity)player;
                ItemStack activeStack = clientPlayer.getStackInHand(hand);
                int n = refillSlot = hand == Hand.MAIN_HAND ? clientPlayer.getInventory().getSelectedSlot() : 9;
                if (activeStack.isStackable()) {
                    RefillManager.setRefillData(StatusType.AUTO_REFILL, refillSlot, activeStack.getItem(), 0);
                }
            }
            return ActionResult.PASS;
        });
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (Utils.checkPlayerUUID((Entity)player)) {
                Hand windChargeHand;
                if (Config.PRIORITIZE_WIND_CHARGE && world.isClient() && !ElytraBoost.isElytraBoostIdle()) {
                    ItemStack itemStack = player.getStackInHand(hand);
                    if (!itemStack.isOf(Items.WIND_CHARGE)) {
                        return ActionResult.FAIL;
                    }
                } else if (Config.AUTO_WIND_CHARGE_SELECT && Config.JUMP_ASSIST && ElytraBoost.isElytraBoostIdle() && (MinecraftClient.getInstance().options.sprintKey.isPressed() || MinecraftClient.getInstance().options.sneakKey.isPressed()) && !player.isGliding() && !player.isOnGround() && player.getPitch() > 70.0f && !player.getStackInHand(hand).isOf(Items.WIND_CHARGE) && (windChargeHand = Utils.findToSetWindCharge((ClientPlayerEntity)player)) != null) {
                    MaceAttackAssistanceClient.afterJump((ClientPlayerEntity)player, player.getInventory().getSelectedSlot(), 3);
                }
            }
            return ActionResult.PASS;
        });
        ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            Entity cameraEntity = MinecraftClient.getInstance().getCameraEntity();
            if (world.isClient() && cameraEntity != null && entity.getUuid().equals(cameraEntity.getUuid())) {
                KeyBinding.unpressAll();
            }
        });
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            ClientPlayerEntity clientPlayer = client.player;
            if (client.currentScreen == null && clientPlayer != null) {
                SpearAttacks.spearAssist(client, clientPlayer);
                if (instant_attack && client.options.attackKey.isPressed()) {
                    Entity patt0$temp = Utils.getCrosshairEntity();
                    if (patt0$temp instanceof LivingEntity) {
                        LivingEntity target = (LivingEntity)patt0$temp;
                        if (!(JobManager.checkAttackStatus() || Config.EXTREME && target.isBlocking())) {
                            clientPlayer.swingHand(clientPlayer.getActiveHand());
                            if (Config.DEBUG_SCREEN) {
                                LOGGER.info("instant attack : {} , age : {}", (Object)clientPlayer.getMainHandStack().getItem().getTranslationKey(), (Object)clientPlayer.age);
                            }
                            ((MinecraftClientInvoker)client).doAttackInvoker();
                        }
                    }
                    instant_attack = false;
                }
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerInteractionManager interactionManager = client.interactionManager;
            ClientPlayerEntity clientPlayer = client.player;
            boolean flag0 = client.getServer() != null || JobManager.JUMP_MODE;
            boolean flag1 = ZoomState.MAAClientState.allowedLevels > 1;
            JobManager.QUICK_JUMP = flag0 || ZoomState.MAAClientState.allowedLevels == 3;
            boolean bl = JobManager.JUMP_OPTION = flag0 || flag1;
            if (!ZoomState.MAAClientState.antiCheat && client.currentScreen == null && clientPlayer != null && interactionManager != null) {
                boolean vSpeed;
                if (Config.AUTO_ELYTRA && AutoElytraSwap.condition()) {
                    AutoElytraSwap.setFlag(true);
                    Hand hand = AutoElytraSwap.getHandStatus();
                    if (hand != null) {
                        clientPlayer.swingHand(hand);
                        interactionManager.interactItem((PlayerEntity)clientPlayer, hand);
                    }
                }
                AimCondition.tick(clientPlayer);
                EnderPearlManager.tick();
                RefillManager.tick(client, clientPlayer);
                FriendKeyHandler.tick();
                PrevSlotManager.tick(client);
                AutoZoomInOut.autoZoomInOut();
                boolean bl2 = vSpeed = Utils.speedOverThreshold(clientPlayer) > 0;
                if ((Config.DOUBLE_TAP && Utils.canDoubleTap() || Config.STUN_SLAMMING && Utils.canStunSlam(targetMob) || vSpeed) && Config.EXTREME && (client.options.attackKey.isPressed() || vSpeed)) {
                    tempExtremeFlag = true;
                    Config.EXTREME = false;
                }
                if (!(JobManager.checkAttackStatus() || client.options.attackKey.isPressed() || vSpeed || !tempExtremeFlag && !clientPlayer.isOnGround())) {
                    tempExtremeFlag = false;
                    Config.EXTREME = true;
                }
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
                    boolean bl3 = allow = Config.DEBUG_SCREEN || Config.TARGET_SEARCH_MODE;
                    if (allow || clientPlayer.isGliding()) {
                        BeamRenderHandler.targetList = Config.PARALLEL_SEARCH ? BeamRenderHandler.getWorldEntityListParallel(client, clientPlayer, (World)client.world) : BeamRenderHandler.getWorldEntityList(client, clientPlayer, (World)client.world);
                    }
                    if (targetMob != null && BeamRenderHandler.targetList != null) {
                        BeamRenderHandler.targetList.add(targetMob);
                    }
                    worldCheckCounter = Config.RADAR_UPDATE_INTERVAL;
                }
                if (Config.EXTREME && !client.options.attackKey.isPressed() && JobManager.checkOrderIsEmpty()) {
                    if (should_attack_interval) {
                        if (clientPlayer.isOnGround()) {
                            targetMob = null;
                        }
                        clientPlayer.onLanding();
                        if (clientPlayer.isOnGround()) {
                            ((KeyBindingInvoker)client.options.attackKey).invokerReset();
                        }
                    }
                    waiting_tick_counter = -1;
                    should_attack_interval = false;
                    flagPreAxe = false;
                }
                if (requireChargeJump) {
                    MaceAttackAssistanceClient.chargeJump(clientPlayer, interactionManager);
                    requireChargeJump = false;
                }
                JumpController.tick();
                ElytraBoost.elytraBoost(client, clientPlayer);
                JobManager.tick(client, clientPlayer);
                if (SpearAttacks.SPEAR_SLAM_ACTIVE && PrevSlotManager.isEmpty() && !TickScheduler.hasPendingOrReadyDelayedTasks()) {
                    SpearAttacks.SPEAR_SLAM_ACTIVE = false;
                }
                if (useEntityEventRefillCounter > 0 && useEventRefillStatus != null && --useEntityEventRefillCounter == 0 && MaceAttackAssistanceClient.useEventRefillStatus.beforeCount > clientPlayer.getStackInHand(MaceAttackAssistanceClient.useEventRefillStatus.hand).getCount()) {
                    MaceAttackAssistanceClient.autoRefillStackableItems(MaceAttackAssistanceClient.useEventRefillStatus.hand, clientPlayer, true);
                    useEventRefillStatus = null;
                }
                if (Config.AUTO_REFILL) {
                    ItemStack itemStack = clientPlayer.getMainHandStack();
                    if (itemStack.get(DataComponentTypes.FOOD) != null) {
                        if (clientPlayer.isUsingItem()) {
                            if (!wasEating) {
                                wasEating = true;
                            }
                        } else if (wasEating) {
                            int slot = clientPlayer.getInventory().getSelectedSlot();
                            RefillManager.setRefillData(StatusType.AUTO_REFILL, slot, itemStack.getItem(), 0);
                            wasEating = false;
                        }
                    } else {
                        wasEating = false;
                    }
                }
                this.jumpSpam(client, clientPlayer);
                if (targetMob != null && JobManager.checkOrderIsEmpty()) {
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
                boolean bl4 = flag_charging_particle = !flag_charging_particle;
                if (flag_charging_particle && Config.ATTACK_ASSISTANCE && MaceAttackAssistanceClient.playerCheck(client, clientPlayer) && clientPlayer.getVelocity().getY() < Config.VELOCITY_BY_DISTANCE[Config.HEIGHT_THRESHOLD] && MaceAttackAssistanceClient.isAllowedItem(clientPlayer.getMainHandStack()) && Config.MACE_PARTICLE != Config.WeaponParticle.off && !client.options.hudHidden) {
                    MaceParticle.maceParticleHandler(client, clientPlayer, clientPlayer.getVelocity().getY() <= Config.VELOCITY_BY_DISTANCE[Config.PARTICLE_TRANSITION_THRESHOLD]);
                }
                client.execute(() -> {
                    if (flag_attack_canceled && !client.options.attackKey.isPressed()) {
                        ((KeyBindingInvoker)client.options.attackKey).invokerReset();
                        flag_attack_canceled = false;
                    }
                });
                KeyInput.keyInputInGame(client, clientPlayer, interactionManager);
            }
        });
    }

    public static void autoRefillStackableItems(Hand hand, ClientPlayerEntity clientPlayer, boolean isEntityInteract) {
        ItemStack handStack = hand == Hand.MAIN_HAND ? clientPlayer.getMainHandStack() : clientPlayer.getOffHandStack();
        if (Config.AUTO_REFILL && handStack.isStackable() && (handStack.get(DataComponentTypes.FOOD) == null || isEntityInteract) && !clientPlayer.getItemCooldownManager().isCoolingDown(handStack)) {
            int refillSlot = hand == Hand.MAIN_HAND ? clientPlayer.getInventory().getSelectedSlot() : 9;
            RefillManager.setRefillData(StatusType.AUTO_REFILL, refillSlot, handStack.getItem(), 0);
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
        if (JobManager.checkOrderIsEmpty() && (!Config.ATTACK_ASSISTANCE || clientPlayer.getVelocity().getY() > Config.VELOCITY_BY_DISTANCE[Config.HEIGHT_THRESHOLD] || clientPlayer.getVelocity().getY() > 0.0) && !JobManager.checkAttackStatus()) {
            targetMob = null;
            return;
        }
        targetMob = Config.AIM_RAYCAST ? Utils.findCrosshairClosestTarget((PlayerEntity)clientPlayer, Config.AIM_RAYCAST_RANGE, Config.AIM_RAYCAST_RADIUS) : this.findNearestMob(client, clientPlayer);
        if (targetMob != null) {
            target_life_counter = 20;
            MaceAttackAssistanceClient.calculateTargetYawPitch(clientPlayer, targetMob);
        }
    }

    public Entity findNearestMob(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        double downDistance = Math.max(50.0, (double)Config.RADAR_DOWNWARD);
        double closestDistance = Double.MAX_VALUE;
        Entity nearestMob = null;
        Vec3d playerEyePos = clientPlayer.getEyePos();
        Vec3d playerPos = clientPlayer.getEntityPos();
        Box pBox = clientPlayer.getBoundingBox();
        Vec3d start = new Vec3d(pBox.minX, playerEyePos.getY() + 3.0, pBox.minZ);
        Vec3d end = new Vec3d(pBox.maxX, playerEyePos.getY() - downDistance, pBox.maxZ);
        Box searchArea = new Box(start, end).expand(5.0, 0.0, 5.0);
        if (client.world != null) {
            List<Entity> nearbyEntities = client.world.getOtherEntities((Entity)clientPlayer, searchArea, entity -> (!Config.FRIEND_PROTECTION || !FriendManager.isFriend(entity.getUuid())) && entity.isLiving() && entity.isAlive() && MaceAttackAssistanceClient.isAllowedTarget(entity) && Utils.isVisibleFromPlayer(client, clientPlayer, entity));
            for (Entity entity2 : nearbyEntities) {
                double getDistance = playerPos.distanceTo(entity2.getEntityPos());
                if (Config.AIM_ASSIST && (Config.AIM_MODE == Config.AimMode.Track || Config.AIM_MODE == Config.AimMode.Auto && nearbyEntities.size() < Config.AUTO_MODE_THRESHOLD) && targetMob != null && targetMob.getUuid().equals(entity2.getUuid()) && Utils.isInAttackableRange(clientPlayer, targetMob)) {
                    return entity2;
                }
                if (!(getDistance < closestDistance)) continue;
                closestDistance = getDistance;
                nearestMob = entity2;
            }
        }
        return nearestMob;
    }

    private static void calculateTargetYawPitch(ClientPlayerEntity clientPlayer, Entity target) {
        Vec3d playerPos = clientPlayer.getEyePos();
        Vec3d targetPos = Utils.getTargetPos(target, true, 1.0f);
        double dx = targetPos.x - playerPos.x;
        double dy = targetPos.y - playerPos.y;
        double dz = targetPos.z - playerPos.z;
        double getDistance = Math.sqrt(dx * dx + dz * dz);
        double angle = (Math.toDegrees(Math.atan2(dz, dx)) + 180.0) % 360.0 - 270.0;
        double pitchCorrectionFactor = 0.9 * Math.min(clientPlayer.getVelocity().getY() / 2.0, 1.0);
        targetYaw = Utils.normalizeAngle((float)angle);
        targetPitch = (float)(-Math.toDegrees(Math.atan2(dy + pitchCorrectionFactor, getDistance)));
    }

    public static void onRenderTick(MinecraftClient client) {
        boolean flag;
        ClientPlayerEntity clientPlayer = client.player;
        if (targetMob == null || clientPlayer == null || !client.options.attackKey.isPressed() || !JobManager.QUICK_JUMP && targetMob instanceof PlayerEntity) {
            return;
        }
        if (waiting_tick_counter > 0) {
            return;
        }
        float currentYaw = clientPlayer.getYaw();
        float currentPitch = clientPlayer.getPitch();
        float angleDiffYaw = Math.abs(targetYaw - currentYaw);
        float angleDiffPitch = Math.abs(targetPitch - currentPitch);
        boolean bl = Config.AIM_FALL_THRESHOLD == 2 ? clientPlayer.getVelocity().getY() < Config.VELOCITY_BY_DISTANCE[1] : AimCondition.canAim();
        if (bl || JobManager.checkAttackStatus()) {
            boolean d;
            float setValueYaw = MaceAttackAssistanceClient.getMovingDelta(client, clientPlayer, angleDiffYaw, true);
            float setValuePitch = MaceAttackAssistanceClient.getMovingDelta(client, clientPlayer, angleDiffPitch, false);
            float range = Utils.rangeByWeapons(clientPlayer);
            boolean bl2 = d = targetMob != null && clientPlayer.distanceTo(targetMob) <= range;
            if (Config.AIM_ASSIST && (!clientPlayer.isGliding() || Utils.canDoubleTap() && d) && (!Config.LEGACY_AIM_MODE || d)) {
                clientPlayer.setYaw(Utils.lerpYaw(currentYaw, targetYaw, setValueYaw));
                clientPlayer.setPitch(Utils.lerpPitch(currentPitch, targetPitch, setValuePitch));
            }
        }
        if (targetMob != null && client.targetedEntity != null && client.targetedEntity.getUuid().equals(targetMob.getUuid()) && (clientPlayer.isOnGround() || clientPlayer.getVelocity().getY() > Config.VELOCITY_BY_DISTANCE[1]) && JobManager.checkOrderIsEmpty()) {
            targetMob = null;
        }
    }

    private static float getMovingDelta(MinecraftClient client, ClientPlayerEntity clientPlayer, float angleDiff, boolean isYaw) {
        boolean aimFlag;
        float deltaTime = client.getRenderTickCounter().getDynamicDeltaTicks();
        double fallSpeed = Math.abs(clientPlayer.getVelocity().getY());
        double distanceToTarget = clientPlayer.distanceTo(targetMob);
        double distanceThreshold = Utils.rangeByWeapons(clientPlayer);
        boolean bl = aimFlag = (Config.LEGACY_AIM_MODE || Config.AIM_FORCED_ADJUSTMENT) && !Config.AIM_RAYCAST;
        if (waiting_tick_counter <= 0 && distanceToTarget <= distanceThreshold) {
            instant_attack = true;
            if (aimFlag) {
                return 1.0f;
            }
        }
        if (aimFlag && waiting_tick_counter <= 0 && distanceToTarget <= distanceThreshold && clientPlayer.getVelocity().getY() < Config.VELOCITY_BY_DISTANCE[10]) {
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
            if (itemStack.isIn(ItemTags.SWORDS)) {
                return true;
            }
            if (itemStack.getItem() instanceof MaceItem) {
                return true;
            }
            return itemStack.getItem() instanceof AxeItem;
        }
        return false;
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
        if (entity instanceof AnimalEntity && Config.ALLOWED_PASSIVE) {
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
        return !Config.FRIEND_PROTECTION || !FriendManager.isFriend(entity.getUuid());
    }

    private void jumpSpam(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        double vy = clientPlayer.getVelocity().getY();
        if (!Config.JUMP_SPAM) {
            return;
        }
        if (!ElytraBoost.isElytraBoostIdle()) {
            return;
        }
        if (vy > 0.0 && vy < (double)Config.JUMP_SPAM_THRESHOLD * 0.1) {
            return;
        }
        if (shouldPressJump) {
            PlayerInput clientPlayerInput = clientPlayer.input.playerInput;
            clientPlayer.input.playerInput = new PlayerInput(clientPlayerInput.forward(), clientPlayerInput.backward(), clientPlayerInput.left(), clientPlayerInput.right(), false, clientPlayerInput.sneak(), clientPlayerInput.sprint());
            shouldPressJump = false;
        }
        if (this.jumpSpamCondition(client, clientPlayer)) {
            JumpController.setChargeJumpCounter(1);
            shouldPressJump = true;
            jumpCooldown = Config.JUMP_SPAM_TICK;
        }
        if (jumpCooldown > 0) {
            --jumpCooldown;
        }
    }

    private boolean jumpSpamCondition(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        if (!this.spamKeysIsPressed(client, clientPlayer)) {
            return false;
        }
        if (!this.verifyPlayerCondition(clientPlayer)) {
            return false;
        }
        if (!Utils.verifyGround(clientPlayer, 2) && counterAfterChargeJump <= 0) {
            return false;
        }
        return jumpCooldown == 0;
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
            return clientPlayer.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA);
        }
        return false;
    }

    public static void chargeJump(ClientPlayerEntity clientPlayer, ClientPlayerInteractionManager interactionManager) {
        Hand hand;
        if (clientPlayer != null && interactionManager != null && (hand = Utils.getHandHoldingWindCharge(MinecraftClient.getInstance(), clientPlayer)) != null) {
            int refillSlot = hand == Hand.MAIN_HAND ? clientPlayer.getInventory().getSelectedSlot() : 9;
            RefillManager.setRefillData(StatusType.AUTO_REFILL, refillSlot, clientPlayer.getStackInHand(hand).getItem(), 3);
            JumpController.setChargeJumpCounter(0);
            float prevPitch = clientPlayer.getPitch();
            clientPlayer.setPitch(90.0f);
            clientPlayer.swingHand(hand);
            interactionManager.interactItem((PlayerEntity)clientPlayer, hand);
            clientPlayer.setPitch(prevPitch);
            counterAfterChargeJump = 20;
            MaceAttackAssistanceClient.afterJump(clientPlayer, clientPlayer.getInventory().getSelectedSlot(), -1);
            DebugScreen.lastY = clientPlayer.getY();
            Debug.previous_y = -64.0;
        }
        requireChargeJump = false;
    }

    public static void sneakChargeJump(ClientPlayerEntity clientPlayer, Hand hand) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerInteractionManager interactionManager = client.interactionManager;
        if (clientPlayer != null && hand != null && interactionManager != null) {
            int refillSlot = hand == Hand.MAIN_HAND ? clientPlayer.getInventory().getSelectedSlot() : 9;
            RefillManager.setRefillData(StatusType.AUTO_REFILL, refillSlot, clientPlayer.getStackInHand(hand).getItem(), 3);
            float prevPitch = clientPlayer.getPitch();
            clientPlayer.setPitch(90.0f);
            clientPlayer.swingHand(hand);
            interactionManager.interactItem((PlayerEntity)clientPlayer, hand);
            clientPlayer.setPitch(prevPitch);
            counterAfterChargeJump = 20;
            MaceAttackAssistanceClient.afterJump(clientPlayer, clientPlayer.getInventory().getSelectedSlot(), -1);
            DebugScreen.lastY = clientPlayer.getY();
            Debug.previous_y = -64.0;
        }
    }

    public static void afterJump(ClientPlayerEntity clientPlayer, int selectedSlot, int wait) {
        if (wait < 1) {
            wait = Utils.isUsingElytra(clientPlayer) ? 5 : 1;
        }
        PrevSlotManager.setPrevSlot(StatusType.NONE, selectedSlot, wait);
    }

    public static void setAttackInterval() {
        should_attack_interval = true;
        waiting_tick_counter = 10;
    }

    static {
        LOGGER = LogManager.getLogger((String)MOD_ID);
        flagPreAxe = false;
        shouldShieldBreak = false;
        jumpCooldown = 0;
        shouldPressJump = false;
        targetMob = null;
        useEntityEventRefillCounter = 0;
        requireChargeJump = false;
        counterAfterChargeJump = 0;
        worldCheckCounter = 0;
        tempExtremeFlag = false;
    }

    public record useEntityEventRefillStatus(Hand hand, int beforeCount) {
    }
}
