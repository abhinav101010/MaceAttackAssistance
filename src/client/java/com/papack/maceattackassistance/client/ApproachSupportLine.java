/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 */
package com.papack.maceattackassistance.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.papack.maceattackassistance.client.config.Config;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public class ApproachSupportLine {
    private static final double DEG_TO_RAD = Math.PI / 180;
    private static Vec3 targetCenter;
    private static Vec3 selfPos;

    public static void tick(LocalPlayer clientPlayer, Entity target) {
        targetCenter = target.getBoundingBox().getCenter();
        selfPos = clientPlayer.position().add(0.0, (double)clientPlayer.getEyeHeight(), 0.0);
    }

    public static void drawFanLines(PoseStack matrices, VertexConsumer buffer) {
        double segmentLength = 10.0;
        Minecraft client = Minecraft.getInstance();
        Vec3 camPos = client.gameRenderer.mainCamera().position();
        for (int i = 0; i < 2; ++i) {
            Vec3 toPlayer = selfPos.subtract(targetCenter);
            double totalLength = toPlayer.length();
            double theta = Math.toRadians(i == 0 ? 60.0 : 70.0);
            Vec3 horizontalDir = new Vec3(toPlayer.x, 0.0, toPlayer.z).normalize();
            double y = totalLength * Math.sin(theta);
            double horizontalLength = totalLength * Math.cos(theta);
            Vec3 baseEnd = targetCenter.add(horizontalDir.scale(horizontalLength)).add(0.0, y, 0.0);
            Vec3 line1Dir = ApproachSupportLine.rotateAroundAxis(baseEnd.subtract(targetCenter).normalize(), new Vec3(0.0, 1.0, 0.0), 0.08726646259971647);
            Vec3 line2Dir = ApproachSupportLine.rotateAroundAxis(baseEnd.subtract(targetCenter).normalize(), new Vec3(0.0, 1.0, 0.0), -0.08726646259971647);
            Vec3 start = targetCenter.subtract(camPos);
            ApproachSupportLine.drawSegmentedLine(matrices, buffer, start, targetCenter.add(line1Dir.scale(totalLength)).subtract(camPos), segmentLength);
            ApproachSupportLine.drawSegmentedLine(matrices, buffer, start, targetCenter.add(line2Dir.scale(totalLength)).subtract(camPos), segmentLength);
        }
    }

    private static void drawSegmentedLine(PoseStack matrices, VertexConsumer buffer, Vec3 start, Vec3 end, double segmentLength) {
        Vec3 delta = end.subtract(start);
        double length = delta.length();
        Vec3 dir = delta.normalize();
        int segments = (int)Math.ceil(length / segmentLength);
        for (int i = 0; i < segments; ++i) {
            double t0 = (double)i * segmentLength;
            double t1 = Math.min((double)(i + 1) * segmentLength, length);
            Vec3 segStart = start.add(dir.scale(t0));
            Vec3 segEnd = start.add(dir.scale(t1));
            float r = i % 3 == 0 ? 1.0f : 0.0f;
            float g = i % 3 == 1 ? 1.0f : 0.0f;
            float b = i % 3 == 2 ? 1.0f : 0.0f;
            ApproachSupportLine.drawLine(matrices, buffer, segStart, segEnd, r, g, b);
        }
    }

    private static void drawLine(PoseStack matrices, VertexConsumer buffer, Vec3 start, Vec3 end, float r, float g, float b) {
        Matrix4f matrix = matrices.last().pose();
        Vec3 dir = end.subtract(start).normalize();
        buffer.addVertex((Matrix4fc)matrix, (float)start.x, (float)start.y, (float)start.z).setColor(r, g, b, 0.4f).setNormal((float)dir.x, (float)dir.y, (float)dir.z);
        buffer.addVertex((Matrix4fc)matrix, (float)end.x, (float)end.y, (float)end.z).setColor(r, g, b, 0.4f).setNormal((float)dir.x, (float)dir.y, (float)dir.z);
    }

    private static Vec3 rotateAroundAxis(Vec3 v, Vec3 axis, double angleRad) {
        axis = axis.normalize();
        double cos = Math.cos(angleRad);
        double sin = Math.sin(angleRad);
        Vec3 term1 = v.scale(cos);
        Vec3 term2 = axis.cross(v).scale(sin);
        Vec3 term3 = axis.scale(axis.dot(v) * (1.0 - cos));
        return term1.add(term2).add(term3);
    }

    public static Player findNearestPlayer(Minecraft client, LocalPlayer clientPlayer, int rangeXZ, int rangeY) {
        double downDistance = Math.max(50.0, (double)Config.RADAR_DOWNWARD);
        double closestDistance = Double.MAX_VALUE;
        Player nearestPlayer = null;
        Vec3 playerEyePos = clientPlayer.getEyePosition();
        Vec3 playerPos = clientPlayer.position();
        AABB pBox = clientPlayer.getBoundingBox();
        Vec3 start = new Vec3(pBox.minX, playerEyePos.y() + 3.0, pBox.minZ);
        Vec3 end = new Vec3(pBox.maxX, playerEyePos.y() - downDistance, pBox.maxZ);
        AABB searchArea = new AABB(start, end).inflate((double)rangeXZ, (double)rangeY, (double)rangeXZ);
        if (client.level != null) {
            List<Entity> nearbyEntities = client.level.getEntities((Entity)clientPlayer, searchArea, entity -> {
                Player target;
                return entity instanceof Player && (target = (Player)entity).onGround();
            });
            for (Entity entity2 : nearbyEntities) {
                double distance = playerPos.distanceTo(entity2.position());
                if (!(entity2 instanceof Player)) continue;
                Player playerEntity = (Player)entity2;
                if (!(distance < closestDistance)) continue;
                closestDistance = distance;
                nearestPlayer = playerEntity;
            }
        }
        return nearestPlayer;
    }
}
