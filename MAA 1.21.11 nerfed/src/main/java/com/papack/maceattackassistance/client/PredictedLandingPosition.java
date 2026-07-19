/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.World
 *  net.minecraft.AirBlock
 *  net.minecraft.BlockPos
 *  net.minecraft.Position
 *  net.minecraft.Vec3d
 *  net.minecraft.BlockState
 *  net.minecraft.MinecraftClient
 *  net.minecraft.Camera
 *  net.minecraft.MatrixStack
 *  net.minecraft.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.world.World;
import net.minecraft.block.AirBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.network.ClientPlayerEntity;

public class PredictedLandingPosition {
    public static void tick(MatrixStack matrixStack, Camera camera) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }
        if (player.isOnGround()) {
            return;
        }
        if (player.getVelocity().getY() > 0.0) {
            return;
        }
        Vec3d landing = PredictedLandingPosition.simulateLanding(player, player.getEntityPos(), player.getVelocity(), Config.AUTO_ELYTRA_TICK_AHEAD);
        PredictedLandingPosition.drawLandingMarker(matrixStack, camera, landing);
    }

    private static Vec3d simulateLanding(ClientPlayerEntity player, Vec3d startPos, Vec3d velocity, int maxTicks) {
        World world = player.getEntityWorld();
        Vec3d pos = startPos;
        Vec3d vel = velocity;
        double gravity = 0.08;
        double drag = 0.91;
        for (int i = 0; i < maxTicks; ++i) {
            vel = vel.add(0.0, -gravity, 0.0);
            BlockState state = world.getBlockState(BlockPos.ofFloored((Position)(pos = pos.add(vel = vel.multiply(drag, drag, drag)))));
            if (!(state.getBlock() instanceof AirBlock)) break;
        }
        return pos;
    }

    private static void drawLandingMarker(MatrixStack matrices, Camera camera, Vec3d landing) {
    }
}
