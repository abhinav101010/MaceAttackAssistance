/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerInventory
 *  net.minecraft.item.AxeItem
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ShieldItem
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.network.ClientPlayerInteractionManager
 *  net.minecraft.client.network.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.mixin.ClientPlayerInteractionManagerInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;

@Environment(value=EnvType.CLIENT)
public class StunSlam {
    public static void preSelectAxe(MinecraftClient client, ClientPlayerEntity clientPlayer, Entity target) {
        ClientPlayerInteractionManager interactionManager = client.interactionManager;
        PlayerInventory playerInventory = clientPlayer.getInventory();
        ItemStack mainHandStack = clientPlayer.getMainHandStack();
        boolean mainHandItemIsAxe = StunSlam.isAxe(mainHandStack);
        if (Config.STUN_SLAMMING && interactionManager != null && target != null) {
            try {
                if (target instanceof LivingEntity) {
                    int axeSlot;
                    LivingEntity livingEntity = (LivingEntity)target;
                    boolean bl = MaceAttackAssistanceClient.shouldShieldBreak = livingEntity.isUsingItem() && livingEntity.getActiveItem().getItem() instanceof ShieldItem;
                    if (MaceAttackAssistanceClient.shouldShieldBreak && !mainHandItemIsAxe && (axeSlot = StunSlam.getAxeSlotId(clientPlayer)) > -1) {
                        MaceAttackAssistanceClient.ex_preStun_slot = MaceAttackAssistanceClient.ex_previous_slot == -1 ? playerInventory.getSelectedSlot() : MaceAttackAssistanceClient.ex_previous_slot;
                        playerInventory.setSelectedSlot(axeSlot);
                        ((ClientPlayerInteractionManagerInvoker)interactionManager).syncSelectedSlotInvoker();
                    }
                    if (MaceAttackAssistanceClient.ex_previous_slot == -1) {
                        MaceAttackAssistanceClient.ex_previous_slot = MaceAttackAssistanceClient.ex_preStun_slot > -1 ? MaceAttackAssistanceClient.ex_preStun_slot : playerInventory.getSelectedSlot();
                    }
                }
            }
            catch (Exception e) {
                MaceAttackAssistanceClient.LOGGER.info(e.getMessage());
            }
        }
    }

    private static int getAxeSlotId(ClientPlayerEntity clientPlayer) {
        int hotBarSlotId;
        block2: {
            block1: {
                hotBarSlotId = -1;
                if (Config.AXE_SLOT <= -1) break block1;
                ItemStack itemStack = clientPlayer.getInventory().getStack(Config.AXE_SLOT);
                if (!StunSlam.isAxe(itemStack)) break block2;
                hotBarSlotId = Config.AXE_SLOT;
                break block2;
            }
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = clientPlayer.getInventory().getStack(i);
                if (!StunSlam.isAxe(itemStack)) continue;
                hotBarSlotId = i;
                break;
            }
        }
        return hotBarSlotId;
    }

    private static boolean isAxe(ItemStack itemStack) {
        return itemStack.getItem() instanceof AxeItem;
    }
}
