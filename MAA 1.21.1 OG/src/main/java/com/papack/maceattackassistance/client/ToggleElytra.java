/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.screen.slot.SlotActionType
 *  net.minecraft.item.ArmorItem
 *  net.minecraft.item.ArmorItem$class_8051
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.network.ClientPlayerInteractionManager
 *  net.minecraft.client.network.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;

@Environment(value=EnvType.CLIENT)
public class ToggleElytra {
    public static void toggleElytra(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        ClientPlayerInteractionManager interactionManager = client.interactionManager;
        int hotBarSlotId = -1;
        if (Config.TOGGLE_SLOT != 0) {
            ItemStack itemStack = clientPlayer.getInventory().getStack(Config.TOGGLE_SLOT - 1);
            if (ToggleElytra.isElytra(itemStack) || ToggleElytra.isAir(itemStack) || ToggleElytra.isChestPlate(itemStack)) {
                hotBarSlotId = Config.TOGGLE_SLOT - 1;
            }
        } else {
            int emptySlot = -1;
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = clientPlayer.getInventory().getStack(i);
                if (ToggleElytra.isElytra(itemStack) || ToggleElytra.isChestPlate(itemStack)) {
                    hotBarSlotId = i;
                    break;
                }
                if (!ToggleElytra.isAir(itemStack) || emptySlot != -1) continue;
                emptySlot = i;
            }
            if (hotBarSlotId == -1 && emptySlot > -1) {
                hotBarSlotId = emptySlot;
            }
        }
        if (hotBarSlotId > -1 && interactionManager != null) {
            int slotId = hotBarSlotId;
            client.execute(() -> interactionManager.clickSlot(clientPlayer.playerScreenHandler.syncId, 6, slotId, SlotActionType.SWAP, (PlayerEntity)clientPlayer));
        }
    }

    public static boolean isElytra(ItemStack itemStack) {
        return itemStack.isOf(Items.ELYTRA);
    }

    private static boolean isAir(ItemStack itemStack) {
        return itemStack.isOf(Items.AIR);
    }

    private static boolean isChestPlate(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item instanceof ArmorItem) {
            ArmorItem armorItem = (ArmorItem)item;
            return armorItem.getType().equals(ArmorItem.Type.CHESTPLATE);
        }
        return false;
    }
}
