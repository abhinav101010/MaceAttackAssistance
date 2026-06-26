/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants$Key
 *  net.fabricmc.api.ClientModInitializer
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
 *  net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
 *  net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents
 *  net.fabricmc.fabric.api.event.player.AttackBlockCallback
 *  net.fabricmc.fabric.api.event.player.UseBlockCallback
 *  net.fabricmc.fabric.api.event.player.UseEntityCallback
 *  net.fabricmc.fabric.api.event.player.UseItemCallback
 *  net.minecraft.client.CameraType
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.MultiPlayerGameMode
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.resources.Identifier
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.ambient.AmbientCreature
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.animal.fish.WaterAnimal
 *  net.minecraft.world.entity.animal.golem.IronGolem
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.npc.villager.Villager
 *  net.minecraft.world.entity.player.Input
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.AxeItem
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.MaceItem
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package com.papack.maceattackassistance.client;

import com.mojang.blaze3d.platform.InputConstants;
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
import com.papack.maceattackassistance.client.InstantAim;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.JumpController;
import com.papack.maceattackassistance.client.KeyInput;
import com.papack.maceattackassistance.client.KnockbackDisplacement;
import com.papack.maceattackassistance.client.MAAState;
import com.papack.maceattackassistance.client.MaceParticle;
import com.papack.maceattackassistance.client.PredictedLandingPosition;
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
import com.papack.maceattackassistance.mixin.KeyMappingInvoker;
import com.papack.maceattackassistance.network.MaaPayload;
import com.papack.maceattackassistance.network.MaaSyncPayload;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(value=EnvType.CLIENT)
public class MaceAttackAssistanceClient
implements ClientModInitializer {
    public static useEntityEventRefillStatus useEventRefillStatus;
    public static final String MOD_ID = "maceattackassistance";
    public static Logger LOGGER;
    public static boolean flagPreAxe;
    public static boolean flagStunSlam;
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
    private static boolean wasEating;
    public static boolean requireChargeJump;
    private static int counterAfterChargeJump;
    private static int worldCheckCounter;
    private static boolean tempExtremeFlag;
    private static boolean flag_aimingElytra;
    private static boolean flagElytraSpear;

    public static boolean getFlagElytraSpear() {
        return flagElytraSpear;
    }

    public static void setFlagElytraSpear(boolean flag) {
        flagElytraSpear = flag;
    }

    public static boolean getAimingElytra() {
        return flag_aimingElytra;
    }

    public static boolean getTempExtremeFlag() {
        return tempExtremeFlag;
    }

    public static Entity getTargetMob() {
        return targetMob;
    }

    public static void setTargetMob(Entity entity) {
        targetMob = entity;
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
            LOGGER.info("exist config : completed");
        }
        catch (Exception e) {
            LOGGER.error("exist config : failed");
        }
        try {
            ConfigOperation.loadFile();
            LOGGER.info("load config : completed");
        }
        catch (Exception e) {
            LOGGER.error("load config : failed");
        }
        try {
            ConfigOperation.saveFile();
            LOGGER.info("save config : completed");
        }
        catch (Exception e) {
            LOGGER.error("save config : failed");
        }
        ClientPlayConnectionEvents.INIT.register((ignored, ignored1) -> {
            MAAState.f2 = false;
            MAAState.clear();
        });
        ClientPlayConnectionEvents.DISCONNECT.register((ignored, ignored1) -> {
            MAAState.clear();
            MAAState.f2 = false;
            MAAState.f1 = false;
            MAAState.f3 = false;
            MaceAttackAssistanceClient.targetMob = null;
            LOGGER.info("MAA state reset on disconnect");
        });
        ClientPlayNetworking.registerGlobalReceiver(MaaPayload.ID, (payload, context) -> {
            try {
                TickScheduler.setConditionTask(() -> Minecraft.getInstance().player != null, () -> {
                    LocalPlayer patt0$temp = context.client().player;
                    if (patt0$temp instanceof LocalPlayer) {
                        LocalPlayer clientPlayer = patt0$temp;
                        if (payload.uuid().equals(clientPlayer.getUUID())) {
                            MAAState.f2 = true;
                            LOGGER.info("packet received");
                        }
                    }
                });
            }
            catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(MaaSyncPayload.TYPE, (payload, context) -> {
            try {
                TickScheduler.setConditionTask(() -> Minecraft.getInstance().player != null, () -> {
                    LocalPlayer patt0$temp = context.client().player;
                    if (patt0$temp instanceof LocalPlayer) {
                        LocalPlayer clientPlayer = patt0$temp;
                        if (payload.uuid().equals(clientPlayer.getUUID())) {
                            MAAState.allowedLevels = payload.level();
                            MAAState.antiCheat = MAAState.allowedLevels == 0;
                            LOGGER.info("plugin packet received: {}", (Object)payload.level());
                        }
                    }
                });
            }
            catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        });
        HudElementRegistry.addLast((Identifier)Identifier.parse((String)MOD_ID), (context, ignored) -> {
            Minecraft client = Minecraft.getInstance();
            if (client.gui.screen() == null) {
                InstantAim.tick(client);
                if (Config.DEBUG_SCREEN) {
                    DebugScreen.debugOverlay(context);
                }
                if (Config.ZOOM_CAMERA && Config.PERSPECTIVE_BACK_CROSSHAIR && client.options.getCameraType() == CameraType.THIRD_PERSON_BACK) {
                    ZoomState.renderCrosshair(context);
                }
            }
        });
        LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.register(ignored -> {
            Minecraft client = Minecraft.getInstance();
            LocalPlayer clientPlayer = client.player;
            if (clientPlayer != null) {
                try {
                    ZoomState.moveCamera(client, clientPlayer, client.getDeltaTracker());
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        LevelRenderEvents.AFTER_TRANSLUCENT_FEATURES.register(context -> {
            Minecraft client = Minecraft.getInstance();
            MaceAttackAssistanceClient.onRenderTick(client);
            KnockbackDisplacement.onRenderTickForKnockBack(client, context.gameRenderer().mainCamera().getCameraEntityPartialTicks(client.getDeltaTracker()));
            if (Config.DEBUG_LANDING_POS) {
                PredictedLandingPosition.tick(context);
            }
        });
        AttackBlockCallback.EVENT.register((player, world, ignored, ignored1, ignored2) -> {
            if (world.isClientSide() && !player.isCreative() && Utils.checkPlayerUUID((Entity)player) && flag_attack_canceled) {
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClientSide() && !player.isCreative() && Utils.checkPlayerUUID((Entity)player)) {
                boolean canPlace;
                ItemStack stack = player.getItemInHand(hand);
                Item item = stack.getItem();
                if (Config.PRIORITIZE_WIND_CHARGE && !ElytraBoost.isElytraBoostIdle() && !stack.is(Items.WIND_CHARGE)) {
                    Minecraft client = Minecraft.getInstance();
                    MultiPlayerGameMode interactionManager = client.gameMode;
                    InteractionHand windChargeHand = Utils.getHandHoldingWindCharge(Minecraft.getInstance(), (LocalPlayer)player);
                    if (interactionManager != null && windChargeHand != null) {
                        player.swing(windChargeHand);
                        interactionManager.useItem(player, windChargeHand);
                    }
                    return InteractionResult.FAIL;
                }
                if (item instanceof BlockItem) {
                    BlockItem blockItem = (BlockItem)item;
                    Block block = blockItem.getBlock();
                    BlockPos placedPos = hitResult.getBlockPos().relative(hitResult.getDirection());
                    BlockState state = block.defaultBlockState();
                    canPlace = state.canSurvive((LevelReader)world, placedPos) && world.isUnobstructed(state, placedPos, CollisionContext.empty());
                } else {
                    canPlace = stack.isStackable();
                }
                if (canPlace && player instanceof LocalPlayer) {
                    LocalPlayer clientPlayer = (LocalPlayer)player;
                    MaceAttackAssistanceClient.autoRefillStackableItems(hand, clientPlayer, false);
                }
            }
            return InteractionResult.PASS;
        });
        UseEntityCallback.EVENT.register((player, world, hand, ignored, ignored1) -> {
            if (world.isClientSide() && !player.isCreative() && Config.AUTO_REFILL && Utils.checkPlayerUUID((Entity)player)) {
                int refillSlot;
                LocalPlayer clientPlayer = (LocalPlayer)player;
                ItemStack activeStack = clientPlayer.getItemInHand(hand);
                int n = refillSlot = hand == InteractionHand.MAIN_HAND ? clientPlayer.getInventory().getSelectedSlot() : 9;
                if (activeStack.isStackable()) {
                    RefillManager.setRefillData(StatusType.AUTO_REFILL, refillSlot, activeStack.getItem(), 0);
                }
            }
            return InteractionResult.PASS;
        });
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack itemStack;
            if (Utils.checkPlayerUUID((Entity)player) && Config.PRIORITIZE_WIND_CHARGE && world.isClientSide() && !ElytraBoost.isElytraBoostIdle() && !(itemStack = player.getItemInHand(hand)).is(Items.WIND_CHARGE)) {
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });
        ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
            if (world.isClientSide() && cameraEntity != null && entity.getUUID().equals(cameraEntity.getUUID())) {
                KeyMapping.releaseAll();
            }
        });
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            LocalPlayer clientPlayer = client.player;
            if (client.gui.screen() == null && clientPlayer != null && Config.SPEAR_ASSIST) {
                SpearAttacks.spearAssist(client, clientPlayer);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            MultiPlayerGameMode interactionManager = client.gameMode;
            LocalPlayer clientPlayer = client.player;
            boolean flag0 = client.getSingleplayerServer() != null || MAAState.f2;
            boolean flag1 = MAAState.allowedLevels > 1;
            MAAState.f3 = flag0 || MAAState.allowedLevels == 3;
            boolean bl = MAAState.f1 = flag0 || flag1;
            if (!MAAState.antiCheat && client.gui.screen() == null && clientPlayer != null && interactionManager != null) {
                boolean vSpeed;
                double speed;
                double distance;
                if (Config.AUTO_ELYTRA && AutoElytraSwap.condition()) {
                    AutoElytraSwap.setFlag(true);
                    InteractionHand hand = AutoElytraSwap.getHandStatus();
                    if (hand != null) {
                        clientPlayer.swing(hand);
                        interactionManager.useItem((Player)clientPlayer, hand);
                    }
                }
                if (Config.AUTO_LEFT_CLICK && MaceAttackAssistanceClient.getTargetMob() != null && Utils.checkRightBtnAndSpear(client, clientPlayer) && (distance = (double)clientPlayer.distanceTo(MaceAttackAssistanceClient.getTargetMob())) - (speed = Math.abs(clientPlayer.getDeltaMovement().length() * 2.0)) <= 3.0) {
                    TickScheduler.setDelayTask(1, () -> {
                        KeyMapping.click((InputConstants.Key)((KeyMappingInvoker)Minecraft.getInstance().options.keyAttack).accessorBoundKey());
                        KeyMapping.set((InputConstants.Key)((KeyMappingInvoker)Minecraft.getInstance().options.keyAttack).accessorBoundKey(), (boolean)true);
                    });
                    TickScheduler.setDelayTask(3, () -> KeyMapping.set((InputConstants.Key)((KeyMappingInvoker)Minecraft.getInstance().options.keyAttack).accessorBoundKey(), (boolean)false));
                }
                KnockbackDisplacement.onClientTickForKnockBack(client);
                AimCondition.tick(clientPlayer);
                EnderPearlManager.tick();
                RefillManager.tick(client, clientPlayer);
                FriendKeyHandler.tick();
                PrevSlotManager.tick(client);
                AutoZoomInOut.autoZoomInOut();
                boolean bl2 = vSpeed = Utils.speedOverThreshold(clientPlayer) > 0;
                if ((Config.DOUBLE_TAP && Utils.canDoubleTap() || Config.STUN_SLAMMING && Utils.canStunSlam(targetMob) || vSpeed) && Config.EXTREME && (client.options.keyAttack.isDown() || vSpeed)) {
                    tempExtremeFlag = true;
                    Config.EXTREME = false;
                }
                if (!(JobManager.checkAttackStatus() || client.options.keyAttack.isDown() || vSpeed || !tempExtremeFlag && !clientPlayer.onGround())) {
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
                if (client.level != null && worldCheckCounter == 0) {
                    boolean allow;
                    BeamRenderHandler.clearTargetList();
                    boolean bl3 = allow = Config.DEBUG_SCREEN || Config.TARGET_SEARCH_MODE;
                    if (allow || clientPlayer.isFallFlying()) {
                        BeamRenderHandler.targetList = Config.PARALLEL_SEARCH ? BeamRenderHandler.getWorldEntityListParallel(client, clientPlayer, (Level)client.level) : BeamRenderHandler.getWorldEntityList(client, clientPlayer, (Level)client.level, false);
                    }
                    if (targetMob != null && BeamRenderHandler.targetList != null) {
                        BeamRenderHandler.targetList.add(targetMob);
                    }
                    worldCheckCounter = Config.RADAR_UPDATE_INTERVAL;
                }
                if (Config.EXTREME && !client.options.keyAttack.isDown() && JobManager.checkOrderIsEmpty()) {
                    if (should_attack_interval) {
                        if (clientPlayer.onGround()) {
                            targetMob = null;
                        }
                        clientPlayer.resetFallDistance();
                        if (clientPlayer.onGround()) {
                            ((KeyMappingInvoker)client.options.keyAttack).invokerReset();
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
                if (useEntityEventRefillCounter > 0 && useEventRefillStatus != null && --useEntityEventRefillCounter == 0 && MaceAttackAssistanceClient.useEventRefillStatus.beforeCount > clientPlayer.getItemInHand(MaceAttackAssistanceClient.useEventRefillStatus.hand).getCount()) {
                    MaceAttackAssistanceClient.autoRefillStackableItems(MaceAttackAssistanceClient.useEventRefillStatus.hand, clientPlayer, true);
                    useEventRefillStatus = null;
                }
                if (Config.AUTO_REFILL) {
                    ItemStack itemStack = clientPlayer.getMainHandItem();
                    if (itemStack.get(DataComponents.FOOD) != null) {
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
                if (flag_charging_particle && Config.ATTACK_ASSISTANCE && MaceAttackAssistanceClient.playerCheck(client, clientPlayer) && clientPlayer.getDeltaMovement().y() < Config.VELOCITY_BY_DISTANCE[Config.HEIGHT_THRESHOLD] && MaceAttackAssistanceClient.isAllowedItem(clientPlayer.getMainHandItem()) && Config.MACE_PARTICLE != Config.WeaponParticle.off) {
                    MaceParticle.maceParticleHandler(client, clientPlayer, clientPlayer.getDeltaMovement().y() <= Config.VELOCITY_BY_DISTANCE[Config.PARTICLE_TRANSITION_THRESHOLD]);
                }
                client.execute(() -> {
                    if (flag_attack_canceled && !client.options.keyAttack.isDown()) {
                        ((KeyMappingInvoker)client.options.keyAttack).invokerReset();
                        flag_attack_canceled = false;
                    }
                });
                KeyInput.keyInputInGame(client, clientPlayer, interactionManager);
            }
        });
    }

    public static void autoRefillStackableItems(InteractionHand hand, LocalPlayer clientPlayer, boolean isEntityInteract) {
        ItemStack handStack;
        ItemStack itemStack = handStack = hand == InteractionHand.MAIN_HAND ? clientPlayer.getMainHandItem() : clientPlayer.getOffhandItem();
        if (Config.AUTO_REFILL && handStack.isStackable() && (handStack.get(DataComponents.FOOD) == null || isEntityInteract) && !clientPlayer.getCooldowns().isOnCooldown(handStack)) {
            int refillSlot = hand == InteractionHand.MAIN_HAND ? clientPlayer.getInventory().getSelectedSlot() : 9;
            RefillManager.setRefillData(StatusType.AUTO_REFILL, refillSlot, handStack.getItem(), 0);
        }
    }

    private static boolean playerCheck(Minecraft client, LocalPlayer clientPlayer) {
        if (!client.options.keyAttack.isDown()) {
            return false;
        }
        if (clientPlayer.onGround()) {
            return false;
        }
        if (clientPlayer.isUnderWater()) {
            return false;
        }
        if (clientPlayer.isSwimming()) {
            return false;
        }
        return !clientPlayer.isSpectator();
    }

    private void onClientTick(Minecraft client, LocalPlayer clientPlayer) {
        boolean flagAimingElytra = Config.AIM_ELYTRA && Utils.aimingElytra(clientPlayer);
        this.flag_aimingElytra = flagAimingElytra;
        if (JobManager.checkOrderIsEmpty() && (!Config.ATTACK_ASSISTANCE || clientPlayer.getDeltaMovement().y() > Config.VELOCITY_BY_DISTANCE[Config.HEIGHT_THRESHOLD] || clientPlayer.getDeltaMovement().y() > 0.0) && !JobManager.checkAttackStatus() && !flagAimingElytra) {
            targetMob = null;
            return;
        }
        targetMob = clientPlayer.isFallFlying() || !Config.AIM_RAYCAST ? this.findNearestMob(client, clientPlayer) : Utils.findCrosshairClosestTarget((Player)clientPlayer, Config.AIM_RAYCAST_RANGE, Config.AIM_RAYCAST_RADIUS);
        if (targetMob != null) {
            target_life_counter = 20;
            MaceAttackAssistanceClient.calculateTargetYawPitch(clientPlayer, targetMob);
        }
    }

    public Entity findNearestMob(Minecraft client, LocalPlayer clientPlayer) {
        List<Entity> nearbyEntities;
        double downDistance = Math.max(50.0, (double)Config.RADAR_DOWNWARD);
        double closestDistance = Double.MAX_VALUE;
        Entity nearestMob = null;
        Vec3 playerEyePos = clientPlayer.getEyePosition();
        Vec3 playerPos = clientPlayer.position();
        AABB pBox = clientPlayer.getBoundingBox();
        Vec3 start = new Vec3(pBox.minX, playerEyePos.y() + 5.0, pBox.minZ);
        Vec3 end = new Vec3(pBox.maxX, playerEyePos.y() - downDistance, pBox.maxZ);
        AABB searchArea = new AABB(start, end).inflate(8.0, 0.0, 8.0);
        if (client.level != null && (nearbyEntities = clientPlayer.isFallFlying() ? BeamRenderHandler.getWorldEntityList(client, clientPlayer, clientPlayer.level(), true) : client.level.getEntities((Entity)clientPlayer, searchArea, entity -> (!Config.FRIEND_PROTECTION || !FriendManager.isFriend(entity.getUUID())) && entity.showVehicleHealth() && entity.isAlive() && MaceAttackAssistanceClient.isAllowedTarget(entity) && Utils.isVisibleFromPlayer(client, clientPlayer, entity))) != null) {
            // Track mode: prioritize current target if valid
            if (Config.AIM_ASSIST && (Config.AIM_MODE == Config.AimMode.Track || Config.AIM_MODE == Config.AimMode.Auto && nearbyEntities.size() < Config.AUTO_MODE_THRESHOLD)) {
                Entity currentTarget = MaceAttackAssistanceClient.getTargetMob();
                if (currentTarget != null && currentTarget.isAlive() && Utils.isInAttackableRange(clientPlayer, currentTarget)) {
                    // Verify current target is still in the nearby entities list
                    for (Entity entity2 : nearbyEntities) {
                        if (currentTarget.getUUID().equals(entity2.getUUID())) {
                            return entity2;
                        }
                    }
                }
            }
            // Nearest mode or fallback: find closest entity
            for (Entity entity2 : nearbyEntities) {
                double distance = playerPos.distanceTo(entity2.position());
                if (!(distance < closestDistance)) continue;
                closestDistance = distance;
                nearestMob = entity2;
            }
        }
        return nearestMob;
    }

    private static void calculateTargetYawPitch(LocalPlayer clientPlayer, Entity target) {
        Minecraft mc = Minecraft.getInstance();
        float partalTickTime = mc.gameRenderer.mainCamera().getCameraEntityPartialTicks(mc.getDeltaTracker());
        Vec3 playerPos = clientPlayer.getEyePosition(partalTickTime);
        Vec3 targetPos = Utils.getTargetPos(target, true, partalTickTime);
        double dx = targetPos.x - playerPos.x;
        double dy = targetPos.y - playerPos.y;
        double dz = targetPos.z - playerPos.z;
        double distance = Math.sqrt(dx * dx + dz * dz);
        double angle = (Math.toDegrees(Math.atan2(dz, dx)) + 180.0) % 360.0 - 270.0;
        double pitchCorrectionFactor = 0.9 * Math.min(clientPlayer.getDeltaMovement().y() / 2.0, 1.0);
        targetYaw = Utils.normalizeAngle((float)angle);
        targetPitch = (float)(-Math.toDegrees(Math.atan2(dy + pitchCorrectionFactor, distance)));
    }

    public static void onRenderTick(Minecraft client) {
        LocalPlayer clientPlayer = client.player;
        boolean pearlGrapple = JobManager.checkStatus(StatusType.PEARL_GRAPPLE);
        if (targetMob == null || clientPlayer == null || !targetMob.isAlive()) {
            targetMob = null;
            return;
        }
        // Check if we should aim: pearl grapple, spear right-click, flying, attack key down, or aim elytra
        boolean shouldAim = pearlGrapple
            || Utils.checkRightBtnAndSpear(client, clientPlayer)
            || clientPlayer.isFallFlying()
            || ((client.options.keyAttack.isDown() || Config.MANUAL_STUN_SLAM_KEY.isDown()) && (MAAState.f3 || !(targetMob instanceof Player)))
            || flag_aimingElytra;

        if (!shouldAim) {
            return;
        }
        if (waiting_tick_counter > 0) {
            return;
        }
        float currentYaw = clientPlayer.getYRot();
        float currentPitch = clientPlayer.getXRot();
        float angleDiffYaw = Math.abs(targetYaw - currentYaw);
        float angleDiffPitch = Math.abs(targetPitch - currentPitch);
        // Normalize angle differences for wrap-around
        angleDiffYaw = Math.abs(Utils.normalizeAngle(targetYaw - currentYaw));
        angleDiffPitch = Math.abs(Utils.normalizeAngle(targetPitch - currentPitch));

        boolean canAimFlag = Config.AIM_FALL_THRESHOLD == 2
            ? clientPlayer.getDeltaMovement().y() < Config.VELOCITY_BY_DISTANCE[1]
            : AimCondition.canAim();

        if (canAimFlag || pearlGrapple || JobManager.checkAttackStatus()) {
            float setValueYaw = MaceAttackAssistanceClient.getMovingDelta(client, clientPlayer, angleDiffYaw, true);
            float setValuePitch = MaceAttackAssistanceClient.getMovingDelta(client, clientPlayer, angleDiffPitch, false);
            double range = Utils.rangeByWeapons(clientPlayer);
            boolean inRange = targetMob != null && (double)clientPlayer.distanceTo(targetMob) <= range;

            if ((Config.AIM_ASSIST || pearlGrapple) && (!clientPlayer.isFallFlying() || Utils.canDoubleTap() && inRange || flag_aimingElytra) && (!Config.LEGACY_AIM_MODE || inRange)) {
                clientPlayer.setYRot(Utils.lerpYaw(currentYaw, targetYaw, setValueYaw));
                if (!flag_aimingElytra) {
                    clientPlayer.setXRot(Utils.lerpPitch(currentPitch, targetPitch, setValuePitch));
                }
            }
        }
        // Clear target if on ground and not in a job
        if (targetMob != null && clientPlayer.onGround() && JobManager.checkOrderIsEmpty()) {
            targetMob = null;
        }
    }

    private static float getMovingDelta(Minecraft client, LocalPlayer clientPlayer, float angleDiff, boolean isYaw) {
        float deltaTime = client.getDeltaTracker().getGameTimeDeltaTicks();
        double fallSpeed = Math.abs(clientPlayer.getDeltaMovement().y());
        double distanceToTarget = targetMob != null ? clientPlayer.distanceTo(targetMob) : 0.0;
        double distanceThreshold = Utils.rangeByWeapons(clientPlayer);
        boolean aimFlag = (Config.LEGACY_AIM_MODE || Config.AIM_FORCED_ADJUSTMENT) && !Config.AIM_RAYCAST;

        // Instant snap when forced adjustment is enabled and we're in range
        if (waiting_tick_counter <= 0 && distanceToTarget <= distanceThreshold && aimFlag) {
            return 1.0f;
        }
        if (aimFlag && waiting_tick_counter <= 0 && distanceToTarget <= distanceThreshold && clientPlayer.getDeltaMovement().y() < Config.VELOCITY_BY_DISTANCE[10]) {
            return 1.0f;
        }

        // More responsive aiming - higher base speed
        double normalizedFallSpeed = Math.abs(Math.min(fallSpeed / 1.5, 1.0));
        double visionMoveSpeed = MaceAttackAssistanceClient.getVisionMoveSpeed(angleDiff, isYaw, normalizedFallSpeed);

        // Increase responsiveness for smaller angles
        if (angleDiff < 5.0f) {
            visionMoveSpeed *= 2.0; // Snap faster when close to target
        } else if (angleDiff < 15.0f) {
            visionMoveSpeed *= 1.5;
        }

        return (float)visionMoveSpeed * deltaTime;
    }

    private static double getVisionMoveSpeed(float angleDiff, boolean isYaw, double normalizedFallSpeed) {
        double visionMoveSpeed;
        double normalizedAngleDiff = Math.min((double)(angleDiff / (float)(isYaw ? 180 : 90)), 1.0);
        if (isYaw) {
            double maxSpeedYaw = (double)(flag_aimingElytra ? 30 : Config.MAX_SPEED_YAW) * 0.04;
            double minSpeed = 0.03 + maxSpeedYaw;
            double maxSpeed = 0.07 + maxSpeedYaw;
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
            if (itemStack.is(ItemTags.SWORDS)) {
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
        if (entity instanceof Villager && Config.ALLOWED_VILLAGER) {
            return true;
        }
        if (entity instanceof IronGolem && Config.ALLOWED_IRON_GOLEM) {
            return true;
        }
        if (entity instanceof Monster && Config.ALLOWED_HOSTILE) {
            return true;
        }
        if (entity instanceof Enemy && Config.ALLOWED_HOSTILE) {
            return true;
        }
        if (entity instanceof Animal && Config.ALLOWED_PASSIVE) {
            return true;
        }
        if (entity instanceof AgeableMob && Config.ALLOWED_PASSIVE) {
            return true;
        }
        if (entity instanceof AmbientCreature && Config.ALLOWED_AMBIENT) {
            return true;
        }
        if (entity instanceof WaterAnimal && Config.ALLOWED_AMBIENT) {
            return true;
        }
        if (entity instanceof Player && Config.ALLOWED_PLAYER) {
            return !Config.FRIEND_PROTECTION || !FriendManager.isFriend(entity.getUUID());
        }
        return Config.ALLOWED_AMBIENT;
    }

    private void jumpSpam(Minecraft client, LocalPlayer clientPlayer) {
        double vy = clientPlayer.getDeltaMovement().y();
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
            Input clientPlayerInput = clientPlayer.input.keyPresses;
            clientPlayer.input.keyPresses = new Input(clientPlayerInput.forward(), clientPlayerInput.backward(), clientPlayerInput.left(), clientPlayerInput.right(), false, clientPlayerInput.shift(), clientPlayerInput.sprint());
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

    private boolean jumpSpamCondition(Minecraft client, LocalPlayer clientPlayer) {
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

    private boolean spamKeysIsPressed(Minecraft client, LocalPlayer clientPlayer) {
        boolean wearingElytra = ToggleElytra.isElytra(clientPlayer.getItemBySlot(EquipmentSlot.CHEST));
        boolean isFalling = clientPlayer.getDeltaMovement().y() < (Utils.verifyGround(clientPlayer, 2) ? 0.0 : Config.VELOCITY_BY_DISTANCE[2]);
        boolean isSpacePressed = client.options.keyJump.isDown();
        boolean isCtrlPressed = client.options.keySprint.isDown();
        boolean isShiftPressed = client.options.keyShift.isDown();
        return isSpacePressed && (isCtrlPressed || isShiftPressed || wearingElytra && isFalling);
    }

    private boolean verifyPlayerCondition(LocalPlayer clientPlayer) {
        if (clientPlayer != null) {
            if (clientPlayer.isFallFlying()) {
                return false;
            }
            if (clientPlayer.isCreative()) {
                return false;
            }
            if (clientPlayer.isSpectator()) {
                return false;
            }
            if (clientPlayer.getDeltaMovement().y() <= 0.0 && clientPlayer.isFallFlying()) {
                return false;
            }
            return clientPlayer.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA);
        }
        return false;
    }

    public static void chargeJump(LocalPlayer clientPlayer, MultiPlayerGameMode interactionManager) {
        InteractionHand hand;
        if (clientPlayer != null && interactionManager != null && (hand = Utils.getHandHoldingWindCharge(Minecraft.getInstance(), clientPlayer)) != null) {
            int refillSlot = hand == InteractionHand.MAIN_HAND ? clientPlayer.getInventory().getSelectedSlot() : 9;
            RefillManager.setRefillData(StatusType.AUTO_REFILL, refillSlot, clientPlayer.getItemInHand(hand).getItem(), 3);
            JumpController.setChargeJumpCounter(0);
            float prevPitch = clientPlayer.getXRot();
            clientPlayer.setXRot(90.0f);
            clientPlayer.swing(hand);
            interactionManager.useItem((Player)clientPlayer, hand);
            clientPlayer.setXRot(prevPitch);
            counterAfterChargeJump = 20;
            MaceAttackAssistanceClient.afterJump(clientPlayer, clientPlayer.getInventory().getSelectedSlot(), -1);
            DebugScreen.lastY = clientPlayer.getY();
            Debug.previous_y = -64.0;
        }
        requireChargeJump = false;
    }

    public static void sneakChargeJump(LocalPlayer clientPlayer, InteractionHand hand) {
        Minecraft client = Minecraft.getInstance();
        MultiPlayerGameMode interactionManager = client.gameMode;
        if (clientPlayer != null && hand != null && interactionManager != null) {
            int refillSlot = hand == InteractionHand.MAIN_HAND ? clientPlayer.getInventory().getSelectedSlot() : 9;
            RefillManager.setRefillData(StatusType.AUTO_REFILL, refillSlot, clientPlayer.getItemInHand(hand).getItem(), 3);
            float prevPitch = clientPlayer.getXRot();
            clientPlayer.setXRot(90.0f);
            clientPlayer.swing(hand);
            interactionManager.useItem((Player)clientPlayer, hand);
            clientPlayer.setXRot(prevPitch);
            counterAfterChargeJump = 20;
            MaceAttackAssistanceClient.afterJump(clientPlayer, clientPlayer.getInventory().getSelectedSlot(), -1);
            DebugScreen.lastY = clientPlayer.getY();
            Debug.previous_y = -64.0;
        }
    }

    public static void afterJump(LocalPlayer clientPlayer, int selectedSlot, int wait) {
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
        flagStunSlam = false;
        shouldShieldBreak = false;
        jumpCooldown = 0;
        shouldPressJump = false;
        targetMob = null;
        useEntityEventRefillCounter = 0;
        requireChargeJump = false;
        counterAfterChargeJump = 0;
        worldCheckCounter = 0;
        tempExtremeFlag = false;
        flag_aimingElytra = false;
        flagElytraSpear = false;
    }

    public record useEntityEventRefillStatus(InteractionHand hand, int beforeCount) {
    }
}
