/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.FriendManager;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PearlGrapple {
    private static final double PEARL_SPEED = 1.5;
    private static final double PEARL_GRAVITY = 0.03;
    private static final double PEARL_Y_DRAG = 0.98;
    private static final double PEARL_XZ_DRAG = 0.98;
    private static final double ENTITY_GRAVITY = 0.08;
    private static final double ENTITY_DRAG = 0.98;
    private static final float MAX_ANGLE = 89.8f;
    private static Vec3 peakPos = null;

    public static void setPeakPos(Vec3 pos) {
        peakPos = pos;
    }

    public static Vec3 getPeakPos() {
        return peakPos;
    }

    public static Entity findNearestMobForGrapple(int range) {
        List<Entity> nearbyEntities;
        Minecraft client = Minecraft.getInstance();
        LocalPlayer clientPlayer = client.player;
        if (clientPlayer == null) {
            return null;
        }
        double closestDistance = Double.MAX_VALUE;
        Entity nearestMob = null;
        Vec3 playerPos = clientPlayer.position();
        AABB searchArea = clientPlayer.getBoundingBox().inflate((double)range);
        if (client.level != null && !(nearbyEntities = client.level.getEntities((Entity)clientPlayer, searchArea, entity -> (!Config.FRIEND_PROTECTION || !FriendManager.isFriend(entity.getUUID())) && entity.showVehicleHealth() && entity.isAlive() && MaceAttackAssistanceClient.isAllowedTarget(entity) && Utils.isVisibleFromPlayer(client, clientPlayer, entity))).isEmpty()) {
            for (Entity entity2 : nearbyEntities) {
                double distance = playerPos.distanceToSqr(entity2.position());
                if (!(distance < closestDistance)) continue;
                closestDistance = distance;
                nearestMob = entity2;
            }
        }
        return nearestMob;
    }

    public static Vec3 predictEntityPos(Vec3 pos, Vec3 vel, int ticks) {
        double px = pos.x;
        double py = pos.y;
        double pz = pos.z;
        double vx = vel.x;
        double vy = vel.y;
        double vz = vel.z;
        for (int i = 0; i < ticks; ++i) {
            px += vx;
            py += vy;
            pz += vz;
            vx *= 0.98;
            vy = (vy - 0.08) * 0.98;
            vz *= 0.98;
        }
        return new Vec3(px, py, pz);
    }

    public static Vec3 getObservedVelocity(Entity target) {
        double vx = target.getX() - target.xo;
        double vy = target.getY() - target.yo;
        double vz = target.getZ() - target.zo;
        if (target.onGround()) {
            vy = 0.0;
        }
        return new Vec3(vx, vy, vz);
    }

    public static float[] calculateAngleWithObservedVel(LocalPlayer shooter, Entity target) {
        Vec3 observedVel = PearlGrapple.getObservedVelocity(target);
        Vec3 startPos = shooter.getEyePosition();
        Vec3 shooterVel = PearlGrapple.getObservedVelocity((Entity)shooter);
        Vec3 targetPos = target.getEyePosition();
        Vec3 deltaNow = targetPos.subtract(startPos);
        double horizontalDistance = Math.sqrt(deltaNow.x * deltaNow.x + deltaNow.z * deltaNow.z);
        float basePitch = (float)(-Math.toDegrees(Math.atan2(deltaNow.y, horizontalDistance)));
        basePitch = target.onGround() ? basePitch : Math.min(89.8f, basePitch + 10.0f);
        double dist = startPos.distanceTo(targetPos);
        int minT = Math.max(1, (int)(dist / 1.5) - 2);
        int maxT = (int)Math.min(100.0, dist * 5.0 + 10.0);
        double hitThreshold = 1.0;
        float precision = 0.2f;
        double nearestDistance = Double.MAX_VALUE;
        float[] nearestAngle = null;
        for (float pitch = basePitch; pitch >= -89.8f; pitch -= precision) {
            for (int t = minT; t <= maxT; ++t) {
                double verticalError;
                Vec3 predictedPos = target.onGround() ? targetPos.add(observedVel.scale((double)Math.min(t, 1))) : PearlGrapple.predictEntityPos(targetPos, observedVel, t);
                Vec3 delta = predictedPos.subtract(startPos);
                float yaw = (float)(Math.atan2(-delta.x, delta.z) * 57.29577951308232);
                double f = -Math.sin((double)yaw * 0.017453292) * Math.cos((double)pitch * 0.017453292);
                double g = -Math.sin((double)pitch * 0.017453292);
                double h = Math.cos((double)yaw * 0.017453292) * Math.cos((double)pitch * 0.017453292);
                double vx = f * 1.5 + shooterVel.x;
                double vy = g * 1.5 + shooterVel.y;
                double vz = h * 1.5 + shooterVel.z;
                double px = startPos.x;
                double py = startPos.y;
                double pz = startPos.z;
                for (int i = 0; i < t; ++i) {
                    px += vx;
                    py += vy;
                    pz += vz;
                    vx *= 0.98;
                    vy = (vy - 0.03) * 0.98;
                    vz *= 0.98;
                }
                Vec3 pearlPos = new Vec3(px, py, pz);
                double distance = pearlPos.distanceTo(predictedPos);
                double score = distance + (verticalError = Math.abs(py - predictedPos.y)) * 2.0;
                if (score < nearestDistance) {
                    nearestDistance = score;
                    nearestAngle = new float[]{yaw, pitch};
                }
                if (!(distance < hitThreshold)) continue;
                if (Config.DEBUG_SCREEN) {
                    MaceAttackAssistanceClient.LOGGER.info("calc - success");
                }
                return new float[]{yaw, pitch};
            }
        }
        if (Config.DEBUG_SCREEN) {
            MaceAttackAssistanceClient.LOGGER.info("calc - fallback");
        }
        return nearestAngle;
    }

    public static int getAdvancedDelayTicks(LocalPlayer shooter, Entity target) {
        int tTarget;
        Vec3 observedVel = PearlGrapple.getObservedVelocity(target);
        if (observedVel.y <= 0.0) {
            return 0;
        }
        Vec3 myPos = shooter.getEyePosition();
        Vec3 myVel = shooter.getDeltaMovement();
        if (shooter.onGround()) {
            myVel = new Vec3(myVel.x, 0.0, myVel.z);
        }
        double tempVy = observedVel.y;
        for (tTarget = 0; tempVy > 0.0 && tTarget < 100; ++tTarget) {
            tempVy = (tempVy - 0.08) * 0.98;
        }
        for (int d = 0; d < tTarget; ++d) {
            int remaining;
            Vec3 futureTargetPos;
            Vec3 futureAim;
            Vec3 futureStartPos = PearlGrapple.predictEntityPos(myPos, myVel, d);
            int tPearl = PearlGrapple.calculatePearlTravelTime(futureStartPos, futureAim = PearlGrapple.predictEntityPos(futureTargetPos = PearlGrapple.predictEntityPos(target.getEyePosition(), observedVel, d), observedVel, remaining = Math.max(0, tTarget - d)));
            if (d + tPearl < tTarget) continue;
            PearlGrapple.setPeakPos(futureAim);
            return d;
        }
        return 0;
    }

    private static int calculatePearlTravelTime(Vec3 start, Vec3 target) {
        Vec3 dir = target.subtract(start).normalize();
        double vx = dir.x * 1.5;
        double vy = dir.y * 1.5;
        double vz = dir.z * 1.5;
        double px = start.x;
        double py = start.y;
        double pz = start.z;
        double threshold = 1.0;
        for (int t = 0; t < 100; ++t) {
            double dx = target.x - (px += vx);
            double dy = target.y - (py += vy);
            double dz = target.z - (pz += vz);
            if (dx * dx + dy * dy + dz * dz < threshold * threshold) {
                return t;
            }
            vx *= 0.98;
            vz *= 0.98;
            vy = (vy - 0.03) * 0.98;
        }
        return (int)(start.distanceTo(target) / 1.5);
    }
}
