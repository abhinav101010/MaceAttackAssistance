/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants
 *  com.mojang.blaze3d.platform.Window
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientPacketListener
 *  net.minecraft.client.multiplayer.MultiPlayerGameMode
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ServerboundAttackPacket
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  org.jetbrains.annotations.NotNull
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.papack.maceattackassistance.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.papack.maceattackassistance.client.ApproachSupportLine;
import com.papack.maceattackassistance.client.AutoElytraSwap;
import com.papack.maceattackassistance.client.ElytraBoost;
import com.papack.maceattackassistance.client.FriendManager;
import com.papack.maceattackassistance.client.HotSwap;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.MAAState;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.MacroController;
import com.papack.maceattackassistance.client.PrevSlotManager;
import com.papack.maceattackassistance.client.RefillManager;
import com.papack.maceattackassistance.client.RocketBlitz;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.StunSlam;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundAttackPacket;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(value=EnvType.CLIENT)
@Mixin(value={MultiPlayerGameMode.class})
public abstract class MixinMultiPlayerGameMode {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    @Final
    private ClientPacketListener connection;

    @Shadow
    public abstract InteractionResult useItem(Player var1, InteractionHand var2);

    @Inject(method={"useItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void rocketBlitz(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        block17: {
            block19: {
                LocalPlayer clientPlayer;
                block18: {
                    Player en;
                    if (MAAState.antiCheat) {
                        return;
                    }
                    if (Config.DEBUG_SCREEN && (en = ApproachSupportLine.findNearestPlayer(this.minecraft, (LocalPlayer)player, 20, 20)) != null) {
                        MaceAttackAssistanceClient.LOGGER.info("[Use] {} : r {} : d {} : s {} : xz {} : p {} : t {}", (Object)player.getItemInHand(hand).getItem().getDescriptionId(), (Object)String.format("%.3f", (double)player.distanceTo((Entity)en) / player.getDeltaMovement().length()), (Object)String.format("%.3f", Float.valueOf(player.distanceTo((Entity)en))), (Object)String.format("%.3f", player.getDeltaMovement().length()), (Object)String.format("%.3f", player.getDeltaMovement().horizontalDistance()), (Object)String.format("%.3f", Float.valueOf(player.getXRot())), (Object)player.tickCount);
                    }
                    if (AutoElytraSwap.getFlag()) {
                        AutoElytraSwap.setFlag(false);
                        return;
                    }
                    if (!Utils.checkPlayerUUID((Entity)player)) {
                        cir.cancel();
                    }
                    if (!(clientPlayer = (LocalPlayer)player).level().isClientSide() || clientPlayer.isSpectator() || clientPlayer.isCreative()) break block17;
                    if (!Config.ROCKET_BLITZ || !ElytraBoost.isElytraBoostIdle() || !JobManager.checkOrderIsEmpty() || MaceAttackAssistanceClient.requireChargeJump) break block18;
                    Window windowHandle = this.minecraft.getWindow();
                    Inventory playerInventory = clientPlayer.getInventory();
                    ItemStack offHandStack = clientPlayer.getOffhandItem();
                    int keys = Utils.isSpear(clientPlayer) ? Config.ROCKET_TRIGGER_SPEAR.getGlfwKey() : Config.ROCKET_TRIGGER.getGlfwKey();
                    boolean isTriggerPressed = keys > -1 && InputConstants.isKeyDown((Window)windowHandle, (int)keys);
                    int rocketSlot = RocketBlitz.getRocketSlotId(clientPlayer);
                    int process = 0;
                    if (Utils.isUsingElytra(clientPlayer) && !clientPlayer.onGround() && isTriggerPressed) {
                        if (rocketSlot > -1) {
                            process = 1;
                        } else if (offHandStack.is(Items.FIREWORK_ROCKET) && (Config.PRIORITIZE_ROCKET || ElytraBoost.isNotUsableItems(this.minecraft, clientPlayer.getMainHandItem()))) {
                            process = 2;
                        }
                    }
                    int beforeSlot = -1;
                    switch (process) {
                        case 1: {
                            int currentSlot;
                            int slot = PrevSlotManager.getLastOrderSlot();
                            int n = currentSlot = slot > -1 ? slot : clientPlayer.getInventory().getSelectedSlot();
                            if (rocketSlot != currentSlot) {
                                playerInventory.setSelectedSlot(rocketSlot);
                                ((MultiPlayerGameModeInvoker)this.minecraft.gameMode).syncSelectedSlotInvoker();
                            }
                            if (hand == InteractionHand.OFF_HAND) {
                                beforeSlot = currentSlot;
                                this.useItem(player, InteractionHand.MAIN_HAND);
                                cir.setReturnValue(InteractionResult.FAIL);
                            }
                            if (clientPlayer.getItemInHand(hand).is(Items.FIREWORK_ROCKET)) {
                                JobManager.setOrder(StatusType.ROCKET, beforeSlot > -1 ? beforeSlot : currentSlot);
                                MixinMultiPlayerGameMode.setAutoRefill(clientPlayer, rocketSlot, hand);
                                break;
                            }
                            break block19;
                        }
                        case 2: {
                            if (clientPlayer.getItemInHand(hand).is(Items.FIREWORK_ROCKET)) {
                                JobManager.setOrder(StatusType.ROCKET, 9);
                                MixinMultiPlayerGameMode.setAutoRefill(clientPlayer, 9, hand);
                            }
                            if (hand == InteractionHand.MAIN_HAND) {
                                cir.setReturnValue(InteractionResult.FAIL);
                                break;
                            }
                            break block19;
                        }
                        default: {
                            MixinMultiPlayerGameMode.setAutoRefill(clientPlayer, hand == InteractionHand.MAIN_HAND ? clientPlayer.getInventory().getSelectedSlot() : 9, hand);
                        }
                    }
                    break block19;
                }
                MixinMultiPlayerGameMode.setAutoRefill(clientPlayer, hand == InteractionHand.MAIN_HAND ? clientPlayer.getInventory().getSelectedSlot() : 9, hand);
            }
            ElytraBoost.flag_elytra_boost = false;
        }
    }

    @Unique
    private static void setAutoRefill(LocalPlayer clientPlayer, int slot, InteractionHand hand) {
        ItemStack itemStack;
        if (Config.AUTO_REFILL && (itemStack = clientPlayer.getItemInHand(hand)).isStackable()) {
            RefillManager.setRefillData(StatusType.AUTO_REFILL, slot, itemStack.getItem(), 2);
        }
    }

    @Inject(method={"attack"}, at={@At(value="HEAD")}, cancellable=true)
    private void extremeAttackMode(Player player, Entity entity, CallbackInfo ci) {
        if (Config.DT_AERIAL_DIVE_MODE && (JobManager.checkStatus(StatusType.DOUBLE_TAP) && JobManager.checkValue(StatusType.DOUBLE_TAP) == 5 || JobManager.checkStatus(StatusType.DOUBLE_TAP_OFF_HAND) && JobManager.checkValue(StatusType.DOUBLE_TAP_OFF_HAND) == 5)) {
            ci.cancel();
        }
        if (MaceAttackAssistanceClient.flagStunSlam) {
            player.swing(InteractionHand.MAIN_HAND);
            this.connection.send((Packet)new ServerboundAttackPacket(entity.getId()));
            MaceAttackAssistanceClient.flagStunSlam = false;
            int maceSlot = HotSwap.getPrimaryMaceSlotId((LocalPlayer)player);
            if (maceSlot > -1) {
                player.getInventory().setSelectedSlot(maceSlot);
                ((MultiPlayerGameModeInvoker)this.minecraft.gameMode).syncSelectedSlotInvoker();
            }
            ci.cancel();
        } else {
            if (MAAState.antiCheat) {
                return;
            }
            if (MAAState.z()) {
                return;
            }
            if (MAAState.x() && entity instanceof Player) {
                return;
            }
            StunSlam.flagPreAxeCalled = false;
            boolean chkUuid = Utils.checkPlayerUUID((Entity)player);
            boolean t = entity == null || !entity.isAlive() || Config.FRIEND_PROTECTION && FriendManager.isFriend(entity.getUUID());
            boolean f0 = t || !chkUuid;
            int tickCount = player.tickCount;
            boolean f1 = StunSlam.CHK_PLAYER_AGE == tickCount;
            boolean flag0 = f0 || f1;
            StunSlam.CHK_PLAYER_AGE = tickCount;
            if (!flag0 && !MaceAttackAssistanceClient.should_attack_interval) {
                if (!Config.EXTREME) {
                    return;
                }
                if (JobManager.checkStatus(StatusType.MANUAL_STUN)) {
                    return;
                }
                if (entity instanceof LivingEntity && !JobManager.checkAttackStatus() && !Utils.canDoubleTap() && !MaceAttackAssistanceClient.getTempExtremeFlag()) {
                    Inventory playerInventory = player.getInventory();
                    boolean isOverThreshold = this.canAttackAssist((LocalPlayer)player);
                    boolean onGroundOrJumpAxe = false;
                    int maceSlot = -1;
                    if (!MaceAttackAssistanceClient.shouldShieldBreak) {
                        onGroundOrJumpAxe = StunSlam.preSelectAxe(this.minecraft, (LocalPlayer)player, entity);
                    }
                    if (MaceAttackAssistanceClient.shouldShieldBreak) {
                        MaceAttackAssistanceClient.shouldShieldBreak = false;
                        MaceAttackAssistanceClient.flagPreAxe = false;
                    }
                    boolean flagBreach = false;
                    boolean isWearing = Utils.isWearingArmor(entity);
                    if (Config.HOT_SWAP) {
                        if (Config.SNAPBACK && MacroController.canSnapback((LocalPlayer)player, (LivingEntity)entity)) {
                            maceSlot = HotSwap.getBreachMaceSlotId((LocalPlayer)player);
                        } else if (isOverThreshold) {
                            maceSlot = HotSwap.getPrimaryMaceSlotId((LocalPlayer)player);
                        } else if (!(onGroundOrJumpAxe || !Config.BREACH_SWAP || player.onGround() && !Config.BREACH_ON_GROUND || Config.BREACH_LIMITED && !(entity instanceof Monster) && !(entity instanceof Player))) {
                            int mode = isWearing ? this.getBehavior(Config.SWORD_SWAP_OR_BREACH_SWAP) : this.getBehavior(Config.BEHAVIOR_NOT_WEARING_ARMOR);
                            maceSlot = switch (mode) {
                                case 1 -> Utils.findItemInHotbarByTags((TagKey<Item>)(Config.SWORD_OR_AXE ? ItemTags.SWORDS : ItemTags.AXES));
                                case 2 -> HotSwap.getBreachMaceSlotId((LocalPlayer)player);
                                default -> -1;
                            };
                            flagBreach = true;
                        }
                    }
                    if (maceSlot > -1) {
                        PrevSlotManager.setPrevSlot(flagBreach ? StatusType.BREACH : StatusType.NONE, playerInventory.getSelectedSlot(), 0);
                        playerInventory.setSelectedSlot(maceSlot);
                        ((MultiPlayerGameModeInvoker)this.minecraft.gameMode).syncSelectedSlotInvoker();
                        MaceAttackAssistanceClient.setAttackInterval();
                    }
                }
                return;
            }
            ci.cancel();
        }
    }

    @Unique
    private int getBehavior(Config.Behavior behaviour) {
        return switch (behaviour) {
            default -> throw new MatchException(null, null);
            case Config.Behavior.Off -> 0;
            case Config.Behavior.SwordSwap -> 1;
            case Config.Behavior.BreachSwap -> 2;
        };
    }

    @Unique
    private boolean canAttackAssist(@NotNull LocalPlayer clientPlayer) {
        return clientPlayer.getDeltaMovement().y() <= -0.447;
    }

    @Inject(method={"attack"}, at={@At(value="TAIL")})
    private void attackLog(Player player, Entity entity, CallbackInfo ci) {
        StunSlam.lastSlot = player.getInventory().getSelectedSlot();
        if (Config.DEBUG_SCREEN) {
            MaceAttackAssistanceClient.LOGGER.info("[Attack] ex: {}, d: {}, age: {} , w: {} , v: {}", (Object)Config.EXTREME, (Object)Float.valueOf(player.distanceTo(entity)), (Object)player.tickCount, (Object)player.getMainHandItem().getItem().getDescriptionId(), (Object)JobManager.checkValue(StatusType.STUN_SLAM));
        }
    }
}
