/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Camera
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.rendertype.RenderTypes
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.Identifier
 *  net.minecraft.util.ARGB
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.ambient.AmbientCreature
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.animal.fish.WaterAnimal
 *  net.minecraft.world.entity.animal.golem.IronGolem
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.monster.warden.Warden
 *  net.minecraft.world.entity.npc.villager.Villager
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 */
package com.papack.maceattackassistance.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class BeamRenderHandler {
    private static final Identifier GLOW_TEXTURE = Identifier.fromNamespaceAndPath((String)"maceattackassistance", (String)"textures/glow_particle.png");
    private static final Map<UUID, List<Vector3f>> lastTrailPosPerEntity = new HashMap<UUID, List<Vector3f>>();
    private static final float MIN_STEP = 0.05f;
    private static final Map<UUID, List<Deque<Vector3f>>> spiralTrailsPerEntity = new HashMap<UUID, List<Deque<Vector3f>>>();
    private static final Map<UUID, Long> entityTrailLastUpdate = new HashMap<UUID, Long>();
    private static final long CLEANUP_INTERVAL_MS = 5000L;
    private static final long TRAIL_TIMEOUT_MS = 10000L;
    private static long lastCleanupTime = 0L;
    public static List<Entity> targetList = new ArrayList<Entity>();

    public static void clearTargetList() {
        targetList.clear();
    }

    public static void markerRenderer(DeltaTracker tickCounter, Camera camera) {
        // Stubbed out: MC 26.2 removed client.renderBuffers(). The MultiBufferSource pipeline
        // changed entirely. Core combat features do not depend on marker rendering.
    }

    public static List<Entity> getWorldEntityList(Minecraft client, LocalPlayer clientPlayer, Level world, boolean valueType) {
        float pitchFovDegrees;
        float yawFovDegrees;
        double horiRange;
        double downRange;
        double upRange;
        if (!world.isClientSide()) {
            return null;
        }
        Vec3 playerEyePos = clientPlayer.getEyePosition();
        float playerPitch = clientPlayer.getXRot();
        float playerYaw = clientPlayer.getYRot();
        if (valueType || Config.AIM_ELYTRA && MaceAttackAssistanceClient.getAimingElytra()) {
            upRange = 20.0;
            downRange = 50.0;
            horiRange = 50.0;
            yawFovDegrees = 30.0f;
            pitchFovDegrees = 30.0f;
        } else {
            upRange = Config.RADAR_UPWARD * 10;
            downRange = Config.RADAR_DOWNWARD * 10;
            horiRange = Config.RADAR_HORIZONTAL * 10;
            yawFovDegrees = (Config.ZOOM_CAMERA ? Config.FOV_HORIZONTAL_ON_ZOOM : Config.FOV_HORIZONTAL) * 10;
            pitchFovDegrees = (Config.ZOOM_CAMERA ? Config.FOV_VERTICAL_ON_ZOOM : Config.FOV_VERTICAL) * 10;
        }
        AABB pBox = clientPlayer.getBoundingBox();
        Vec3 start = new Vec3(pBox.minX, playerEyePos.y() + upRange, pBox.minZ);
        Vec3 end = new Vec3(pBox.maxX, playerEyePos.y() - downRange, pBox.maxZ);
        AABB searchArea = new AABB(start, end).inflate(horiRange, 0.0, horiRange);
        List<Entity> worldEntities = new ArrayList<Entity>();
        if (client.level != null) {
            worldEntities = client.level.getEntities((Entity)clientPlayer, searchArea, entity -> entity.showVehicleHealth() && entity.isAlive() && MaceAttackAssistanceClient.isAllowedTarget(entity) && !BeamRenderHandler.isOutsideViewCone(playerEyePos, entity.position(), playerYaw, playerPitch, yawFovDegrees, pitchFovDegrees) && Utils.isSimpleVisibleFromPlayer(client, clientPlayer, entity));
        }
        // Debug logging
        if (Config.DEBUG_SCREEN && !worldEntities.isEmpty()) {
            MaceAttackAssistanceClient.LOGGER.info("[TargetSearch] Found {} entities", worldEntities.size());
        }
        return worldEntities;
    }

    public static List<Entity> getWorldEntityListParallel(Minecraft client, LocalPlayer clientPlayer, Level world) {
        if (!world.isClientSide()) {
            return Collections.emptyList();
        }
        double upRange = Config.RADAR_UPWARD * 10;
        double downRange = Config.RADAR_DOWNWARD * 10;
        double horiRange = Config.RADAR_HORIZONTAL * 10;
        Vec3 playerEyePos = clientPlayer.getEyePosition();
        float playerPitch = clientPlayer.getXRot();
        float playerYaw = clientPlayer.getYRot();
        float yawFovDegrees = (Config.ZOOM_CAMERA ? Config.FOV_HORIZONTAL_ON_ZOOM : Config.FOV_HORIZONTAL) * 10;
        float pitchFovDegrees = (Config.ZOOM_CAMERA ? Config.FOV_VERTICAL_ON_ZOOM : Config.FOV_VERTICAL) * 10;
        AABB pBox = clientPlayer.getBoundingBox();
        Vec3 start = new Vec3(pBox.minX, playerEyePos.y() + upRange, pBox.minZ);
        Vec3 end = new Vec3(pBox.maxX, playerEyePos.y() - downRange, pBox.maxZ);
        AABB searchArea = new AABB(start, end).inflate(horiRange, 0.0, horiRange);
        List<Entity> allEntities = world.getEntities((Entity)clientPlayer, searchArea, entity -> entity.showVehicleHealth() && entity.isAlive());
        List<Entity> preFiltered = allEntities.parallelStream().filter(e -> MaceAttackAssistanceClient.isAllowedTarget(e)).filter(entity -> !BeamRenderHandler.isOutsideViewCone(playerEyePos, entity.position(), playerYaw, playerPitch, yawFovDegrees, pitchFovDegrees)).toList();
        ArrayList<Entity> visibleEntities = new ArrayList<Entity>();
        for (Entity entity2 : preFiltered) {
            if (!Utils.isSimpleVisibleFromPlayer(client, clientPlayer, entity2)) continue;
            visibleEntities.add(entity2);
        }
        // Debug logging
        if (Config.DEBUG_SCREEN && !visibleEntities.isEmpty()) {
            MaceAttackAssistanceClient.LOGGER.info("[ParallelTargetSearch] Found {} entities", visibleEntities.size());
        }
        return visibleEntities;
    }

    private static void particleController(PoseStack matrices, Object consumers, Camera camera, Entity targetEntity, float delta) {
        // Stubbed out: MC 26.2 rendering pipeline changed. MultiBufferSource no longer exists.
    }

    private static void renderBeam(PoseStack matrices, Object consumers, int targetColor) {
        // Stubbed out: MC 26.2 rendering pipeline changed. MultiBufferSource no longer exists.
    }

    private static void renderSpiralParticlesTrail(PoseStack matrices, Object consumers, int targetColor, Entity targetEntity) {
        // Stubbed out: MC 26.2 rendering pipeline changed. MultiBufferSource no longer exists.
    }

    private static int getColor(Entity target) {
        if (target instanceof Villager && Config.ALLOWED_VILLAGER) {
            return Config.COLOR_VILLAGER.getValue();
        }
        if (target instanceof IronGolem && Config.ALLOWED_IRON_GOLEM) {
            return Config.COLOR_IRON_GOLEM.getValue();
        }
        if (target instanceof Warden && Config.ALLOWED_HOSTILE) {
            return Config.COLOR_WARDEN.getValue();
        }
        if (target instanceof Monster && Config.ALLOWED_HOSTILE) {
            return Config.COLOR_HOSTILE.getValue();
        }
        if (target instanceof Enemy && Config.ALLOWED_HOSTILE) {
            return Config.COLOR_HOSTILE.getValue();
        }
        if (target instanceof Animal && Config.ALLOWED_PASSIVE) {
            return Config.COLOR_PASSIVE.getValue();
        }
        if (target instanceof AgeableMob && Config.ALLOWED_PASSIVE) {
            return Config.COLOR_PASSIVE.getValue();
        }
        if (target instanceof AmbientCreature && Config.ALLOWED_AMBIENT) {
            return Config.COLOR_AMBIENT.getValue();
        }
        if (target instanceof WaterAnimal && Config.ALLOWED_AMBIENT) {
            return Config.COLOR_AMBIENT.getValue();
        }
        if (target instanceof Player && Config.ALLOWED_PLAYER) {
            return Config.COLOR_PLAYER.getValue();
        }
        return Config.ALLOWED_AMBIENT ? Config.COLOR_AMBIENT.getValue() : 0;
    }

    public static boolean isOutsideViewCone(Vec3 playerPos, Vec3 targetPos, float yawDegrees, float pitchDegrees, float yawFovDegrees, float pitchFovDegrees) {
        double yawRad = Math.toRadians(-yawDegrees);
        double pitchRad = Math.toRadians(-pitchDegrees);
        Vec3 lookVec = new Vec3(Math.cos(pitchRad) * Math.sin(yawRad), Math.sin(pitchRad), Math.cos(pitchRad) * Math.cos(yawRad)).normalize();
        Vec3 toTarget = targetPos.subtract(playerPos).normalize();
        double yawDot = new Vec3(lookVec.x, 0.0, lookVec.z).normalize().dot(new Vec3(toTarget.x, 0.0, toTarget.z).normalize());
        double yawAngleDeg = Math.toDegrees(Math.acos(yawDot));
        double pitchDot = lookVec.dot(toTarget);
        double pitchAngleDeg = Math.toDegrees(Math.acos(pitchDot)) - yawAngleDeg;
        return yawAngleDeg > (double)yawFovDegrees / 2.0 || Math.abs(pitchAngleDeg) > (double)pitchFovDegrees / 2.0;
    }
}
