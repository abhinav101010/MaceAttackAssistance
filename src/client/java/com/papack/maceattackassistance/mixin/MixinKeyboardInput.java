/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.MultiPlayerGameMode
 *  net.minecraft.client.player.KeyboardInput
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.player.Input
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
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
import com.papack.maceattackassistance.client.MAAState;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.RefillManager;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.WallClimbing;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

    @Redirect(method={"tick"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/KeyMapping;isDown()Z"))
    private boolean inGameKeyPressed(KeyMapping instance) {
        if (MAAState.antiCheat) {
            return instance.isDown();
        }
        Minecraft client = Minecraft.getInstance();
        LocalPlayer clientPlayer = client.player;
        MultiPlayerGameMode interactionManager = client.gameMode;
        this.wallClimbingStatus = WallClimbing.resetClimbingStatus();
        if (client.gui.screen() == null && clientPlayer != null && interactionManager != null) {
            if (JobManager.checkStatus(StatusType.DOUBLE_TAP) || JobManager.checkStatus(StatusType.DOUBLE_TAP_ELYTRA_SPEAR)) {
                return false;
            }
            if (Config.JUMP_ASSIST && this.verifyPlayerCondition(client, clientPlayer)) {
                InteractionHand hand = Utils.getHandHoldingWindCharge(client, clientPlayer);
                if (hand == null && ElytraBoost.isElytraBoostIdle() && (clientPlayer.onGround() || this.wallClimbingStatus.canClimbing())) {
                    hand = Utils.findToSetWindCharge(clientPlayer);
                }
                if (hand != null) {
                    ItemStack windChargeItem = clientPlayer.getItemInHand(hand);
                    if (!clientPlayer.getCooldowns().isOnCooldown(windChargeItem)) {
                        float orgPitch = clientPlayer.getViewXRot(1.0f);
                        float orgYaw = clientPlayer.getViewYRot(1.0f);
                        if (this.wallClimbingStatus.canClimbing()) {
                            clientPlayer.setXRot(77.5f);
                            clientPlayer.setYRot(orgYaw + (float)(-45 + this.wallClimbingStatus.offset() * 45));
                            this.wallClimbingStatus = WallClimbing.resetClimbingStatus();
                            if (ElytraBoost.isElytraBoostIdle()) {
                                RefillManager.setRefillData(StatusType.AUTO_REFILL, Utils.getHandToSlot(clientPlayer, hand), windChargeItem.getItem(), 0);
                                clientPlayer.swing(hand);
                                interactionManager.useItem((Player)clientPlayer, hand);
                            }
                            clientPlayer.xRotO = 90.0f;
                            clientPlayer.setXRot(orgPitch);
                            clientPlayer.setYRot(orgYaw);
                            MaceAttackAssistanceClient.afterJump(clientPlayer, clientPlayer.getInventory().getSelectedSlot(), -1);
                        } else if (ElytraBoost.isElytraBoostIdle() && clientPlayer.onGround()) {
                            if (this.checkJumpMode(clientPlayer)) {
                                clientPlayer.setXRot(90.0f);
                                RefillManager.setRefillData(StatusType.AUTO_REFILL, Utils.getHandToSlot(clientPlayer, hand), windChargeItem.getItem(), 0);
                                clientPlayer.swing(hand);
                                interactionManager.useItem((Player)clientPlayer, hand);
                                clientPlayer.xRotO = 90.0f;
                                clientPlayer.setXRot(orgPitch);
                                clientPlayer.setYRot(orgYaw);
                                MaceAttackAssistanceClient.afterJump(clientPlayer, clientPlayer.getInventory().getSelectedSlot(), -1);
                            } else if (clientPlayer.isShiftKeyDown() || JumpController.ON_SLIME_BLOCK) {
                                MaceAttackAssistanceClient.sneakChargeJump(clientPlayer, hand);
                            } else {
                                MaceAttackAssistanceClient.requireChargeJump = clientPlayer.onGround();
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return instance.isDown();
    }

    @Inject(method={"tick"}, at={@At(value="TAIL")})
    private void jump(CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer clientPlayer = client.player;
        if (clientPlayer != null) {
            Input clientPlayerInput;
            if (JumpController.JUMP) {
                clientPlayerInput = clientPlayer.input.keyPresses;
                clientPlayer.input.keyPresses = new Input(clientPlayerInput.forward(), clientPlayerInput.backward(), clientPlayerInput.left(), clientPlayerInput.right(), true, clientPlayerInput.shift(), clientPlayerInput.sprint());
            }
            if (FlappingSuppression.jumpSuppressionCounter > 0) {
                clientPlayerInput = clientPlayer.input.keyPresses;
                clientPlayer.input.keyPresses = new Input(clientPlayerInput.forward(), clientPlayerInput.backward(), clientPlayerInput.left(), clientPlayerInput.right(), false, clientPlayerInput.shift(), clientPlayerInput.sprint());
            }
        }
    }

    @Unique
    private boolean verifyPlayerCondition(Minecraft client, LocalPlayer clientPlayer) {
        this.wallClimbingStatus = WallClimbing.canEasyWallClimbing(client, clientPlayer);
        if (!client.options.keySprint.isDown() && !client.options.keyShift.isDown()) {
            return false;
        }
        if (!client.options.keyJump.isDown()) {
            return false;
        }
        if (clientPlayer.isUnderWater()) {
            return false;
        }
        if (clientPlayer.isSwimming()) {
            return false;
        }
        if (clientPlayer.isFallFlying()) {
            return false;
        }
        JumpController.ON_SLIME_BLOCK = Utils.isOnSlimeBlock(clientPlayer);
        return !Config.JUMP_SPAM || clientPlayer.onGround() || JumpController.ON_SLIME_BLOCK || this.wallClimbingStatus.canClimbing();
    }

    @Unique
    private boolean checkJumpMode(LocalPlayer clientPlayer) {
        Config.JumpMode mode;
        Config.JumpMode jumpMode = mode = Utils.isUsingElytra(clientPlayer) ? Config.IN_USE_ELYTRA_JUMP_MODE : Config.NOT_IN_USE_ELYTRA_JUMP_MODE;
        if (mode == Config.JumpMode.Toggle) {
            return Config.TOGGLE_JUMP_MODE;
        }
        return mode == Config.JumpMode.Low;
    }
}
