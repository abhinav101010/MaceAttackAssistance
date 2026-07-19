/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
 *  net.minecraft.entity.passive.PassiveEntity
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.mob.AmbientEntity
 *  net.minecraft.entity.passive.IronGolemEntity
 *  net.minecraft.entity.mob.WaterCreatureEntity
 *  net.minecraft.entity.mob.Monster
 *  net.minecraft.entity.mob.HostileEntity
 *  net.minecraft.entity.passive.VillagerEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.world.World
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.render.Camera
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.util.math.MatrixStack$class_4665
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.VertexConsumerProvider$class_4598
 *  net.minecraft.client.render.OverlayTexture
 *  net.minecraft.util.math.ColorHelper$class_5254
 *  net.minecraft.entity.mob.WardenEntity
 *  net.minecraft.client.network.ClientPlayerEntity
 *  org.joml.Matrix4f
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.world.World;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class BeamRenderHandler {
    private static final Identifier GLOW_TEXTURE = Identifier.of((String)"maceattackassistance", (String)"textures/glow_particle.png");
    private static final Map<UUID, List<Deque<Vector3f>>> spiralTrailsPerEntity = new HashMap<UUID, List<Deque<Vector3f>>>();
    private static final Map<UUID, Long> entityTrailLastUpdate = new HashMap<UUID, Long>();
    private static final long CLEANUP_INTERVAL_MS = 5000L;
    private static final long TRAIL_TIMEOUT_MS = 10000L;
    private static long lastCleanupTime = 0L;
    public static List<Entity> targetList = new ArrayList<Entity>();

    public static void clearTargetList() {
        targetList.clear();
    }

    public static void register() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            MatrixStack matrices;
            if (targetList.isEmpty()) {
                return;
            }
            if (Config.MARKER_TYPE == Config.MarkerType.Frame) {
                return;
            }
            if (!Config.TARGET_SEARCH_MODE && !Config.DEBUG_SCREEN) {
                if (!Config.AIM_ASSIST && Config.HIDE_MARKER) {
                    return;
                }
                if (!Config.TARGET_MARKER) {
                    return;
                }
            }
            if ((matrices = context.matrixStack()) == null) {
                return;
            }
            VertexConsumerProvider.class_4598 immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
            Camera camera = context.camera();
            for (Entity target : targetList) {
                BeamRenderHandler.particleController(matrices, (VertexConsumerProvider)immediate, camera, target);
            }
            immediate.draw();
        });
    }

    public static List<Entity> getWorldEntityList(MinecraftClient client, ClientPlayerEntity clientPlayer, World world) {
        if (!world.isClient()) {
            return null;
        }
        double upRange = Config.RADAR_UPWARD * 10;
        double downRange = Config.RADAR_DOWNWARD * 10;
        double horiRange = Config.RADAR_HORIZONTAL * 10;
        Vec3d playerEyePos = clientPlayer.getEyePos();
        float playerPitch = clientPlayer.getPitch();
        float playerYaw = clientPlayer.getYaw();
        float yawFovDegrees = (Config.ZOOM_CAMERA ? Config.FOV_HORIZONTAL_ON_ZOOM : Config.FOV_HORIZONTAL) * 10;
        float pitchFovDegrees = (Config.ZOOM_CAMERA ? Config.FOV_VERTICAL_ON_ZOOM : Config.FOV_VERTICAL) * 10;
        Box pBox = clientPlayer.getBoundingBox();
        Vec3d start = new Vec3d(pBox.minX, playerEyePos.getY() + upRange, pBox.minZ);
        Vec3d end = new Vec3d(pBox.maxX, playerEyePos.getY() - downRange, pBox.maxZ);
        Box searchArea = new Box(start, end).expand(horiRange, 0.0, horiRange);
        List<Object> worldEntities = new ArrayList<Entity>();
        if (client.world != null) {
            worldEntities = client.world.getOtherEntities((Entity)clientPlayer, searchArea, entity -> entity.isLiving() && entity.isAlive() && MaceAttackAssistanceClient.isAllowedTarget(entity) && !BeamRenderHandler.isOutsideViewCone(playerEyePos, entity.getPos(), playerYaw, playerPitch, yawFovDegrees, pitchFovDegrees) && MaceAttackAssistanceClient.isSimpleVisibleFromPlayer(client, clientPlayer, entity));
        }
        return worldEntities;
    }

    private static void particleController(MatrixStack matrices, VertexConsumerProvider consumers, Camera camera, Entity targetEntity) {
        double yOffset = 0.5 * (double)Config.MARKER_OFFSET + (double)(Config.MARKER_TYPE == Config.MarkerType.Beam ? targetEntity.getHeight() : 0.0f);
        Vec3d entityPos = Utils.getTargetPos(targetEntity, false).add(0.0, yOffset, 0.0);
        Vec3d relPos = entityPos.subtract(camera.getPos());
        matrices.push();
        matrices.translate(relPos.x, relPos.y, relPos.z);
        switch (Config.MARKER_TYPE) {
            case Beam: {
                BeamRenderHandler.renderBeam(matrices, consumers, BeamRenderHandler.getColor(targetEntity));
                break;
            }
            case Spiral: {
                BeamRenderHandler.renderSpiralParticlesTrail(matrices, consumers, BeamRenderHandler.getColor(targetEntity), targetEntity);
            }
        }
        matrices.pop();
    }

    private static void renderBeam(MatrixStack matrices, VertexConsumerProvider consumers, int targetColor) {
        float r = (float)ColorHelper.class_5254.getRed((int)targetColor) / 255.0f;
        float g = (float)ColorHelper.class_5254.getGreen((int)targetColor) / 255.0f;
        float b = (float)ColorHelper.class_5254.getBlue((int)targetColor) / 255.0f;
        float alpha = 1.0f;
        float height = 2.5f;
        float width = 0.1f;
        MatrixStack.class_4665 entry = matrices.peek();
        Matrix4f model = entry.getPositionMatrix();
        VertexConsumer vc = consumers.getBuffer(RenderLayer.getLightning());
        for (int i = 0; i < 4; ++i) {
            float angle1 = (float)(Math.PI * 2 * (double)i / 4.0);
            float angle2 = (float)(Math.PI * 2 * (double)(i + 1) / 4.0);
            float x1 = (float)Math.cos(angle1) * width;
            float z1 = (float)Math.sin(angle1) * width;
            float x2 = (float)Math.cos(angle2) * width;
            float z2 = (float)Math.sin(angle2) * width;
            vc.vertex(model, x1, 0.0f, z1).color(r, g, b, alpha).normal(0.0f, 1.0f, 0.0f);
            vc.vertex(model, x2, 0.0f, z2).color(r, g, b, alpha).normal(0.0f, 1.0f, 0.0f);
            vc.vertex(model, x2, height, z2).color(r, g, b, 0.0f).normal(0.0f, 1.0f, 0.0f);
            vc.vertex(model, x1, height, z1).color(r, g, b, 0.0f).normal(0.0f, 1.0f, 0.0f);
        }
    }

    private static void renderSpiralParticlesTrail(MatrixStack matrices, VertexConsumerProvider consumers, int targetColor, Entity targetEntity) {
        float r = (float)ColorHelper.class_5254.getRed((int)targetColor) / 255.0f;
        float g = (float)ColorHelper.class_5254.getGreen((int)targetColor) / 255.0f;
        float b = (float)ColorHelper.class_5254.getBlue((int)targetColor) / 255.0f;
        VertexConsumer vc = consumers.getBuffer(RenderLayer.getEntityTranslucentEmissive((Identifier)GLOW_TEXTURE));
        int spiralCount = Config.SP_SPIRAL_COUNT;
        int trailLength = Config.SP_SPIRAL_LENGTH;
        float coils = Config.SP_COILS;
        float height = (float)Config.SP_HEIGHT * 0.5f;
        float size = (float)Config.SP_SIZE * 0.1f;
        float speed = (float)Config.SP_SPEED * 0.1f;
        float baseRadius = (float)Config.SP_BASE_RADIUS * 0.1f;
        float waveSpeed = (float)Config.SP_WAVE_SPEED * 0.1f;
        float waveAmplitude = (float)Config.SP_WAVE_AMPLITUDE * 0.1f;
        double timeSeconds = (double)System.nanoTime() / 1.0E9;
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        Quaternionf rotation = camera.getRotation();
        Vector3f cameraRight = new Vector3f(1.0f, 0.0f, 0.0f).rotate((Quaternionfc)rotation);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f).rotate((Quaternionfc)rotation);
        Matrix4f modelMatrix = matrices.peek().getPositionMatrix();
        UUID id = targetEntity.getUuid();
        List trails = spiralTrailsPerEntity.computeIfAbsent(id, k -> {
            ArrayList list = new ArrayList();
            for (int i = 0; i < spiralCount; ++i) {
                list.add(new LinkedList());
            }
            return list;
        });
        if (trails.size() != spiralCount) {
            trails.clear();
            for (int i = 0; i < spiralCount; ++i) {
                trails.add(new LinkedList());
            }
        }
        entityTrailLastUpdate.put(id, System.currentTimeMillis());
        for (int s = 0; s < spiralCount; ++s) {
            float angleOffset = (float)(Math.PI * 2 * (double)s / (double)spiralCount);
            float phaseOffset = (float)s / (float)spiralCount;
            float dynamicRadius = baseRadius + (float)Math.sin(timeSeconds * (double)waveSpeed) * waveAmplitude;
            double progress = (timeSeconds * (double)speed + (double)phaseOffset) % 1.0;
            float angle = (float)(Math.PI * 2 * (double)coils * progress) + angleOffset;
            float y = (float)(progress * (double)height);
            float x = (float)Math.cos(angle) * dynamicRadius;
            float z = (float)Math.sin(angle) * dynamicRadius;
            Vector3f newPos = new Vector3f(x, y, z);
            Deque trail = (Deque)trails.get(s);
            trail.addFirst(newPos);
            while (trail.size() > trailLength) {
                trail.removeLast();
            }
            int i = 0;
            for (Vector3f center : trail) {
                float fade = (float)i++ / (float)trail.size();
                fade = (float)Math.pow(fade, (float)Config.SP_SPIRAL_GAMMA * 0.1f);
                float alpha = (1.0f - fade) * ((float)Config.SP_SPIRAL_ALPHA * 0.1f);
                float particleSize = size * (1.0f - fade);
                float halfSize = particleSize / 2.0f;
                Vector3f right = new Vector3f((Vector3fc)cameraRight).mul(halfSize);
                Vector3f up = new Vector3f((Vector3fc)cameraUp).mul(halfSize);
                Vector3f p1 = new Vector3f((Vector3fc)center).sub((Vector3fc)right).sub((Vector3fc)up);
                Vector3f p2 = new Vector3f((Vector3fc)center).add((Vector3fc)right).sub((Vector3fc)up);
                Vector3f p3 = new Vector3f((Vector3fc)center).add((Vector3fc)right).add((Vector3fc)up);
                Vector3f p4 = new Vector3f((Vector3fc)center).sub((Vector3fc)right).add((Vector3fc)up);
                vc.vertex(modelMatrix, p1.x, p1.y, p1.z).color(r, g, b, alpha).texture(0.0f, 0.0f).light(0xF000F0).overlay(OverlayTexture.DEFAULT_UV).normal(0.0f, 1.0f, 0.0f);
                vc.vertex(modelMatrix, p2.x, p2.y, p2.z).color(r, g, b, alpha).texture(1.0f, 0.0f).light(0xF000F0).overlay(OverlayTexture.DEFAULT_UV).normal(0.0f, 1.0f, 0.0f);
                vc.vertex(modelMatrix, p3.x, p3.y, p3.z).color(r, g, b, 0.0f).texture(1.0f, 1.0f).light(0xF000F0).overlay(OverlayTexture.DEFAULT_UV).normal(0.0f, 1.0f, 0.0f);
                vc.vertex(modelMatrix, p4.x, p4.y, p4.z).color(r, g, b, 0.0f).texture(0.0f, 1.0f).light(0xF000F0).overlay(OverlayTexture.DEFAULT_UV).normal(0.0f, 1.0f, 0.0f);
            }
        }
        long now = System.currentTimeMillis();
        if (now - lastCleanupTime > 5000L) {
            lastCleanupTime = now;
            spiralTrailsPerEntity.keySet().removeIf(uuid -> !entityTrailLastUpdate.containsKey(uuid) || now - entityTrailLastUpdate.get(uuid) > 10000L);
            entityTrailLastUpdate.keySet().removeIf(uuid -> !spiralTrailsPerEntity.containsKey(uuid));
        }
    }

    private static int getColor(Entity target) {
        if (target instanceof VillagerEntity && Config.ALLOWED_VILLAGER) {
            return Config.COLOR_VILLAGER.getValue();
        }
        if (target instanceof IronGolemEntity && Config.ALLOWED_IRON_GOLEM) {
            return Config.COLOR_IRON_GOLEM.getValue();
        }
        if (target instanceof WardenEntity && Config.ALLOWED_HOSTILE) {
            return Config.COLOR_WARDEN.getValue();
        }
        if (target instanceof HostileEntity && Config.ALLOWED_HOSTILE) {
            return Config.COLOR_HOSTILE.getValue();
        }
        if (target instanceof Monster && Config.ALLOWED_HOSTILE) {
            return Config.COLOR_HOSTILE.getValue();
        }
        if (target instanceof PassiveEntity && Config.ALLOWED_PASSIVE) {
            return Config.COLOR_PASSIVE.getValue();
        }
        if (target instanceof AmbientEntity && Config.ALLOWED_AMBIENT) {
            return Config.COLOR_AMBIENT.getValue();
        }
        if (target instanceof WaterCreatureEntity && Config.ALLOWED_AMBIENT) {
            return Config.COLOR_AMBIENT.getValue();
        }
        if (target instanceof PlayerEntity && Config.ALLOWED_PLAYER) {
            return Config.COLOR_PLAYER.getValue();
        }
        return Config.ALLOWED_AMBIENT ? Config.COLOR_AMBIENT.getValue() : 0;
    }

    public static boolean isOutsideViewCone(Vec3d playerPos, Vec3d targetPos, float yawDegrees, float pitchDegrees, float yawFovDegrees, float pitchFovDegrees) {
        double yawRad = Math.toRadians(-yawDegrees);
        double pitchRad = Math.toRadians(-pitchDegrees);
        Vec3d lookVec = new Vec3d(Math.cos(pitchRad) * Math.sin(yawRad), Math.sin(pitchRad), Math.cos(pitchRad) * Math.cos(yawRad)).normalize();
        Vec3d toTarget = targetPos.subtract(playerPos).normalize();
        double yawDot = new Vec3d(lookVec.x, 0.0, lookVec.z).normalize().dotProduct(new Vec3d(toTarget.x, 0.0, toTarget.z).normalize());
        double yawAngleDeg = Math.toDegrees(Math.acos(yawDot));
        double pitchDot = lookVec.dotProduct(toTarget);
        double pitchAngleDeg = Math.toDegrees(Math.acos(pitchDot)) - yawAngleDeg;
        return yawAngleDeg > (double)yawFovDegrees / 2.0 || Math.abs(pitchAngleDeg) > (double)pitchFovDegrees / 2.0;
    }
}
