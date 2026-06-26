/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  org.jetbrains.annotations.NotNull
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WallClimbing {
    public static ClimbingStatus canEasyWallClimbing(Minecraft client, LocalPlayer clientPlayer) {
        if (!Config.WALL_CLIMBING || clientPlayer.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA) || !client.options.keyUp.isDown() || !client.options.keySprint.isDown() || client.options.keyShift.isDown() || clientPlayer.onClimbable() || clientPlayer.onGround()) {
            return new ClimbingStatus(false, -1);
        }
        return WallClimbing.getClimbingStatus(clientPlayer, 0);
    }

    @NotNull
    public static ClimbingStatus getClimbingStatus(LocalPlayer clientPlayer, int offsetY) {
        boolean[] existBlock = new boolean[3];
        float yaw = clientPlayer.getYRot();
        Level world = clientPlayer.level();
        for (int i = 0; i < 3; ++i) {
            double rad = Math.toRadians(yaw + (float)(-45 + i * 45));
            double offsetX = -Math.sin(rad);
            double offsetZ = Math.cos(rad);
            BlockPos blockPos = new BlockPos((int)Math.floor(clientPlayer.getX() + offsetX), (int)Math.floor(clientPlayer.getY() + (double)offsetY), (int)Math.floor(clientPlayer.getZ() + offsetZ));
            BlockState blockState = world.getBlockState(blockPos);
            existBlock[i] = !blockState.isAir() && (blockState.isRedstoneConductor((BlockGetter)world, blockPos) || blockState.isFaceSturdy((BlockGetter)world, blockPos, Direction.UP) || !blockState.getCollisionShape((BlockGetter)world, blockPos).isEmpty());
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
