/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.PlayerInput
 *  net.minecraft.Hand
 *  net.minecraft.PlayerEntity
 *  net.minecraft.ItemStack
 *  net.minecraft.KeyBinding
 *  net.minecraft.MinecraftClient
 *  net.minecraft.ClientPlayerInteractionManager
 *  net.minecraft.KeyboardInput
 *  net.minecraft.ClientPlayerEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.papack.maceattackassistance.mixin;

import com.papack.maceattackassistance.client.ElytraBoost;
import com.papack.maceattackassistance.client.FlappingSuppression;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.JumpController;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.RefillManager;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.WallClimbing;
import com.papack.maceattackassistance.client.ZoomState;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.Hand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value=EnvType.CLIENT)
@Mixin(value={KeyboardInput.class})
public class MixinKeyboardInput {
    @Unique
    WallClimbing.ClimbingStatus wallClimbingStatus;

    @Redirect(method={"tick"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/option/KeyBinding;isPressed()Z"))
    private boolean inGameKeyPressed(KeyBinding instance) {
        if (ZoomState.MAAClientState.antiCheat) {
            return instance.isPressed();
        }
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayer = client.player;
        ClientPlayerInteractionManager interactionManager = client.interactionManager;
        this.wallClimbingStatus = WallClimbing.resetClimbingStatus();
        if (client.currentScreen == null && clientPlayer != null && interactionManager != null) {
            if (JobManager.checkStatus(StatusType.DOUBLE_TAP)) {
                return false;
            }
            if (Config.JUMP_ASSIST && this.verifyPlayerCondition(client, clientPlayer)) {
                Hand hand = Utils.getHandHoldingWindCharge(client, clientPlayer);
                if (hand == null && ElytraBoost.isElytraBoostIdle()) {
                    hand = Utils.findToSetWindCharge(clientPlayer);
                }
                if (hand != null) {
                    ItemStack windChargeItem = clientPlayer.getStackInHand(hand);
                    if (!clientPlayer.getItemCooldownManager().isCoolingDown(windChargeItem)) {
                        float orgPitch = clientPlayer.getPitch(1.0f);
                        float orgYaw = clientPlayer.getYaw(1.0f);
                        if (this.wallClimbingStatus.canClimbing()) {
                            clientPlayer.setPitch(77.5f);
                            clientPlayer.setYaw(orgYaw + (float)(-45 + this.wallClimbingStatus.offset() * 45));
                            this.wallClimbingStatus = WallClimbing.resetClimbingStatus();
                            if (ElytraBoost.isElytraBoostIdle()) {
                                RefillManager.setRefillData(StatusType.AUTO_REFILL, Utils.getHandToSlot(clientPlayer, hand), windChargeItem.getItem(), 0);
                                clientPlayer.swingHand(hand);
                                interactionManager.interactItem((PlayerEntity)clientPlayer, hand);
                            }
                            clientPlayer.lastPitch = 90.0f;
                            clientPlayer.setPitch(orgPitch);
                            clientPlayer.setYaw(orgYaw);
                            MaceAttackAssistanceClient.afterJump(clientPlayer, clientPlayer.getInventory().getSelectedSlot(), -1);
                        } else if (ElytraBoost.isElytraBoostIdle() && clientPlayer.isOnGround()) {
                            if (this.checkJumpMode(clientPlayer)) {
                                clientPlayer.setPitch(90.0f);
                                RefillManager.setRefillData(StatusType.AUTO_REFILL, Utils.getHandToSlot(clientPlayer, hand), windChargeItem.getItem(), 0);
                                clientPlayer.swingHand(hand);
                                interactionManager.interactItem((PlayerEntity)clientPlayer, hand);
                                clientPlayer.lastPitch = 90.0f;
                                clientPlayer.setPitch(orgPitch);
                                clientPlayer.setYaw(orgYaw);
                                MaceAttackAssistanceClient.afterJump(clientPlayer, clientPlayer.getInventory().getSelectedSlot(), -1);
                            } else if (clientPlayer.isSneaking() || JumpController.ON_SLIME_BLOCK) {
                                MaceAttackAssistanceClient.sneakChargeJump(clientPlayer, hand);
                            } else {
                                MaceAttackAssistanceClient.requireChargeJump = true;
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return instance.isPressed();
    }

    @Inject(method={"tick"}, at={@At(value="TAIL")})
    private void jump(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayer = client.player;
        if (clientPlayer != null) {
            PlayerInput clientPlayerInput;
            if (JumpController.JUMP) {
                clientPlayerInput = clientPlayer.input.playerInput;
                clientPlayer.input.playerInput = new PlayerInput(clientPlayerInput.forward(), clientPlayerInput.backward(), clientPlayerInput.left(), clientPlayerInput.right(), true, clientPlayerInput.sneak(), clientPlayerInput.sprint());
            }
            if (FlappingSuppression.jumpSuppressionCounter > 0) {
                clientPlayerInput = clientPlayer.input.playerInput;
                clientPlayer.input.playerInput = new PlayerInput(clientPlayerInput.forward(), clientPlayerInput.backward(), clientPlayerInput.left(), clientPlayerInput.right(), false, clientPlayerInput.sneak(), clientPlayerInput.sprint());
            }
        }
    }

    @Unique
    private boolean verifyPlayerCondition(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        this.wallClimbingStatus = WallClimbing.canEasyWallClimbing(client, clientPlayer);
        if (!client.options.sprintKey.isPressed() && !client.options.sneakKey.isPressed()) {
            return false;
        }
        if (!client.options.jumpKey.isPressed()) {
            return false;
        }
        if (clientPlayer.isSubmergedInWater()) {
            return false;
        }
        if (clientPlayer.isSwimming()) {
            return false;
        }
        if (clientPlayer.isGliding()) {
            return false;
        }
        JumpController.ON_SLIME_BLOCK = Utils.isOnSlimeBlock(clientPlayer);
        return !Config.JUMP_SPAM || clientPlayer.isOnGround() || JumpController.ON_SLIME_BLOCK || this.wallClimbingStatus.canClimbing();
    }

    @Unique
    private boolean checkJumpMode(ClientPlayerEntity clientPlayer) {
        Config.JumpMode mode;
        Config.JumpMode jumpMode = mode = Utils.isUsingElytra(clientPlayer) ? Config.IN_USE_ELYTRA_JUMP_MODE : Config.NOT_IN_USE_ELYTRA_JUMP_MODE;
        if (mode == Config.JumpMode.Toggle) {
            return Config.TOGGLE_JUMP_MODE;
        }
        return mode == Config.JumpMode.Low;
    }
}
