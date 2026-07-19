/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.Hand
 *  net.minecraft.Entity
 *  net.minecraft.LivingEntity
 *  net.minecraft.ItemStack
 *  net.minecraft.World
 *  net.minecraft.Box
 *  net.minecraft.Vec3d
 *  net.minecraft.MinecraftClient
 *  net.minecraft.ItemTags
 *  net.minecraft.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.BeamRenderHandler;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.config.Config;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.client.network.ClientPlayerEntity;

public class AutoElytraSwap {
    private static boolean flagAutoElytraSwap = false;
    private static Hand handStatus = null;

    public static void setFlag(Boolean flag) {
        flagAutoElytraSwap = flag;
    }

    public static boolean getFlag() {
        return flagAutoElytraSwap;
    }

    public static Hand getHandStatus() {
        return handStatus;
    }

    public static boolean condition() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayer = client.player;
        if (clientPlayer == null) {
            return false;
        }
        if (!clientPlayer.isGliding()) {
            return false;
        }
        if (!AutoElytraSwap.hasChestPlate(clientPlayer.getMainHandStack(), clientPlayer.getOffHandStack())) {
            return false;
        }
        if (!client.options.jumpKey.isPressed()) {
            return false;
        }
        if (clientPlayer.getVelocity().getY() > 0.0) {
            return false;
        }
        World world = clientPlayer.getEntityWorld();
        if (world == null) {
            return false;
        }
        List<Entity> targetList = AutoElytraSwap.getTargetEntityList(client, clientPlayer, world);
        if (targetList == null) {
            return false;
        }
        Entity nearestTarget = AutoElytraSwap.findNearestTarget(clientPlayer, targetList);
        if (!(nearestTarget instanceof LivingEntity)) {
            return false;
        }
        LivingEntity target = (LivingEntity)nearestTarget;
        MaceAttackAssistanceClient.setTargetMob((Entity)target);
        return AutoElytraSwap.simulateLandingNormal(clientPlayer, target.getEntityPos(), target.isBlocking());
    }

    public static List<Entity> getTargetEntityList(MinecraftClient client, ClientPlayerEntity clientPlayer, World world) {
        if (!world.isClient()) {
            return null;
        }
        double upRange = 10.0;
        double downRange = 20.0;
        double horiRange = 30.0;
        Vec3d playerEyePos = clientPlayer.getEyePos();
        float playerPitch = clientPlayer.getPitch();
        float playerYaw = clientPlayer.getYaw();
        float yawFovDegrees = 40.0f;
        float pitchFovDegrees = 180.0f;
        Box pBox = clientPlayer.getBoundingBox();
        Vec3d start = new Vec3d(pBox.minX, playerEyePos.getY() + upRange, pBox.minZ);
        Vec3d end = new Vec3d(pBox.maxX, playerEyePos.getY() - downRange, pBox.maxZ);
        Box searchArea = new Box(start, end).expand(horiRange, 0.0, horiRange);
        List<Entity> worldEntities = new ArrayList<Entity>();
        if (client.world != null) {
            worldEntities = client.world.getOtherEntities((Entity)clientPlayer, searchArea, entity -> entity.isLiving() && entity.isAlive() && entity.isOnGround() && MaceAttackAssistanceClient.isAllowedTarget(entity) && !BeamRenderHandler.isOutsideViewCone(playerEyePos, entity.getEntityPos(), playerYaw, playerPitch, yawFovDegrees, pitchFovDegrees));
        }
        return worldEntities;
    }

    public static Entity findNearestTarget(ClientPlayerEntity clientPlayer, List<Entity> nearbyEntities) {
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

    private static boolean simulateLandingNormal(ClientPlayerEntity player, Vec3d targetPos, boolean isBlocking) {
        if (player.getPitch() < 0.0f) {
            return false;
        }
        double speed = player.getVelocity().length();
        double distance = player.getEntityPos().distanceTo(targetPos);
        return distance / speed <= (isBlocking ? (double)Config.AUTO_ELYTRA_TICK_AHEAD * 0.1 : (double)Config.AUTO_ELYTRA_TICK_AHEAD_NORMAL * 0.1);
    }

    public static boolean hasChestPlate(ItemStack mainStack, ItemStack offStack) {
        if (mainStack.isIn(ItemTags.CHEST_ARMOR)) {
            handStatus = Hand.MAIN_HAND;
            return true;
        }
        if (offStack.isIn(ItemTags.CHEST_ARMOR)) {
            handStatus = Hand.OFF_HAND;
            return true;
        }
        handStatus = null;
        return false;
    }
}
