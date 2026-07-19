/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EquipmentSlot
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.boss.dragon.EnderDragonPart
 *  net.minecraft.entity.boss.dragon.EnderDragonEntity
 *  net.minecraft.item.ElytraItem
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.world.World
 *  net.minecraft.block.AirBlock
 *  net.minecraft.block.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.block.BlockState
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.network.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.ElytraBoost;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.config.Config;
import java.util.Objects;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class Utils {
    public static boolean checkPlayerUUID(Entity entity) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayer = client.player;
        boolean chkUuid = false;
        if (clientPlayer != null) {
            chkUuid = entity.getUuid().equals(clientPlayer.getUuid());
        }
        return chkUuid;
    }

    public static boolean isInAttackableRange(ClientPlayerEntity clientPlayer, Entity targetEntity) {
        if (clientPlayer != null && targetEntity != null) {
            double dz;
            double dx = clientPlayer.getX() - targetEntity.getX();
            return Math.sqrt(dx * dx + (dz = clientPlayer.getZ() - targetEntity.getZ()) * dz) <= 3.0;
        }
        return false;
    }

    public static boolean isNotUsingElytra(ClientPlayerEntity clientPlayer) {
        return !clientPlayer.isGliding() || !clientPlayer.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA);
    }

    public static boolean isOnSlimeBlock(ClientPlayerEntity clientPlayer) {
        BlockPos pos = BlockPos.ofFloored((double)clientPlayer.getX(), (double)(clientPlayer.getY() - 1.0), (double)clientPlayer.getZ());
        BlockState state = MinecraftClient.getInstance().world.getBlockState(pos);
        return state.isOf(Blocks.SLIME_BLOCK);
    }

    public static boolean isUsingElytra(ClientPlayerEntity clientPlayer) {
        return clientPlayer.isGliding() || clientPlayer.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA);
    }

    public static boolean verifyGround(ClientPlayerEntity clientPlayer, int height) {
        return Utils.verifyGround((LivingEntity)clientPlayer, height);
    }

    public static boolean verifyGround(LivingEntity livingEntity, int height) {
        World world = MinecraftClient.getInstance().world;
        for (int i = 1; i <= height; ++i) {
            BlockPos footPosition = livingEntity.getBlockPos().add(0, -1 * i, 0);
            if (world.getBlockState(footPosition).getBlock() instanceof AirBlock) continue;
            return false;
        }
        return true;
    }

    public static double xzDistance(ClientPlayerEntity clientPlayer, Entity target) {
        double dx = clientPlayer.getX() - target.getX();
        double dz = clientPlayer.getZ() - target.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }

    public static float lerpPitch(float start, float end, float delta) {
        return start + delta * (end - start);
    }

    public static float lerpYaw(float start, float end, float delta) {
        return start + delta * MaceAttackAssistanceClient.normalizeAngle(end - start);
    }

    public static Hand getHandHoldingWindCharge(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        ItemStack mainHandStack = clientPlayer.getMainHandStack();
        if (mainHandStack.isOf(Items.WIND_CHARGE)) {
            return Hand.MAIN_HAND;
        }
        if (clientPlayer.getOffHandStack().isOf(Items.WIND_CHARGE) && (Config.PRIORITIZE_WIND_CHARGE || ElytraBoost.isNotUsableItems(client, mainHandStack))) {
            return Hand.OFF_HAND;
        }
        return null;
    }

    public static int findWindChargeSlot(ClientPlayerEntity clientPlayer) {
        for (int i = 0; i < 9; i++) {
            if (clientPlayer.getInventory().getStack(i).isOf(Items.WIND_CHARGE)) {
                return i;
            }
        }
        return -1;
    }

    public static Vec3d getDragonPos(EnderDragonEntity dragon) {
        EnderDragonPart hitPart = dragon.getBodyParts()[1];
        return ((Entity)Objects.requireNonNullElse(hitPart, dragon)).getEyePos();
    }

    public static Vec3d getTargetPos(Entity target, boolean eyePos) {
        Vec3d VanillaChestLootTableGenerator;
        if (target instanceof EnderDragonEntity) {
            EnderDragonEntity enderDragonEntity = (EnderDragonEntity)target;
            VanillaChestLootTableGenerator = Utils.getDragonPos(enderDragonEntity);
        } else {
            VanillaChestLootTableGenerator = eyePos ? target.getEyePos() : new Vec3d(target.getX(), target.getY(), target.getZ());
        }
        return VanillaChestLootTableGenerator;
    }
}
