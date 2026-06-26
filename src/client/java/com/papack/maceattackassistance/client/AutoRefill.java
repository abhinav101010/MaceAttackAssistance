/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.MultiPlayerGameMode
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.ContainerInput
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 */
package com.papack.maceattackassistance.client;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AutoRefill {
    public static void autoRefill(MultiPlayerGameMode interactionManager, LocalPlayer clientPlayer, int slot) {
        if (slot < 0 || clientPlayer.isCreative()) {
            return;
        }
        int containerId = clientPlayer.inventoryMenu.containerId;
        interactionManager.handleContainerInput(containerId, slot, 0, ContainerInput.PICKUP, (Player)clientPlayer);
        interactionManager.handleContainerInput(containerId, slot, 0, ContainerInput.PICKUP_ALL, (Player)clientPlayer);
        interactionManager.handleContainerInput(containerId, slot, 0, ContainerInput.PICKUP, (Player)clientPlayer);
    }

    public static void autoRefillInventory(MultiPlayerGameMode interactionManager, LocalPlayer clientPlayer, int inventory, int hotbar) {
        if (inventory < 9 || inventory > 35) {
            return;
        }
        if (hotbar < 36 || hotbar > 44) {
            return;
        }
        if (clientPlayer.isCreative()) {
            return;
        }
        int containerId = clientPlayer.inventoryMenu.containerId;
        interactionManager.handleContainerInput(containerId, inventory, 0, ContainerInput.PICKUP, (Player)clientPlayer);
        interactionManager.handleContainerInput(containerId, inventory, 0, ContainerInput.PICKUP_ALL, (Player)clientPlayer);
        interactionManager.handleContainerInput(containerId, hotbar, 0, ContainerInput.PICKUP, (Player)clientPlayer);
        interactionManager.handleContainerInput(containerId, inventory, 0, ContainerInput.PICKUP, (Player)clientPlayer);
    }

    public static boolean checkStockQuantity(LocalPlayer clientPlayer, int slot) {
        if (slot < 0 || clientPlayer.isCreative()) {
            return false;
        }
        ItemStack itemStack = slot == 9 ? clientPlayer.getOffhandItem() : clientPlayer.getInventory().getItem(slot);
        if (itemStack.getMaxStackSize() == itemStack.getCount()) {
            return false;
        }
        Item item = itemStack.getItem();
        return itemStack.isStackable() && AutoRefill.checkStockQuantity(clientPlayer, item);
    }

    public static boolean checkStockQuantity(LocalPlayer clientPlayer, Item item) {
        if (clientPlayer.isCreative()) {
            return false;
        }
        Inventory inventory = clientPlayer.getInventory();
        for (int i = 9; i < 36; ++i) {
            if (!inventory.getItem(i).is(item)) continue;
            return true;
        }
        return false;
    }

    public static void equipmentSwap(MultiPlayerGameMode interactionManager, LocalPlayer clientPlayer, int targetSlot) {
        int containerId = clientPlayer.inventoryMenu.containerId;
        int equipmentSlot = 6;
        targetSlot = targetSlot < 9 ? targetSlot + 36 : targetSlot;
        interactionManager.handleContainerInput(containerId, targetSlot, 0, ContainerInput.PICKUP, (Player)clientPlayer);
        interactionManager.handleContainerInput(containerId, equipmentSlot, 0, ContainerInput.PICKUP, (Player)clientPlayer);
        interactionManager.handleContainerInput(containerId, targetSlot, 0, ContainerInput.PICKUP, (Player)clientPlayer);
    }
}
