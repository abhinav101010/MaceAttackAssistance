/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.item.ShieldItem
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.client.option.KeyBinding
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.util.InputUtil
 *  net.minecraft.client.util.InputUtil$class_306
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.network.ClientPlayerEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package com.papack.maceattackassistance.mixin;

import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.MacroController;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.StunSlam;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.mixin.KeyBindingInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(value=EnvType.CLIENT)
@Mixin(value={MinecraftClient.class})
public abstract class MixinMinecraft {
    @Redirect(method={"handleInputEvents()V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/option/KeyBinding;wasPressed()Z"))
    private boolean canSwingDownTheMace(KeyBinding instance) {
        if (!Config.EXTREME) {
            if (JobManager.checkOrderIsEmpty()) {
                MinecraftClient client = (MinecraftClient)(Object)this;
                ClientPlayerEntity clientPlayer = client.player;
                Screen screen = client.currentScreen;
                float ALLOW_DISTANCE = 4.23f;
                Config.SwingData swingData = this.getSwingData(client);
                boolean isAttackKey = instance.getBoundKeyTranslationKey().equals(client.options.attackKey.getBoundKeyTranslationKey());
                boolean flagStun = false;
                int highSpeed = 1;
                float offset = 0.0f;
                if (isAttackKey && screen == null && clientPlayer != null && client.options.attackKey.isPressed() && Config.ATTACK_ASSISTANCE && Utils.isNotUsingElytra(clientPlayer)) {
                    Vec3d playerVelocity = clientPlayer.getVelocity();
                    double yV = playerVelocity.getY();
                    int isOverSpeed = this.speedOverThreshold(clientPlayer);
                Entity target = MaceAttackAssistanceClient.getTargetMob();
                if (target != null && com.papack.maceattackassistance.client.FriendManager.isFriend(target)) {
                    return instance.wasPressed();
                }
                    if (target == null) {
                        target = client.targetedEntity;
                    }
                    if (target != null && com.papack.maceattackassistance.client.FriendManager.isFriend(target)) {
                        return instance.wasPressed();
                    }
                    if (target instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity)target;
                        double distance = clientPlayer.distanceTo(target);
                        if (Utils.xzDistance(clientPlayer, target) <= (double)ALLOW_DISTANCE) {
                            boolean isOffsetRequired;
                            boolean bl = isOffsetRequired = livingEntity.isUsingItem() && livingEntity.getActiveItem().getItem() instanceof ShieldItem;
                            if (Config.STUN_SLAMMING && isOffsetRequired) {
                                flagStun = true;
                                if (distance <= 7.0 + Math.abs(yV) * 2.5) {
                                    int currentSlot = clientPlayer.getInventory().getSelectedSlot();
                                    int axeSlot = JobManager.getAxeSlotId(clientPlayer);
                                    if (axeSlot > -1 && axeSlot != currentSlot) {
                                        JobManager.setPreviousSlot(StatusType.NONE, currentSlot);
                                        KeyBinding.onKeyPressed((InputUtil.Key)((KeyBindingInvoker)client.options.hotbarKeys[axeSlot]).accessorBoundKey());
                                    }
                                }
                            }
                            int ticksAhead = 0;
                            if (yV < Config.VELOCITY_BY_DISTANCE[35]) {
                                ++ticksAhead;
                            }
                            if (yV > Config.VELOCITY_BY_DISTANCE[Config.FALL_VELOCITY[0]]) {
                                highSpeed = 0;
                            }
                            if (swingData.canSwing()) {
                                ticksAhead += 2;
                            }
                            if (isOverSpeed > 0) {
                                highSpeed = isOverSpeed;
                            }
                            boolean shouldAttack = this.willBeInRange(clientPlayer, ticksAhead, flagStun, offset, target);
                            if (client.targetedEntity != null || shouldAttack) {
                                MacroController.macroController(clientPlayer, MinecraftClient.getInstance().world, livingEntity, highSpeed);
                            }
                        }
                    }
                    if ((isOverSpeed > 0 || swingData.canSwing() && Utils.verifyGround(clientPlayer, Config.HEIGHT_THRESHOLD)) && !clientPlayer.isOnGround() && (client.targetedEntity == null || client.targetedEntity.isOnGround())) {
                        MaceAttackAssistanceClient.flag_attack_canceled = true;
                        return false;
                    }
                }
                return instance.wasPressed();
            }
        } else {
            MinecraftClient client = (MinecraftClient)(Object)this;
            ClientPlayerEntity clientPlayer = client.player;
            Screen screen = client.currentScreen;
            boolean isAttackKey = instance.getBoundKeyTranslationKey().equals(client.options.attackKey.getBoundKeyTranslationKey());
            if (isAttackKey && screen == null && clientPlayer != null && client.options.attackKey.isPressed() && Config.ATTACK_ASSISTANCE && Utils.isNotUsingElytra(clientPlayer)) {
                Entity target = MaceAttackAssistanceClient.getTargetMob();
                if (target != null && clientPlayer.distanceTo(target) <= 5.0f) {
                    StunSlam.preSelectAxe(client, clientPlayer, target);
                }
                if (this.shouldHandSwing(client) && !clientPlayer.isOnGround() && Utils.verifyGround(clientPlayer, Config.HEIGHT_THRESHOLD) && client.targetedEntity == null) {
                    MaceAttackAssistanceClient.flag_attack_canceled = true;
                    return false;
                }
            }
        }
        return instance.wasPressed();
    }

    @Unique
    private Config.SwingData getSwingData(MinecraftClient client) {
        boolean isKeyPressed = InputUtil.isKeyPressed(client.getWindow(), Config.SWING_TOGGLE.getGlfwKey());
        boolean canSwing = Config.WEAPON_SWING != isKeyPressed;
        return new Config.SwingData(canSwing, isKeyPressed);
    }

    @Unique
    private int speedOverThreshold(ClientPlayerEntity clientPlayer) {
        double vz;
        Vec3d vec = clientPlayer.getVelocity();
        double length = vec.length();
        double vx = vec.getX();
        double v = Math.sqrt(vx * vx + (vz = vec.getZ()) * vz);
        if (v > 0.82 && length > 1.65 && !clientPlayer.isOnGround()) {
            return 2;
        }
        if (v > 0.55 && !clientPlayer.isOnGround()) {
            return 1;
        }
        return 0;
    }

    @Unique
    public boolean willBeInRange(ClientPlayerEntity clientPlayer, int ticksAhead, boolean flagStun, float offset, Entity target) {
        boolean flag1stHit;
        boolean targetIsOnGround = target.isOnGround();
        float allowDistance = (float)Config.ATTACK_RANGE * 0.01f + 0.3f;
        float yDiffThreshold = (float)Config.ATTACK_RANGE_DIFF * 0.01f;
        float standingEyeHeight = target.getStandingEyeHeight();
        float dynamicOffset = standingEyeHeight > 1.0f ? standingEyeHeight * 0.1f : 0.0f;
        Vec3d playerPos = new Vec3d(clientPlayer.getX(), clientPlayer.getY() + (double)dynamicOffset, clientPlayer.getZ());
        Vec3d targetPos = Utils.getTargetPos(target, false).add(0.0, (double)dynamicOffset, 0.0);
        Vec3d playerVelocity = clientPlayer.getVelocity();
        Vec3d targetVelocity = targetIsOnGround ? new Vec3d(target.getVelocity().getX(), 0.0, target.getVelocity().getZ()) : target.getVelocity();
        Config.VecData newPlayerVecData = this.calculateNewPosition((LivingEntity)clientPlayer, playerPos, playerVelocity, ticksAhead);
        Vec3d newPlayerPos = newPlayerVecData.position();
        Vec3d newPlayerVelocity = newPlayerVecData.velocity();
        Config.VecData newTargetVecData = this.calculateNewPosition((LivingEntity)target, targetPos, targetVelocity, ticksAhead);
        Vec3d newTargetPos = newTargetVecData.position();
        Vec3d newTargetVelocity = newTargetVecData.velocity();
        boolean newYDiff = !targetIsOnGround || flagStun || Math.abs(newPlayerPos.getY() - newTargetPos.getY()) < (double)yDiffThreshold;
        double distance = newPlayerPos.distanceTo(newTargetPos) - (double)offset;
        boolean bl = flag1stHit = distance <= (double)allowDistance && newYDiff;
        if (!flagStun || !flag1stHit) {
            return flag1stHit;
        }
        Vec3d lastPlayerPos = this.calculateNewPosition((LivingEntity)clientPlayer, newPlayerPos, newPlayerVelocity, 1).position();
        Vec3d lastTargetPos = this.calculateNewPosition((LivingEntity)target, newTargetPos, newTargetVelocity, 1).position();
        boolean lastYDiff = !targetIsOnGround || Math.abs(lastPlayerPos.getY() - lastTargetPos.getY()) < (double)yDiffThreshold;
        double lastDistance = lastPlayerPos.distanceTo(lastTargetPos) - (double)offset;
        return lastDistance - (double)offset <= (double)allowDistance && lastYDiff;
    }

    @Unique
    private Config.VecData calculateNewPosition(LivingEntity entity, Vec3d currentPosition, Vec3d currentVelocity, int ticksAhead) {
        double newVecX = currentVelocity.getX() * (double)ticksAhead;
        double newVecZ = currentVelocity.getZ() * (double)ticksAhead;
        double newVecY = currentVelocity.getY();
        if (!entity.isOnGround()) {
            for (int i = 0; i < Math.abs(ticksAhead); ++i) {
                newVecY = (newVecY - 0.08) * 0.98;
            }
        }
        Vec3d position = new Vec3d(currentPosition.getX() + newVecX, currentPosition.getY() + newVecY, currentPosition.getZ() + newVecZ);
        Vec3d velocity = new Vec3d(currentVelocity.getX(), newVecY, currentVelocity.getZ());
        return new Config.VecData(position, velocity);
    }

    @Unique
    private boolean shouldHandSwing(MinecraftClient client) {
        return InputUtil.isKeyPressed(client.getWindow(), Config.SWING_TOGGLE.getGlfwKey()) != Config.WEAPON_SWING;
    }
}
