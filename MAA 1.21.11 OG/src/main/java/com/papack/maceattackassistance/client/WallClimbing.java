/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EquipmentSlot
 *  net.minecraft.item.Items
 *  net.minecraft.world.BlockView
 *  net.minecraft.world.World
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.block.BlockState
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.network.ClientPlayerEntity
 *  org.jetbrains.annotations.NotNull
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class WallClimbing {
    public static ClimbingStatus canEasyWallClimbing(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        if (!Config.WALL_CLIMBING || clientPlayer.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA) || !client.options.forwardKey.isPressed() || !client.options.sprintKey.isPressed() || client.options.sneakKey.isPressed() || clientPlayer.isClimbing() || clientPlayer.isOnGround()) {
            return new ClimbingStatus(false, -1);
        }
        return WallClimbing.getClimbingStatus(clientPlayer, 0);
    }

    @NotNull
    public static ClimbingStatus getClimbingStatus(ClientPlayerEntity clientPlayer, int offsetY) {
        boolean[] existBlock = new boolean[3];
        float yaw = clientPlayer.getYaw();
        World world = MinecraftClient.getInstance().world;
        for (int i = 0; i < 3; ++i) {
            double rad = Math.toRadians(yaw + (float)(-45 + i * 45));
            double offsetX = -Math.sin(rad);
            double offsetZ = Math.cos(rad);
            BlockPos blockPos = new BlockPos((int)Math.floor(clientPlayer.getX() + offsetX), (int)Math.floor(clientPlayer.getY() + (double)offsetY), (int)Math.floor(clientPlayer.getZ() + offsetZ));
            BlockState blockState = world.getBlockState(blockPos);
            existBlock[i] = !blockState.isAir() && (blockState.isSolidBlock((BlockView)world, blockPos) || blockState.isSideSolidFullSquare((BlockView)world, blockPos, Direction.UP) || !blockState.getCollisionShape((BlockView)world, blockPos).isEmpty());
        }
        if (existBlock[1]) {
            return new ClimbingStatus(true, 1);
        }
        if (existBlock[0]) {
            return new ClimbingStatus(true, 0);
        }
        if (existBlock[2]) {
            return new ClimbingStatus(true, 2);
        }
        return new ClimbingStatus(false, -1);
    }

    public static ClimbingStatus resetClimbingStatus() {
        return new ClimbingStatus(false, -1);
    }

    public record ClimbingStatus(boolean canClimbing, int offset) {
    }
}
