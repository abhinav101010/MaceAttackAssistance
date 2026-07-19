/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.particle.ParticleEffect
 *  net.minecraft.particle.ParticleTypes
 *  net.minecraft.particle.SimpleParticleType
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.option.Perspective
 *  net.minecraft.client.particle.ParticleManager
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.network.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.config.Config;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.network.ClientPlayerEntity;

@Environment(value=EnvType.CLIENT)
public class MaceParticle {
    private static final List<SimpleParticleType> particleList = new ArrayList<SimpleParticleType>(Arrays.asList(ParticleTypes.TRIAL_SPAWNER_DETECTION_OMINOUS, ParticleTypes.TRIAL_SPAWNER_DETECTION));

    public static void maceParticleHandler(MinecraftClient client, ClientPlayerEntity player, boolean overThreshold) {
        SimpleParticleType particleType = Config.MACE_PARTICLE == Config.WeaponParticle.Transition ? (Config.PARTICLE_ORDER == Config.TransitionOrder.Blue_Red ? (overThreshold ? particleList.get(1) : particleList.get(0)) : (overThreshold ? particleList.get(0) : particleList.get(1))) : particleList.get(Config.MACE_PARTICLE == Config.WeaponParticle.Blue ? 0 : 1);
        MaceParticle.weaponEmitsParticles(client, player, 0.2, 3, particleType);
    }

    public static void weaponEmitsParticles(MinecraftClient client, ClientPlayerEntity player, double radius, int count, SimpleParticleType particleType) {
        ParticleManager particleManager = client.particleManager;
        Vec3d weaponPosition = MaceParticle.getWeaponPosition(client, player);
        for (int i = 0; i < count; ++i) {
            double x = weaponPosition.x + (ThreadLocalRandom.current().nextDouble() * 2.0 - 1.0) * radius;
            double y = weaponPosition.y + (ThreadLocalRandom.current().nextDouble() * 2.0 - 1.0) * radius;
            double z = weaponPosition.z + (ThreadLocalRandom.current().nextDouble() * 2.0 - 1.0) * radius;
            Vec3d startPosition = new Vec3d(x, y, z);
            Vec3d direction = new Vec3d(weaponPosition.x, weaponPosition.y, weaponPosition.z).subtract(startPosition).normalize();
            double particleSpeed = 0.1;
            Particle particle = particleManager.addParticle((ParticleEffect)particleType, startPosition.x, startPosition.y, startPosition.z, direction.x * particleSpeed, direction.y * particleSpeed, direction.z * particleSpeed);
            if (particle == null) continue;
            client.execute(() -> particleManager.addParticle(particle));
        }
    }

    private static Vec3d getWeaponPosition(MinecraftClient client, ClientPlayerEntity player) {
        double distance;
        double yaw;
        double pitch;
        Vec3d playerPos;
        if (client.options.getPerspective().equals(Perspective.FIRST_PERSON)) {
            playerPos = player.getEyePos();
            pitch = player.getPitch() * -1.0f;
            yaw = player.getYaw() + 90.0f + 30.0f;
            distance = 0.8;
        } else {
            playerPos = player.getBoundingBox().getCenter();
            pitch = 0.0;
            yaw = player.getBodyYaw() + 90.0f + 30.0f;
            distance = 0.6;
        }
        return playerPos.add(MaceParticle.getOffset(pitch, yaw, distance));
    }

    private static Vec3d getOffset(double pitch, double yaw, double distance) {
        pitch = Math.toRadians(pitch);
        yaw = Math.toRadians(yaw);
        double offsetX = Math.cos(pitch) * Math.cos(yaw);
        double offsetY = Math.sin(pitch);
        double offsetZ = Math.cos(pitch) * Math.sin(yaw);
        return new Vec3d(offsetX * distance, offsetY * distance, offsetZ * distance);
    }
}
