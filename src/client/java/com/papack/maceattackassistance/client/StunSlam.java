/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.MultiPlayerGameMode
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.item.AxeItem
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.PrevSlotManager;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.mixin.MultiPlayerGameModeInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@Environment(value=EnvType.CLIENT)
public class StunSlam {
    public static int CHK_PLAYER_AGE = 0;
    public static boolean flagPreAxeCalled = false;
    public static int lastSlot = -1;

    public static boolean preSelectAxe(Minecraft client, LocalPlayer clientPlayer, Entity target) {
        flagPreAxeCalled = true;
        MultiPlayerGameMode interactionManager = client.gameMode;
        Inventory playerInventory = clientPlayer.getInventory();
        ItemStack mainHandStack = clientPlayer.getMainHandItem();
        boolean mainHandItemIsAxe = StunSlam.isAxe(mainHandStack);
        if (Config.STUN_SLAMMING && interactionManager != null && target != null && clientPlayer.getDeltaMovement().y() < 0.0) {
            try {
                if (target instanceof LivingEntity) {
                    int axeSlot;
                    LivingEntity livingEntity = (LivingEntity)target;
                    MaceAttackAssistanceClient.flagPreAxe = MaceAttackAssistanceClient.shouldShieldBreak = livingEntity.isBlocking();
                    if (MaceAttackAssistanceClient.shouldShieldBreak && !mainHandItemIsAxe && (axeSlot = StunSlam.getAxeSlotId(clientPlayer)) > -1) {
                        PrevSlotManager.setPrevSlot(StatusType.NONE, playerInventory.getSelectedSlot(), 0);
                        playerInventory.setSelectedSlot(axeSlot);
                        ((MultiPlayerGameModeInvoker)interactionManager).syncSelectedSlotInvoker();
                    }
                    return MaceAttackAssistanceClient.shouldShieldBreak;
                }
            }
            catch (Exception e) {
                MaceAttackAssistanceClient.LOGGER.info(e.getMessage());
            }
        }
        return false;
    }

    public static int getAxeSlotId(LocalPlayer clientPlayer) {
        int hotBarSlotId;
        block2: {
            block1: {
                hotBarSlotId = -1;
                if (Config.AXE_SLOT <= -1) break block1;
                ItemStack itemStack = clientPlayer.getInventory().getItem(Config.AXE_SLOT);
                if (!StunSlam.isAxe(itemStack)) break block2;
                hotBarSlotId = Config.AXE_SLOT;
                break block2;
            }
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = clientPlayer.getInventory().getItem(i);
                if (!StunSlam.isAxe(itemStack)) continue;
                hotBarSlotId = i;
                break;
            }
        }
        return hotBarSlotId;
    }

    public static boolean isAxe(ItemStack itemStack) {
        return itemStack.getItem() instanceof AxeItem;
    }

    public static void manualStunSlamCondition(LocalPlayer clientPlayer) {
        Entity target = MaceAttackAssistanceClient.getTargetMob();
        if (target != null) {
            int slot;
            StatusType statusType = StunSlam.stunSlamCondition(clientPlayer, target);
            if (Config.ATTACK_ASSISTANCE && statusType != null && JobManager.checkOrderIsEmpty() && (slot = clientPlayer.getInventory().getSelectedSlot()) > -1) {
                JobManager.setOrder(statusType, slot);
            }
        }
    }

    public static StatusType stunSlamCondition(LocalPlayer clientPlayer, Entity target) {
        boolean flagDistance;
        double vy = (clientPlayer.getDeltaMovement().y - 0.08) * 0.98;
        Vec3 pos = clientPlayer.position().add(0.0, vy, 0.0);
        boolean flagIsAir = StunSlam.willBeInAirAfter2Ticks(clientPlayer);
        boolean bl = flagDistance = pos.distanceTo(target.position()) <= (double)Utils.getRangeStun();
        if (flagIsAir && flagDistance) {
            return StatusType.MANUAL_STUN;
        }
        if (flagDistance) {
            return Config.AUTO_SHIELD_DRAINING ? StatusType.AUTO_SHIELD_DRAINING : StatusType.MANUAL_STUN;
        }
        return null;
    }

    public static boolean willBeInAirAfter2Ticks(LocalPlayer player) {
        Level level = player.level();
        Vec3 pos = player.position();
        Vec3 vel = player.getDeltaMovement();
        AABB baseBox = player.getBoundingBox();
        for (int i = 0; i < 2; ++i) {
            vel = vel.scale(0.98);
            vel = vel.add(0.0, -0.08, 0.0);
            Vec3 nextPos = pos.add(vel);
            AABB movedBox = baseBox.move(nextPos.x - player.getX(), nextPos.y - player.getY(), nextPos.z - player.getZ());
            if (level.getBlockCollisions((Entity)player, movedBox).iterator().hasNext()) {
                return false;
            }
            pos = nextPos;
        }
        return true;
    }
}
