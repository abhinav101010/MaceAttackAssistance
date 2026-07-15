/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.Entity
 *  net.minecraft.PlayerEntity
 *  net.minecraft.Box
 *  net.minecraft.Vec3d
 *  net.minecraft.MinecraftClient
 *  net.minecraft.MatrixStack
 *  net.minecraft.VertexConsumer
 *  net.minecraft.ClientPlayerEntity
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.config.Config;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.network.ClientPlayerEntity;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public class ApproachSupportLine {
    private static final double DEG_TO_RAD = Math.PI / 180;
    private static Vec3d targetCenter;
    private static Vec3d selfPos;

    public static void tick(ClientPlayerEntity clientPlayer, Entity target) {
        targetCenter = target.getBoundingBox().getCenter();
        selfPos = clientPlayer.getEntityPos().add(0.0, (double)clientPlayer.getStandingEyeHeight(), 0.0);
    }

    public static void drawFanLines(MatrixStack matrices, VertexConsumer buffer) {
        double segmentLength = 10.0;
        MinecraftClient client = MinecraftClient.getInstance();
        Vec3d camPos = client.gameRenderer.getCamera().getCameraPos();
        for (int i = 0; i < 2; ++i) {
            Vec3d toPlayer = selfPos.subtract(targetCenter);
            double totalLength = toPlayer.length();
            double theta = Math.toRadians(i == 0 ? 60.0 : 70.0);
            Vec3d horizontalDir = new Vec3d(toPlayer.x, 0.0, toPlayer.z).normalize();
            double y = totalLength * Math.sin(theta);
            double horizontalLength = totalLength * Math.cos(theta);
            Vec3d baseEnd = targetCenter.add(horizontalDir.multiply(horizontalLength)).add(0.0, y, 0.0);
            Vec3d line1Dir = ApproachSupportLine.rotateAroundAxis(baseEnd.subtract(targetCenter).normalize(), new Vec3d(0.0, 1.0, 0.0), 0.08726646259971647);
            Vec3d line2Dir = ApproachSupportLine.rotateAroundAxis(baseEnd.subtract(targetCenter).normalize(), new Vec3d(0.0, 1.0, 0.0), -0.08726646259971647);
            Vec3d start = targetCenter.subtract(camPos);
            ApproachSupportLine.drawSegmentedLine(matrices, buffer, start, targetCenter.add(line1Dir.multiply(totalLength)).subtract(camPos), segmentLength);
            ApproachSupportLine.drawSegmentedLine(matrices, buffer, start, targetCenter.add(line2Dir.multiply(totalLength)).subtract(camPos), segmentLength);
        }
    }

    private static void drawSegmentedLine(MatrixStack matrices, VertexConsumer buffer, Vec3d start, Vec3d end, double segmentLength) {
        Vec3d delta = end.subtract(start);
        double length = delta.length();
        Vec3d dir = delta.normalize();
        int segments = (int)Math.ceil(length / segmentLength);
        for (int i = 0; i < segments; ++i) {
            double t0 = (double)i * segmentLength;
            double t1 = Math.min((double)(i + 1) * segmentLength, length);
            Vec3d segStart = start.add(dir.multiply(t0));
            Vec3d segEnd = start.add(dir.multiply(t1));
            float r = i % 3 == 0 ? 1.0f : 0.0f;
            float g = i % 3 == 1 ? 1.0f : 0.0f;
            float b = i % 3 == 2 ? 1.0f : 0.0f;
            ApproachSupportLine.drawLine(matrices, buffer, segStart, segEnd, r, g, b);
        }
    }

    private static void drawLine(MatrixStack matrices, VertexConsumer buffer, Vec3d start, Vec3d end, float r, float g, float b) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        Vec3d dir = end.subtract(start).normalize();
        buffer.vertex((Matrix4fc)matrix, (float)start.x, (float)start.y, (float)start.z).color(r, g, b, 0.4f).normal((float)dir.x, (float)dir.y, (float)dir.z);
        buffer.vertex((Matrix4fc)matrix, (float)end.x, (float)end.y, (float)end.z).color(r, g, b, 0.4f).normal((float)dir.x, (float)dir.y, (float)dir.z);
    }

    private static Vec3d rotateAroundAxis(Vec3d v, Vec3d axis, double angleRad) {
        axis = axis.normalize();
        double cos = Math.cos(angleRad);
        double sin = Math.sin(angleRad);
        Vec3d term1 = v.multiply(cos);
        Vec3d term2 = axis.crossProduct(v).multiply(sin);
        Vec3d term3 = axis.multiply(axis.dotProduct(v) * (1.0 - cos));
        return term1.add(term2).add(term3);
    }

    public static PlayerEntity findNearestPlayer(MinecraftClient client, ClientPlayerEntity clientPlayer, int rangeXZ, int rangeY) {
        double downDistance = Math.max(50.0, (double)Config.RADAR_DOWNWARD);
        double closestDistance = Double.MAX_VALUE;
        PlayerEntity nearestPlayer = null;
        Vec3d playerEyePos = clientPlayer.getEyePos();
        Vec3d playerPos = clientPlayer.getEntityPos();
        Box pBox = clientPlayer.getBoundingBox();
        Vec3d start = new Vec3d(pBox.minX, playerEyePos.getY() + 3.0, pBox.minZ);
        Vec3d end = new Vec3d(pBox.maxX, playerEyePos.getY() - downDistance, pBox.maxZ);
        Box searchArea = new Box(start, end).expand((double)rangeXZ, (double)rangeY, (double)rangeXZ);
        if (client.world != null) {
            List<Entity> nearbyEntities = client.world.getOtherEntities((Entity)clientPlayer, searchArea, entity -> {
                PlayerEntity target;
                return entity instanceof PlayerEntity && (target = (PlayerEntity)entity).isOnGround();
            });
            for (Entity entity2 : nearbyEntities) {
                double distance = playerPos.distanceTo(entity2.getEntityPos());
                if (!(entity2 instanceof PlayerEntity)) continue;
                PlayerEntity playerEntity = (PlayerEntity)entity2;
                if (!(distance < closestDistance)) continue;
                closestDistance = distance;
                nearestPlayer = playerEntity;
            }
        }
        return nearestPlayer;
    }
}
