/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.client.CameraType
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleEngine
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.world.phys.Vec3
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.config.Config;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

@Environment(value=EnvType.CLIENT)
public class MaceParticle {
    private static final List<SimpleParticleType> particleList = new ArrayList<SimpleParticleType>(Arrays.asList(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS, ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER));

    public static void maceParticleHandler(Minecraft client, LocalPlayer player, boolean overThreshold) {
        SimpleParticleType particleType = Config.MACE_PARTICLE == Config.WeaponParticle.Transition ? (Config.PARTICLE_ORDER == Config.TransitionOrder.Blue_Red ? (overThreshold ? particleList.get(1) : particleList.get(0)) : (overThreshold ? particleList.get(0) : particleList.get(1))) : particleList.get(Config.MACE_PARTICLE == Config.WeaponParticle.Blue ? 0 : 1);
        MaceParticle.weaponEmitsParticles(client, player, 0.2, 3, particleType);
    }

    public static void weaponEmitsParticles(Minecraft client, LocalPlayer player, double radius, int count, SimpleParticleType particleType) {
        ParticleEngine particleManager = client.particleEngine;
        Vec3 weaponPosition = MaceParticle.getWeaponPosition(client, player);
        for (int i = 0; i < count; ++i) {
            double x = weaponPosition.x + (ThreadLocalRandom.current().nextDouble() * 2.0 - 1.0) * radius;
            double y = weaponPosition.y + (ThreadLocalRandom.current().nextDouble() * 2.0 - 1.0) * radius;
            double z = weaponPosition.z + (ThreadLocalRandom.current().nextDouble() * 2.0 - 1.0) * radius;
            Vec3 startPosition = new Vec3(x, y, z);
            Vec3 direction = new Vec3(weaponPosition.x, weaponPosition.y, weaponPosition.z).subtract(startPosition).normalize();
            double particleSpeed = 0.1;
            Particle particle = particleManager.createParticle((ParticleOptions)particleType, startPosition.x, startPosition.y, startPosition.z, direction.x * particleSpeed, direction.y * particleSpeed, direction.z * particleSpeed);
            if (particle == null) continue;
            client.execute(() -> particleManager.add(particle));
        }
    }

    private static Vec3 getWeaponPosition(Minecraft client, LocalPlayer player) {
        double distance;
        double yaw;
        double pitch;
        Vec3 playerPos;
        if (client.options.getCameraType().equals((Object)CameraType.FIRST_PERSON)) {
            playerPos = player.getEyePosition();
            pitch = player.getXRot() * -1.0f;
            yaw = player.getYRot() + 90.0f + 30.0f;
            distance = 0.8;
        } else {
            playerPos = player.getBoundingBox().getCenter();
            pitch = 0.0;
            yaw = player.getVisualRotationYInDegrees() + 90.0f + 30.0f;
            distance = 0.6;
        }
        return playerPos.add(MaceParticle.getOffset(pitch, yaw, distance));
    }

    private static Vec3 getOffset(double pitch, double yaw, double distance) {
        pitch = Math.toRadians(pitch);
        yaw = Math.toRadians(yaw);
        double offsetX = Math.cos(pitch) * Math.cos(yaw);
        double offsetY = Math.sin(pitch);
        double offsetZ = Math.cos(pitch) * Math.sin(yaw);
        return new Vec3(offsetX * distance, offsetY * distance, offsetZ * distance);
    }
}
