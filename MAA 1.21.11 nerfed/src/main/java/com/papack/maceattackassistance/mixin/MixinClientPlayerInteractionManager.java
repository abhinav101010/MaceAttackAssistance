/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.Window
 *  net.minecraft.Hand
 *  net.minecraft.ActionResult
 *  net.minecraft.Entity
 *  net.minecraft.LivingEntity
 *  net.minecraft.HostileEntity
 *  net.minecraft.PlayerEntity
 *  net.minecraft.PlayerInventory
 *  net.minecraft.Item
 *  net.minecraft.ItemStack
 *  net.minecraft.Items
 *  net.minecraft.MinecraftClient
 *  net.minecraft.ItemTags
 *  net.minecraft.InputUtil
 *  net.minecraft.ClientPlayerInteractionManager
 *  net.minecraft.TagKey
 *  net.minecraft.ClientPlayerEntity
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

import com.papack.maceattackassistance.client.ApproachSupportLine;
import com.papack.maceattackassistance.client.AutoElytraSwap;
import com.papack.maceattackassistance.client.ElytraBoost;
import com.papack.maceattackassistance.client.FriendManager;
import com.papack.maceattackassistance.client.HotSwap;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.MacroController;
import com.papack.maceattackassistance.client.PrevSlotManager;
import com.papack.maceattackassistance.client.RefillManager;
import com.papack.maceattackassistance.client.RocketBlitz;
import com.papack.maceattackassistance.client.SpearAttacks;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.StunSlam;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.ZoomState;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.Window;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.client.network.ClientPlayerEntity;
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
@Mixin(value={ClientPlayerInteractionManager.class})
public abstract class MixinClientPlayerInteractionManager {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    public abstract ActionResult interactItem(PlayerEntity var1, Hand var2);

    @Inject(method={"interactItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void rocketBlitz(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        block17: {
            block19: {
                ClientPlayerEntity clientPlayer;
                block18: {
                    PlayerEntity en;
                    if (ZoomState.MAAClientState.antiCheat) {
                        return;
                    }
                    if (Config.DEBUG_SCREEN && (en = ApproachSupportLine.findNearestPlayer(this.client, (ClientPlayerEntity)player, 20, 20)) != null) {
                        MaceAttackAssistanceClient.LOGGER.info("[Use] {} : r {} : d {} : s {} : xz {} : p {}", (Object)player.getStackInHand(hand).getItem().getTranslationKey(), (Object)String.format("%.3f", (double)player.distanceTo((Entity)en) / player.getVelocity().length()), (Object)String.format("%.3f", Float.valueOf(player.distanceTo((Entity)en))), (Object)String.format("%.3f", player.getVelocity().length()), (Object)String.format("%.3f", player.getVelocity().horizontalLength()), (Object)String.format("%.3f", Float.valueOf(player.getPitch())));
                    }
                    if (AutoElytraSwap.getFlag()) {
                        AutoElytraSwap.setFlag(false);
                        return;
                    }
                    if (!Utils.checkPlayerUUID((Entity)player)) {
                        cir.cancel();
                    }
                    if (!(clientPlayer = (ClientPlayerEntity)player).getEntityWorld().isClient() || clientPlayer.isSpectator() || clientPlayer.isCreative()) break block17;
                    if (!Config.ROCKET_BLITZ || !ElytraBoost.isElytraBoostIdle() || !JobManager.checkOrderIsEmpty() || MaceAttackAssistanceClient.requireChargeJump) break block18;
                    PlayerInventory playerInventory = clientPlayer.getInventory();
                    ItemStack offHandStack = clientPlayer.getOffHandStack();
                    Window windowHandle = this.client.getWindow();
                    boolean isTriggerPressed = InputUtil.isKeyPressed((Window)windowHandle, (int)Config.ROCKET_TRIGGER.getGlfwKey());
                    int rocketSlot = RocketBlitz.getRocketSlotId(clientPlayer);
                    int process = 0;
                    if (Utils.isUsingElytra(clientPlayer) && !clientPlayer.isOnGround() && isTriggerPressed) {
                        if (rocketSlot > -1) {
                            process = 1;
                        } else if (offHandStack.isOf(Items.FIREWORK_ROCKET) && (Config.PRIORITIZE_ROCKET || ElytraBoost.isNotUsableItems(this.client, clientPlayer.getMainHandStack()))) {
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
                            }
                            if (hand == Hand.OFF_HAND) {
                                beforeSlot = currentSlot;
                                this.interactItem(player, Hand.MAIN_HAND);
                                cir.setReturnValue(ActionResult.FAIL);
                            }
                            if (clientPlayer.getStackInHand(hand).isOf(Items.FIREWORK_ROCKET)) {
                                JobManager.setOrder(StatusType.ROCKET, beforeSlot > -1 ? beforeSlot : currentSlot);
                                MixinClientPlayerInteractionManager.setAutoRefill(clientPlayer, rocketSlot, hand);
                                break;
                            }
                            break block19;
                        }
                        case 2: {
                            if (clientPlayer.getStackInHand(hand).isOf(Items.FIREWORK_ROCKET)) {
                                JobManager.setOrder(StatusType.ROCKET, 9);
                                MixinClientPlayerInteractionManager.setAutoRefill(clientPlayer, 9, hand);
                            }
                            if (hand == Hand.MAIN_HAND) {
                                cir.setReturnValue(ActionResult.FAIL);
                                break;
                            }
                            break block19;
                        }
                        default: {
                            MixinClientPlayerInteractionManager.setAutoRefill(clientPlayer, hand == Hand.MAIN_HAND ? clientPlayer.getInventory().getSelectedSlot() : 9, hand);
                        }
                    }
                    break block19;
                }
                MixinClientPlayerInteractionManager.setAutoRefill(clientPlayer, hand == Hand.MAIN_HAND ? clientPlayer.getInventory().getSelectedSlot() : 9, hand);
            }
            ElytraBoost.flag_elytra_boost = false;
        }
    }

    @Unique
    private static void setAutoRefill(ClientPlayerEntity clientPlayer, int slot, Hand hand) {
        ItemStack itemStack;
        if (Config.AUTO_REFILL && (itemStack = clientPlayer.getStackInHand(hand)).isStackable()) {
            RefillManager.setRefillData(StatusType.AUTO_REFILL, slot, itemStack.getItem(), 2);
        }
    }

    @Inject(method={"attackEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void extremeAttackMode(PlayerEntity clientPlayer, Entity target, CallbackInfo ci) {
        if (ZoomState.MAAClientState.antiCheat) {
            return;
        }
        if (ZoomState.KeyManager.keyManager()) {
            return;
        }
        if (JobManager.jumpOption() && target instanceof PlayerEntity) {
            return;
        }
        if (Config.SPEAR_SLAM && SpearAttacks.SPEAR_SLAM_ACTIVE) {
            return;
        }
        StunSlam.flagPreAxeCalled = false;
        boolean chkUuid = Utils.checkPlayerUUID((Entity)clientPlayer);
        boolean t = target == null || !target.isAlive() || Config.FRIEND_PROTECTION && FriendManager.isFriend(target.getUuid());
        boolean f0 = t || !chkUuid;
        int d = clientPlayer.age;
        boolean f1 = StunSlam.CHK_PLAYER_AGE == d;
        boolean flag0 = f0 || f1;
        StunSlam.CHK_PLAYER_AGE = d;
        if (!flag0 && !MaceAttackAssistanceClient.should_attack_interval) {
            if (!Config.EXTREME) {
                return;
            }
            if (target instanceof LivingEntity && !JobManager.checkAttackStatus() && !Utils.canDoubleTap() && !MaceAttackAssistanceClient.getTempExtremeFlag()) {
                PlayerInventory playerInventory = clientPlayer.getInventory();
                boolean isOverThreshold = this.canAttackAssist((ClientPlayerEntity)clientPlayer);
                boolean onGroundOrJumpAxe = false;
                int maceSlot = -1;
                if (!MaceAttackAssistanceClient.shouldShieldBreak) {
                    onGroundOrJumpAxe = StunSlam.preSelectAxe(this.client, (ClientPlayerEntity)clientPlayer, target);
                }
                if (MaceAttackAssistanceClient.shouldShieldBreak) {
                    MaceAttackAssistanceClient.shouldShieldBreak = false;
                    MaceAttackAssistanceClient.flagPreAxe = false;
                }
                boolean flagBreach = false;
                boolean isWearing = Utils.isWearingArmor(target);
                if (Config.HOT_SWAP) {
                    if (Config.SNAPBACK && MacroController.canSnapback((ClientPlayerEntity)clientPlayer, (LivingEntity)target)) {
                        maceSlot = HotSwap.getBreachMaceSlotId((ClientPlayerEntity)clientPlayer);
                    } else if (isOverThreshold) {
                        maceSlot = HotSwap.getPrimaryMaceSlotId((ClientPlayerEntity)clientPlayer);
                    } else if (!(onGroundOrJumpAxe || !Config.BREACH_SWAP || clientPlayer.isOnGround() && !Config.BREACH_ON_GROUND || Config.BREACH_LIMITED && !(target instanceof HostileEntity) && !(target instanceof PlayerEntity))) {
                        int mode = isWearing ? this.getBehavior(Config.SWORD_SWAP_OR_BREACH_SWAP) : this.getBehavior(Config.BEHAVIOR_NOT_WEARING_ARMOR);
                        maceSlot = switch (mode) {
                            case 1 -> Utils.findItemInHotbarByTags((TagKey<Item>)(Config.SWORD_OR_AXE ? ItemTags.SWORDS : ItemTags.AXES));
                            case 2 -> HotSwap.getBreachMaceSlotId((ClientPlayerEntity)clientPlayer);
                            default -> -1;
                        };
                        flagBreach = true;
                    }
                }
                if (maceSlot > -1) {
                    PrevSlotManager.setPrevSlot(flagBreach ? StatusType.BREACH : StatusType.NONE, playerInventory.getSelectedSlot(), 0);
                    playerInventory.setSelectedSlot(maceSlot);
                    MaceAttackAssistanceClient.setAttackInterval();
                }
            }
            return;
        }
        ci.cancel();
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
    private boolean canAttackAssist(@NotNull ClientPlayerEntity clientPlayer) {
        return clientPlayer.getVelocity().getY() <= -0.447;
    }

    @Inject(method={"attackEntity"}, at={@At(value="TAIL")})
    private void attackLog(PlayerEntity player, Entity target, CallbackInfo ci) {
        StunSlam.lastSlot = player.getInventory().getSelectedSlot();
        if (Config.DEBUG_SCREEN) {
            MaceAttackAssistanceClient.LOGGER.info("[Attack] ex: {}, d: {}, age: {} , w: {} , v: {}", (Object)Config.EXTREME, (Object)Float.valueOf(player.distanceTo(target)), (Object)player.age, (Object)player.getMainHandStack().getItem().getTranslationKey(), (Object)JobManager.checkValue(StatusType.STUN_SLAM));
        }
    }
}
