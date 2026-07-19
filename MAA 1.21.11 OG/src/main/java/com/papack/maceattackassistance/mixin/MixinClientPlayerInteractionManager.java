/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.util.Hand
 *  net.minecraft.util.ActionResult
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.mob.HostileEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.entity.player.PlayerInventory
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.util.InputUtil
 *  net.minecraft.client.network.ClientPlayNetworkHandler
 *  net.minecraft.client.network.ClientPlayerInteractionManager
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.item.MaceItem
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

import com.papack.maceattackassistance.client.ElytraBoost;
import com.papack.maceattackassistance.client.HotSwap;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.MacroController;
import com.papack.maceattackassistance.client.RocketBlitz;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.StunSlam;
import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.MaceItem;
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
    @Final
    private ClientPlayNetworkHandler networkHandler;
    @Unique
    private static int beforeSlot = -1;

    @Shadow
    public abstract ActionResult interactItem(PlayerEntity var1, Hand var2);

    @Inject(method={"interactItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void rocketBlitz(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ClientPlayerEntity clientPlayer;
        if (!Utils.checkPlayerUUID((Entity)player)) {
            cir.cancel();
        }
            if ((clientPlayer = (ClientPlayerEntity)player) != null && MinecraftClient.getInstance().world.isClient() && !clientPlayer.isSpectator() && !clientPlayer.isCreative()) {
            if (Config.ROCKET_BLITZ && ElytraBoost.isElytraBoostIdle() && JobManager.checkOrderIsEmpty()) {
                PlayerInventory playerInventory = clientPlayer.getInventory();
                ItemStack offHandStack = clientPlayer.getOffHandStack();
                long windowHandle = this.client.getWindow().getHandle();
                boolean isTriggerPressed = InputUtil.isKeyPressed(this.client.getWindow(), Config.ROCKET_TRIGGER.getGlfwKey());
                int rocketSlot = RocketBlitz.getRocketSlotId(clientPlayer);
                int process = 0;
                if (Utils.isUsingElytra(clientPlayer) && !clientPlayer.isOnGround() && isTriggerPressed) {
                    if (rocketSlot > -1) {
                        process = 1;
                    } else if (offHandStack.isOf(Items.FIREWORK_ROCKET) && (Config.PRIORITIZE_ROCKET || ElytraBoost.isNotUsableItems(this.client, clientPlayer.getMainHandStack()))) {
                        process = 2;
                    }
                }
                switch (process) {
                    case 1: {
                        int currentSlot = clientPlayer.getInventory().getSelectedSlot();
                        if (rocketSlot != currentSlot) {
                            playerInventory.setSelectedSlot(rocketSlot);
                        }
                        if (hand == Hand.OFF_HAND) {
                            beforeSlot = currentSlot;
                            this.interactItem(player, Hand.MAIN_HAND);
                            cir.setReturnValue(ActionResult.FAIL);
                        }
                        if (JobManager.checkOrderIsEmpty()) {
                            JobManager.setOrder(StatusType.ROCKET, beforeSlot > -1 ? beforeSlot : currentSlot);
                            beforeSlot = -1;
                        }
                        MixinClientPlayerInteractionManager.setAutoRefill(this.client, clientPlayer, rocketSlot);
                        break;
                    }
                    case 2: {
                        if (JobManager.checkOrderIsEmpty()) {
                            JobManager.setOrder(StatusType.ROCKET, 9);
                        }
                        if (hand == Hand.MAIN_HAND) {
                            cir.setReturnValue(ActionResult.FAIL);
                        }
                        MixinClientPlayerInteractionManager.setAutoRefill(this.client, clientPlayer, 9);
                        break;
                    }
                    default: {
                        MixinClientPlayerInteractionManager.setAutoRefill(this.client, clientPlayer, hand == Hand.MAIN_HAND ? clientPlayer.getInventory().getSelectedSlot() : 9);
                        break;
                    }
                }
            } else {
                MixinClientPlayerInteractionManager.setAutoRefill(this.client, clientPlayer, hand == Hand.MAIN_HAND ? clientPlayer.getInventory().getSelectedSlot() : 9);
            }
            ElytraBoost.flag_elytra_boost = false;
        }
    }

    @Unique
    private static void setAutoRefill(MinecraftClient client, ClientPlayerEntity clientPlayer, int slot) {
        if (Config.AUTO_REFILL) {
            TickScheduler.schedule(3, () -> TickScheduler.scheduleWhen(() -> JobManager.checkOrderIsEmpty() && JobManager.keyReleased(client), () -> {
                MaceAttackAssistanceClient.nonEventRefills = slot;
                MaceAttackAssistanceClient.refillInAdvance(clientPlayer);
            }));
        }
    }

    @Inject(method={"attackEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void extremeAttackMode(PlayerEntity clientPlayer, Entity target, CallbackInfo ci) {
        try {
            boolean chkUuid = Utils.checkPlayerUUID((Entity)clientPlayer);
            if (Config.EXTREME && target instanceof LivingEntity) {
                if (MaceAttackAssistanceClient.should_attack_interval || target.getId() <= 0 || !target.isAlive() || !chkUuid) {
                    ci.cancel();
                } else {
                    boolean flagSwing = false;
                    PlayerInventory playerInventory = clientPlayer.getInventory();
                    boolean isOverThreshold = this.canAttackAssist((ClientPlayerEntity)clientPlayer);
                    boolean onGroundOrJumpAxe = false;
                    int maceSlot = -1;
                    if (!MaceAttackAssistanceClient.shouldShieldBreak) {
                        StunSlam.preSelectAxe(this.client, (ClientPlayerEntity)clientPlayer, target);
                        onGroundOrJumpAxe = MaceAttackAssistanceClient.shouldShieldBreak;
                    }
                    if (MaceAttackAssistanceClient.shouldShieldBreak) {
                        clientPlayer.swingHand(Hand.MAIN_HAND);
                        this.networkHandler.sendPacket((Packet)PlayerInteractEntityC2SPacket.attack((Entity)target, (boolean)clientPlayer.isSneaking()));
                        MaceAttackAssistanceClient.shouldShieldBreak = false;
                    }
                    if (Config.HOT_SWAP) {
                        if (Config.SNAPBACK && MacroController.canSnapback((ClientPlayerEntity)clientPlayer, (LivingEntity)target)) {
                            maceSlot = HotSwap.getBreachMaceSlotId((ClientPlayerEntity)clientPlayer);
                        } else if (isOverThreshold) {
                            maceSlot = HotSwap.getPrimaryMaceSlotId((ClientPlayerEntity)clientPlayer);
                        } else if (!(onGroundOrJumpAxe || !Config.BREACH_SWAP || clientPlayer.isOnGround() && !Config.BREACH_ON_GROUND || Config.BREACH_LIMITED && !(target instanceof HostileEntity) && !(target instanceof PlayerEntity))) {
                            maceSlot = HotSwap.getBreachMaceSlotId((ClientPlayerEntity)clientPlayer);
                        }
                    }
                    if (maceSlot > -1) {
                        ItemStack hotbarStack1 = playerInventory.getStack(maceSlot);
                        if (hotbarStack1.getItem() instanceof MaceItem) {
                            if (MaceAttackAssistanceClient.ex_previous_slot == -1) {
                                MaceAttackAssistanceClient.ex_previous_slot = MaceAttackAssistanceClient.ex_preStun_slot > -1 ? MaceAttackAssistanceClient.ex_preStun_slot : playerInventory.getSelectedSlot();
                            }
                            playerInventory.setSelectedSlot(maceSlot);
                            MaceAttackAssistanceClient.should_attack_interval = true;
                            MaceAttackAssistanceClient.waiting_tick_counter = 10;
                        }
                    } else if (MaceAttackAssistanceClient.ex_previous_slot == -1 && MaceAttackAssistanceClient.ex_preStun_slot > -1) {
                        MaceAttackAssistanceClient.ex_previous_slot = MaceAttackAssistanceClient.ex_preStun_slot;
                    }
                    if (target.getId() <= 0 || !target.isAlive()) {
                        ci.cancel();
                    }
                    if (!flagSwing) {
                        clientPlayer.handSwingTicks = -1;
                        clientPlayer.handSwinging = true;
                        clientPlayer.preferredHand = Hand.MAIN_HAND;
                    }
                }
            }
        }
        catch (Exception e) {
            MaceAttackAssistanceClient.LOGGER.error("extremeAttackMode:", (Throwable)e);
            ci.cancel();
        }
    }

    @Unique
    private boolean canAttackAssist(@NotNull ClientPlayerEntity clientPlayer) {
        return clientPlayer.getVelocity().getY() <= -0.447;
    }
}
