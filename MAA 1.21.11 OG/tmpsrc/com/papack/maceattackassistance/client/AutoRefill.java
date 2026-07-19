/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.entity.player.PlayerInventory
 *  net.minecraft.screen.slot.SlotActionType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.client.network.ClientPlayerInteractionManager
 *  net.minecraft.client.network.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;

public class AutoRefill {
    public static void autoRefill(ClientPlayerInteractionManager interactionManager, ClientPlayerEntity clientPlayer, int slot) {
        if (slot < 0 || clientPlayer.isCreative()) {
            return;
        }
        int containerId = clientPlayer.playerScreenHandler.syncId;
        interactionManager.clickSlot(containerId, slot, 0, SlotActionType.PICKUP, (PlayerEntity)clientPlayer);
        interactionManager.clickSlot(containerId, slot, 0, SlotActionType.PICKUP_ALL, (PlayerEntity)clientPlayer);
        interactionManager.clickSlot(containerId, slot, 0, SlotActionType.PICKUP, (PlayerEntity)clientPlayer);
    }

    public static boolean checkStockQuantity(ClientPlayerEntity clientPlayer, int slot) {
        if (slot < 0 || clientPlayer.isCreative()) {
            return false;
        }
        ItemStack itemStack = slot == 9 ? clientPlayer.getOffHandStack() : clientPlayer.getInventory().getStack(slot);
        if (itemStack.getMaxCount() == itemStack.getCount()) {
            return false;
        }
        Item item = itemStack.getItem();
        return itemStack.isStackable() && AutoRefill.checkStockQuantity(clientPlayer, item);
    }

    public static boolean checkStockQuantity(ClientPlayerEntity clientPlayer, Item item) {
        if (clientPlayer.isCreative()) {
            return false;
        }
        PlayerInventory inventory = clientPlayer.getInventory();
        for (int i = 9; i < 36; ++i) {
            if (!inventory.getStack(i).isOf(item)) continue;
            return true;
        }
        return false;
    }
}
