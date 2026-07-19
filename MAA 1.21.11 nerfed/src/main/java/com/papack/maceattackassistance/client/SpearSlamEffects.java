package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.config.Config;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.network.ClientPlayerEntity;

@Environment(value=EnvType.CLIENT)
public class SpearSlamEffects {

    public static void playSlamEffect(MinecraftClient client, ClientPlayerEntity player, Entity target) {
        if (Config.SPEAR_SLAM_PARTICLES) {
            spawnSlamParticles(client, player, target);
        }
        if (Config.SPEAR_SLAM_SOUND) {
            playSlamSound(client, player);
        }
    }

    public static void spawnSlamParticles(MinecraftClient client, ClientPlayerEntity player, Entity target) {
        ParticleManager particleManager = client.particleManager;
        Vec3d centerPos;
        if (target != null) {
            centerPos = target.getEntityPos().add(0.0, target.getHeight() / 2.0, 0.0);
        } else {
            centerPos = player.getEntityPos().add(0.0, player.getHeight() / 2.0, 0.0);
        }

        for (int i = 0; i < 12; ++i) {
            double spread = 0.5;
            double x = centerPos.x + (ThreadLocalRandom.current().nextDouble() - 0.5) * spread;
            double y = centerPos.y + (ThreadLocalRandom.current().nextDouble() - 0.5) * spread;
            double z = centerPos.z + (ThreadLocalRandom.current().nextDouble() - 0.5) * spread;
            particleManager.addParticle(
                ParticleTypes.LARGE_SMOKE,
                x, y, z,
                0.0, 0.05, 0.0
            );
        }

        for (int i = 0; i < 6; ++i) {
            double spread = 0.3;
            double x = centerPos.x + (ThreadLocalRandom.current().nextDouble() - 0.5) * spread;
            double y = centerPos.y + (ThreadLocalRandom.current().nextDouble() - 0.5) * spread;
            double z = centerPos.z + (ThreadLocalRandom.current().nextDouble() - 0.5) * spread;
            particleManager.addParticle(
                ParticleTypes.CRIT,
                x, y, z,
                0.0, 0.1, 0.0
            );
        }
    }

    private static void playSlamSound(MinecraftClient client, ClientPlayerEntity player) {
        if (client.world != null) {
            client.world.playSound(
                player,
                player.getBlockPos(),
                SoundEvents.ITEM_MACE_SMASH_GROUND,
                SoundCategory.PLAYERS,
                1.0f,
                1.2f
            );
        }
    }
}
