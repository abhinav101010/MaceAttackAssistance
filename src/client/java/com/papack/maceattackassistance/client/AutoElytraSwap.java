/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.BeamRenderHandler;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.config.Config;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class AutoElytraSwap {
    private static boolean flagAutoElytraSwap = false;
    private static InteractionHand handStatus = null;

    public static void setFlag(Boolean flag) {
        flagAutoElytraSwap = flag;
    }

    public static boolean getFlag() {
        return flagAutoElytraSwap;
    }

    public static InteractionHand getHandStatus() {
        return handStatus;
    }

    public static boolean condition() {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer clientPlayer = client.player;
        if (clientPlayer == null) {
            return false;
        }
        if (!clientPlayer.isFallFlying()) {
            return false;
        }
        if (!AutoElytraSwap.hasChestPlate(clientPlayer.getMainHandItem(), clientPlayer.getOffhandItem())) {
            return false;
        }
        if (!client.options.keyJump.isDown()) {
            return false;
        }
        if (clientPlayer.getDeltaMovement().y() > 0.0) {
            return false;
        }
        Level world = clientPlayer.level();
        List<Entity> targetList = AutoElytraSwap.getTargetEntityList(client, clientPlayer, world);
        if (targetList == null) {
            return false;
        }
        Entity entity = AutoElytraSwap.findNearestTarget(clientPlayer, targetList);
        if (!(entity instanceof LivingEntity)) {
            return false;
        }
        LivingEntity target = (LivingEntity)entity;
        MaceAttackAssistanceClient.setTargetMob((Entity)target);
        return AutoElytraSwap.simulateLandingNormal(clientPlayer, target.position(), target.isBlocking());
    }

    public static List<Entity> getTargetEntityList(Minecraft client, LocalPlayer clientPlayer, Level world) {
        if (!world.isClientSide()) {
            return null;
        }
        double upRange = 10.0;
        double downRange = 20.0;
        double horiRange = 30.0;
        Vec3 playerEyePos = clientPlayer.getEyePosition();
        float playerPitch = clientPlayer.getXRot();
        float playerYaw = clientPlayer.getYRot();
        float yawFovDegrees = 40.0f;
        float pitchFovDegrees = 180.0f;
        AABB pBox = clientPlayer.getBoundingBox();
        Vec3 start = new Vec3(pBox.minX, playerEyePos.y() + upRange, pBox.minZ);
        Vec3 end = new Vec3(pBox.maxX, playerEyePos.y() - downRange, pBox.maxZ);
        AABB searchArea = new AABB(start, end).inflate(horiRange, 0.0, horiRange);
        List<Entity> worldEntities = new ArrayList<Entity>();
        if (client.level != null) {
            worldEntities = client.level.getEntities((Entity)clientPlayer, searchArea, entity -> entity.showVehicleHealth() && entity.isAlive() && entity.onGround() && MaceAttackAssistanceClient.isAllowedTarget(entity) && !BeamRenderHandler.isOutsideViewCone(playerEyePos, entity.position(), playerYaw, playerPitch, yawFovDegrees, pitchFovDegrees));
        }
        return worldEntities;
    }

    public static Entity findNearestTarget(LocalPlayer clientPlayer, List<Entity> nearbyEntities) {
        double closestDistance = Double.MAX_VALUE;
        Entity nearestEntity = null;
        for (Entity entity : nearbyEntities) {
            double distance = clientPlayer.distanceTo(entity);
            if (!(distance < closestDistance)) continue;
            closestDistance = distance;
            nearestEntity = entity;
        }
        return nearestEntity;
    }

    private static boolean simulateLandingNormal(LocalPlayer player, Vec3 targetPos, boolean isBlocking) {
        if (player.getXRot() < 0.0f) {
            return false;
        }
        double speed = player.getDeltaMovement().length();
        double distance = player.position().distanceTo(targetPos);
        return distance / speed <= (isBlocking ? (double)Config.AUTO_ELYTRA_TICK_AHEAD * 0.1 : (double)Config.AUTO_ELYTRA_TICK_AHEAD_NORMAL * 0.1);
    }

    public static boolean hasChestPlate(ItemStack mainStack, ItemStack offStack) {
        if (mainStack.is(ItemTags.CHEST_ARMOR)) {
            handStatus = InteractionHand.MAIN_HAND;
            return true;
        }
        if (offStack.is(ItemTags.CHEST_ARMOR)) {
            handStatus = InteractionHand.OFF_HAND;
            return true;
        }
        handStatus = null;
        return false;
    }
}
