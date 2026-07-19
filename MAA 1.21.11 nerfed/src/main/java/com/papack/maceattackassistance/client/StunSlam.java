/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.Entity
 *  net.minecraft.LivingEntity
 *  net.minecraft.PlayerInventory
 *  net.minecraft.AxeItem
 *  net.minecraft.ItemStack
 *  net.minecraft.MinecraftClient
 *  net.minecraft.ClientPlayerInteractionManager
 *  net.minecraft.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.HotSwap;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.PrevSlotManager;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.mixin.ClientPlayerInteractionManagerInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;

@Environment(value=EnvType.CLIENT)
public class StunSlam {
    public static int CHK_PLAYER_AGE = 0;
    public static boolean flagPreAxeCalled = false;
    public static int lastSlot = -1;

    public static boolean preSelectAxe(MinecraftClient client, ClientPlayerEntity clientPlayer, Entity target) {
        flagPreAxeCalled = true;
        ClientPlayerInteractionManager interactionManager = client.interactionManager;
        PlayerInventory playerInventory = clientPlayer.getInventory();
        ItemStack mainHandStack = clientPlayer.getMainHandStack();
        boolean mainHandItemIsAxe = StunSlam.isAxe(mainHandStack);
        if (Config.STUN_SLAMMING && interactionManager != null && target != null && clientPlayer.getVelocity().getY() < 0.0) {
            try {
                if (target instanceof LivingEntity) {
                    int axeSlot;
                    LivingEntity livingEntity = (LivingEntity)target;
                    MaceAttackAssistanceClient.flagPreAxe = MaceAttackAssistanceClient.shouldShieldBreak = livingEntity.isBlocking();
                    if (MaceAttackAssistanceClient.shouldShieldBreak && !mainHandItemIsAxe && (axeSlot = StunSlam.getAxeSlotId(clientPlayer)) > -1) {
                        PrevSlotManager.setPrevSlot(StatusType.NONE, playerInventory.getSelectedSlot(), 0);
                        playerInventory.setSelectedSlot(axeSlot);
                        ((ClientPlayerInteractionManagerInvoker)interactionManager).syncSelectedSlotInvoker();
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

    public static int getAxeSlotId(ClientPlayerEntity clientPlayer) {
        int hotBarSlotId;
        block3: {
            block2: {
                hotBarSlotId = -1;
                if (Config.AXE_SLOT <= -1) break block2;
                ItemStack itemStack = clientPlayer.getInventory().getStack(Config.AXE_SLOT);
                if (!StunSlam.isAxe(itemStack)) break block3;
                hotBarSlotId = Config.AXE_SLOT;
                break block3;
            }
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = clientPlayer.getInventory().getStack(i);
                if (!StunSlam.isAxe(itemStack)) continue;
                hotBarSlotId = i;
                break;
            }
        }
        if (Config.SHIELD_DRAINING && hotBarSlotId == -1 && clientPlayer.getVelocity().getY() < Config.VELOCITY_BY_DISTANCE[2]) {
            hotBarSlotId = HotSwap.getPrimaryMaceSlotId(clientPlayer);
        }
        return hotBarSlotId;
    }

    public static boolean isAxe(ItemStack itemStack) {
        return itemStack.getItem() instanceof AxeItem;
    }
}
