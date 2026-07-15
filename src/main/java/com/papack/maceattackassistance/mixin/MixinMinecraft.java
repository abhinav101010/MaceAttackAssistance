/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.Window
 *  net.minecraft.Entity
 *  net.minecraft.LivingEntity
 *  net.minecraft.Vec3d
 *  net.minecraft.Packet
 *  net.minecraft.UpdateSelectedSlotC2SPacket
 *  net.minecraft.KeyBinding
 *  net.minecraft.MinecraftClient
 *  net.minecraft.InputUtil
 *  net.minecraft.Screen
 *  net.minecraft.ClientPlayNetworkHandler
 *  net.minecraft.ClientPlayerEntity
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package com.papack.maceattackassistance.mixin;

import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.MacroController;
import com.papack.maceattackassistance.client.PrevSlotManager;
import com.papack.maceattackassistance.client.SpearAttacks;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.ZoomState;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.Window;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(value=EnvType.CLIENT)
@Mixin(value={MinecraftClient.class})
public abstract class MixinMinecraft {
    @Shadow
    @Nullable
    public abstract ClientPlayNetworkHandler getNetworkHandler();

    @Redirect(method={"handleInputEvents()V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/option/KeyBinding;wasPressed()Z"))
    private boolean canSwingDownTheMace(KeyBinding instance) {
        boolean canDT;
        if (ZoomState.MAAClientState.antiCheat || ZoomState.KeyManager.keyManager()) {
            return instance.wasPressed();
        }
        boolean bl = canDT = Utils.canDoubleTap() || JobManager.checkStatus(StatusType.DOUBLE_TAP);
        if (!Config.EXTREME || canDT) {
            if (JobManager.checkOrderIsEmpty()) {
                MinecraftClient client = ((MinecraftClient)(Object)this);
                ClientPlayerEntity clientPlayer = client.player;
                Screen screen = client.currentScreen;
                float ALLOW_DISTANCE = 4.23f;
                Config.SwingData swingData = this.getSwingData(client);
                boolean isAttackKey = instance.getBoundKeyTranslationKey().equals(client.options.attackKey.getBoundKeyTranslationKey());
                boolean isUseKey = instance.getBoundKeyTranslationKey().equals(client.options.useKey.getBoundKeyTranslationKey());
                int highSpeed = 1;
                if ((isAttackKey || isUseKey) && screen == null && clientPlayer != null && client.options.attackKey.isPressed() && Config.ATTACK_ASSISTANCE && (Utils.isNotUsingElytra(clientPlayer) || canDT)) {
                    Vec3d playerVelocity = clientPlayer.getVelocity();
                    double yV = playerVelocity.getY();
                    int speedLevel = Utils.speedOverThreshold(clientPlayer);
                    boolean isOverSpeed = speedLevel > 0;
                    Entity target = MaceAttackAssistanceClient.getTargetMob();
                    if (target == null) {
                        target = Utils.getCrosshairEntity();
                    }
                    if (target instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity)target;
                        double getDistance = clientPlayer.distanceTo(target);
                        if (Utils.xzDistance(clientPlayer, target) <= (double)ALLOW_DISTANCE) {
                            boolean d;
                            boolean isOffsetRequired = livingEntity.isBlocking();
                            boolean bl2 = d = getDistance <= 20.0;
                            if (Config.STUN_SLAMMING && d && isOffsetRequired) {
                                int currentSlot = PrevSlotManager.isEmpty() ? clientPlayer.getInventory().getSelectedSlot() : PrevSlotManager.getLastOrderSlot();
                                int axeSlot = JobManager.getAxeSlotId(clientPlayer);
                                if (axeSlot > -1 && axeSlot != currentSlot) {
                                    JobManager.setPreviousSlot(StatusType.NONE, currentSlot);
                                    clientPlayer.getInventory().setSelectedSlot(axeSlot);
                                    ClientPlayNetworkHandler handler = this.getNetworkHandler();
                                    if (handler != null) {
                                        handler.sendPacket((Packet)new UpdateSelectedSlotC2SPacket(axeSlot));
                                    }
                                }
                            }
                            if (yV > Config.VELOCITY_BY_DISTANCE[Config.FALL_VELOCITY[0]]) {
                                highSpeed = 0;
                            }
                            if (isOverSpeed) {
                                highSpeed = speedLevel;
                            }
                            Vec3d nextPos = Utils.simulateFuturePos(clientPlayer.getEntityPos(), clientPlayer.getVelocity(), Math.min(1, speedLevel));
                            double d0 = nextPos.distanceTo(target.getEntityPos());
                            boolean result = d0 < (double)(highSpeed > 0 ? 3.0f : (float)Config.ATTACK_RANGE * 0.01f);
                            Entity entity = client.targetedEntity;
                            if (entity != null && (Config.AIM_ASSIST || result) || isOverSpeed && result) {
                                MacroController.macroController(clientPlayer, clientPlayer.getEntityWorld(), livingEntity, highSpeed);
                            }
                        }
                    }
                    Entity en = Utils.getCrosshairEntity();
                    if (!SpearAttacks.SPEAR_SLAM_ACTIVE && (isOverSpeed || swingData.canSwing() && (Utils.verifyGround(clientPlayer, Config.HEIGHT_THRESHOLD) || MixinMinecraft.playerCondition(clientPlayer))) && !clientPlayer.isOnGround() && (en == null || en.isOnGround())) {
                        MaceAttackAssistanceClient.flag_attack_canceled = true;
                        return false;
                    }
                }
                return instance.wasPressed();
            }
        } else {
            Entity target;
            MinecraftClient client = ((MinecraftClient)(Object)this);
            ClientPlayerEntity clientPlayer = client.player;
            Screen screen = client.currentScreen;
            boolean isAttackKey = instance.getBoundKeyTranslationKey().equals(client.options.attackKey.getBoundKeyTranslationKey());
            if (!SpearAttacks.SPEAR_SLAM_ACTIVE && isAttackKey && screen == null && clientPlayer != null && client.options.attackKey.isPressed() && Config.ATTACK_ASSISTANCE && Utils.isNotUsingElytra(clientPlayer) && this.check(client, clientPlayer, target = Utils.getCrosshairEntity())) {
                MaceAttackAssistanceClient.flag_attack_canceled = true;
                return false;
            }
        }
        return instance.wasPressed();
    }

    @Unique
    private boolean check(MinecraftClient client, ClientPlayerEntity clientPlayer, Entity target) {
        if (target != null && !target.isOnGround()) {
            return false;
        }
        if (clientPlayer.isOnGround()) {
            return false;
        }
        if (!this.shouldHandSwing(client)) {
            return false;
        }
        return Utils.verifyGround(clientPlayer, Config.HEIGHT_THRESHOLD) || MixinMinecraft.playerCondition(clientPlayer);
    }

    @Unique
    private static boolean playerCondition(ClientPlayerEntity clientPlayer) {
        return clientPlayer.isGliding() || Utils.speedOverThreshold(clientPlayer) > 0;
    }

    @Unique
    private Config.SwingData getSwingData(MinecraftClient client) {
        boolean isKeyPressed = InputUtil.isKeyPressed((Window)client.getWindow(), (int)Config.SWING_TOGGLE.getGlfwKey());
        boolean canSwing = Config.WEAPON_SWING != isKeyPressed;
        return new Config.SwingData(canSwing, isKeyPressed);
    }

    @Unique
    private boolean shouldHandSwing(MinecraftClient client) {
        return InputUtil.isKeyPressed((Window)client.getWindow(), (int)Config.SWING_TOGGLE.getGlfwKey()) != Config.WEAPON_SWING;
    }
}
