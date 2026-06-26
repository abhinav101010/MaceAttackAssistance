/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants
 *  com.mojang.blaze3d.platform.Window
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.Vec3
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package com.papack.maceattackassistance.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.MAAState;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.MacroController;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.StunSlam;
import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(value=EnvType.CLIENT)
@Mixin(value={Minecraft.class})
public abstract class MixinMinecraft {
    @Redirect(method={"handleKeybinds()V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/KeyMapping;consumeClick()Z"))
    private boolean canSwingDownTheMace(KeyMapping instance) {
        boolean canDT;
        if (MaceAttackAssistanceClient.getFlagElytraSpear()) {
            MaceAttackAssistanceClient.setFlagElytraSpear(false);
            return false;
        }
        if (MAAState.antiCheat || MAAState.z()) {
            return instance.consumeClick();
        }
        if (MAAState.x() && MaceAttackAssistanceClient.getTargetMob() instanceof Player) {
            return instance.consumeClick();
        }
        boolean bl = canDT = Utils.canDoubleTap() || JobManager.checkStatus(StatusType.DOUBLE_TAP) || JobManager.checkStatus(StatusType.DOUBLE_TAP_ELYTRA_SPEAR);
        if (!Config.EXTREME || canDT) {
            if (JobManager.checkOrderIsEmpty()) {
                Minecraft client = Minecraft.getInstance();
                LocalPlayer clientPlayer = client.player;
                Screen screen = client.gui.screen();
                Config.SwingData swingData = this.getSwingData(client);
                boolean isAttackKey = instance.saveString().equals(client.options.keyAttack.saveString());
                boolean isUseKey = instance.saveString().equals(client.options.keyUse.saveString());
                int highSpeed = 1;
                if ((isAttackKey || isUseKey) && screen == null && clientPlayer != null && client.options.keyAttack.isDown() && Config.ATTACK_ASSISTANCE && (Utils.isNotUsingElytra(clientPlayer) || canDT)) {
                    float HORIZONTAL_ALLOW_DISTANCE = Utils.checkLeftClickBtnAndSpear(client, clientPlayer) ? 4.5f : 4.23f;
                    Vec3 playerVelocity = clientPlayer.getDeltaMovement();
                    double yV = playerVelocity.y();
                    int speedLevel = Utils.speedOverThreshold(clientPlayer);
                    boolean isOverSpeed = speedLevel > 0;
                    Entity target = MaceAttackAssistanceClient.getTargetMob();
                    if (target == null) {
                        target = Utils.getCrosshairEntity();
                    }
                    if (target instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity)target;
                        if (Utils.xzDistance(clientPlayer, target) <= (double)HORIZONTAL_ALLOW_DISTANCE) {
                            boolean isBlocking = livingEntity.isBlocking();
                            StatusType statusType = StunSlam.stunSlamCondition(clientPlayer, target);
                            if (Config.STUN_SLAMMING && isBlocking && statusType != null) {
                                TickScheduler.setDelayTask(0, () -> {
                                    int slot;
                                    if (JobManager.checkOrderIsEmpty() && (slot = clientPlayer.getInventory().getSelectedSlot()) > -1) {
                                        if (Config.DEBUG_SCREEN) {
                                            MaceAttackAssistanceClient.LOGGER.info("y-velocity {}", (Object)clientPlayer.getDeltaMovement().y);
                                        }
                                        JobManager.setOrder(statusType, slot);
                                    }
                                });
                                return false;
                            }
                            if (yV > Config.VELOCITY_BY_DISTANCE[Config.FALL_VELOCITY[0]]) {
                                highSpeed = 0;
                            }
                            if (isOverSpeed) {
                                highSpeed = speedLevel;
                            }
                            Vec3 nextPos = Utils.simulateFuturePos(clientPlayer.position(), clientPlayer.getDeltaMovement(), Math.min(1, speedLevel));
                            double d0 = nextPos.distanceTo(target.position());
                            boolean result = d0 < (double)(highSpeed > 0 ? 3.0f : 2.81f);
                            Entity entity = client.crosshairPickEntity;
                            if (entity != null && (Config.AIM_ASSIST || result) || isOverSpeed && result) {
                                if (Config.DEBUG_SCREEN) {
                                    MaceAttackAssistanceClient.LOGGER.info("DT distance - attack: {}", entity != null ? Float.valueOf(clientPlayer.distanceTo(entity)) : "");
                                }
                                MacroController.macroController(clientPlayer, clientPlayer.level(), livingEntity, highSpeed);
                            }
                        }
                    }
                    Entity en = Utils.getCrosshairEntity();
                    // Use immediate swing check for more responsive temporal swing control
                    boolean canSwingNow = this.shouldHandSwingImmediate(client) && (Utils.verifyGround(clientPlayer, Config.HEIGHT_THRESHOLD) || MixinMinecraft.playerCondition(clientPlayer));
                    if ((isOverSpeed || canSwingNow) && !clientPlayer.onGround() && (en == null || en.onGround())) {
                        MaceAttackAssistanceClient.flag_attack_canceled = true;
                        return false;
                    }
                }
                return instance.consumeClick();
            }
        } else {
            Entity target;
            Minecraft client = Minecraft.getInstance();
            LocalPlayer clientPlayer = client.player;
            Screen screen = client.gui.screen();
            boolean isAttackKey = instance.saveString().equals(client.options.keyAttack.saveString());
            if (isAttackKey && screen == null && clientPlayer != null && client.options.keyAttack.isDown() && Config.ATTACK_ASSISTANCE && Utils.isNotUsingElytra(clientPlayer) && this.check(client, clientPlayer, target = Utils.getCrosshairEntity())) {
                MaceAttackAssistanceClient.flag_attack_canceled = true;
                return false;
            }
        }
        return instance.consumeClick();
    }

    @Unique
    private boolean check(Minecraft client, LocalPlayer clientPlayer, Entity target) {
        if (target != null && !target.onGround()) {
            return false;
        }
        if (clientPlayer.onGround()) {
            return false;
        }
        // Use immediate check for more responsive swing control
        if (!this.shouldHandSwingImmediate(client)) {
            return false;
        }
        return Utils.verifyGround(clientPlayer, Config.HEIGHT_THRESHOLD) || MixinMinecraft.playerCondition(clientPlayer);
    }

    @Unique
    private static boolean playerCondition(LocalPlayer clientPlayer) {
        return clientPlayer.isFallFlying() || Utils.speedOverThreshold(clientPlayer) > 0;
    }

    @Unique
    private Config.SwingData getSwingData(Minecraft client) {
        boolean isKeyPressed = InputConstants.isKeyDown((Window)client.getWindow(), (int)Config.SWING_TOGGLE.getGlfwKey());
        boolean canSwing = Config.WEAPON_SWING != isKeyPressed;
        return new Config.SwingData(canSwing, isKeyPressed);
    }

    @Unique
    private boolean shouldHandSwing(Minecraft client) {
        return InputConstants.isKeyDown((Window)client.getWindow(), (int)Config.SWING_TOGGLE.getGlfwKey()) != Config.WEAPON_SWING;
    }

    @Unique
    private boolean shouldHandSwingImmediate(Minecraft client) {
        // Immediate check without any delay - used for more responsive swing control
        return InputConstants.isKeyDown((Window)client.getWindow(), (int)Config.SWING_TOGGLE.getGlfwKey()) != Config.WEAPON_SWING;
    }
}
