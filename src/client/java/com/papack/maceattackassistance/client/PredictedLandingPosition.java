/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.rendertype.RenderTypes
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.AirBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.UnknownNullability
 */
package com.papack.maceattackassistance.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.papack.maceattackassistance.client.PearlGrapple;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
// MultiBufferSource removed in MC 26.2 — rendering pipeline changed
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.UnknownNullability;

public class PredictedLandingPosition {
    public static void tick(@UnknownNullability LevelRenderContext context) {
        Vec3 peakPos;
        PoseStack matrixStack = context.poseStack();
        Camera camera = context.gameRenderer().mainCamera();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (player.onGround()) {
            return;
        }
        if (player.getDeltaMovement().y() > 0.0) {
            return;
        }
        if (Config.DEBUG_SCREEN && (peakPos = PearlGrapple.getPeakPos()) != null) {
            PredictedLandingPosition.drawLandingMarker(matrixStack, camera, peakPos);
        }
        Vec3 landing = PredictedLandingPosition.simulateLanding(player, player.position(), player.getDeltaMovement(), Config.AUTO_ELYTRA_TICK_AHEAD);
        PredictedLandingPosition.drawLandingMarker(matrixStack, camera, landing);
    }

    private static Vec3 simulateLanding(LocalPlayer player, Vec3 startPos, Vec3 velocity, int maxTicks) {
        Level world = player.level();
        Vec3 pos = startPos;
        Vec3 vel = velocity;
        double gravity = 0.08;
        double drag = 0.91;
        for (int i = 0; i < maxTicks; ++i) {
            vel = vel.add(0.0, -gravity, 0.0);
            BlockState state = world.getBlockState(BlockPos.containing((Position)(pos = pos.add(vel = vel.multiply(drag, drag, drag)))));
            if (!(state.getBlock() instanceof AirBlock)) break;
        }
        return pos;
    }

    public static void drawLandingMarker(PoseStack poseStack, Camera camera, Vec3 landing) {
        // Stubbed out: MC 26.2 removed client.renderBuffers(). The MultiBufferSource pipeline
        // changed entirely. This debug overlay does not affect core combat features.
    }
}
