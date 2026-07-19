/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.util.Hand
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.Item
 *  net.minecraft.client.option.KeyBinding
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.network.ClientPlayerInteractionManager
 *  net.minecraft.client.input.KeyboardInput
 *  net.minecraft.client.network.ClientPlayerEntity
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
import com.papack.maceattackassistance.client.JumpController;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.WallClimbing;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Hand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.PlayerInput;
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

    @Redirect(method={"tick()V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/option/KeyBinding;isPressed()Z"))
    private boolean jumpKeyIsPressed(KeyBinding instance) {
        Hand hand;
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayer = client.player;
        ClientPlayerInteractionManager interactionManager = client.interactionManager;
        this.wallClimbingStatus = WallClimbing.resetClimbingStatus();
        if (client.currentScreen == null && clientPlayer != null && interactionManager != null && Config.JUMP_ASSIST && this.verifyPlayerCondition(client, clientPlayer) && (hand = Utils.getHandHoldingWindCharge(client, clientPlayer)) != null) {
            ItemStack windChargeStack = clientPlayer.getStackInHand(hand);
            if (!clientPlayer.getItemCooldownManager().isCoolingDown(windChargeStack)) {
                float orgPitch = clientPlayer.getPitch(1.0f);
                float orgYaw = clientPlayer.getYaw(1.0f);
                if (this.wallClimbingStatus.canClimbing()) {
                    clientPlayer.setPitch(77.5f);
                    clientPlayer.setYaw(orgYaw + (float)(-45 + this.wallClimbingStatus.offset() * 45));
                    this.wallClimbingStatus = WallClimbing.resetClimbingStatus();
                    if (ElytraBoost.isElytraBoostIdle()) {
                        clientPlayer.swingHand(hand);
                        interactionManager.interactItem((PlayerEntity)clientPlayer, hand);
                    }
                    ((ClientPlayerEntityAccessor)clientPlayer).setLastPitchClient(90.0f);
                    clientPlayer.setPitch(orgPitch);
                    clientPlayer.setYaw(orgYaw);
                } else if (ElytraBoost.isElytraBoostIdle() && (Config.JUMP_SPAM || clientPlayer.isOnGround())) {
                    if (clientPlayer.isSneaking() || JumpController.ON_SLIME_BLOCK) {
                        MaceAttackAssistanceClient.sneakChargeJump(clientPlayer, hand);
                    } else {
                        MaceAttackAssistanceClient.requireChargeJump = true;
                        return false;
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
            if (JumpController.JUMP) {
                PlayerInput old = clientPlayer.input.playerInput;
                clientPlayer.input.playerInput = new PlayerInput(old.forward(), old.backward(), old.left(), old.right(), true, old.sneak(), old.sprint());
            }
            if (FlappingSuppression.jumpSuppressionCounter > 0) {
                PlayerInput old = clientPlayer.input.playerInput;
                clientPlayer.input.playerInput = new PlayerInput(old.forward(), old.backward(), old.left(), old.right(), false, old.sneak(), old.sprint());
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
}
