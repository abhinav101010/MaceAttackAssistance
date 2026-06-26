/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.MultiPlayerGameMode
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.AutoRefill;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Environment(value=EnvType.CLIENT)
public class ToggleElytra {
    public static int toggleElytra() {
        int endSlot;
        Minecraft client = Minecraft.getInstance();
        LocalPlayer clientPlayer = client.player;
        MultiPlayerGameMode interactionManager = client.gameMode;
        if (clientPlayer == null || interactionManager == null) {
            return -1;
        }
        int airSlot = -1;
        int armorSlot = -1;
        int elytraSlot = -1;
        int targetSlot = -1;
        int n = endSlot = Config.ALSO_SEARCH_INVENTORY ? 36 : 9;
        if (Config.TOGGLE_SLOT != 0) {
            ItemStack itemStack = clientPlayer.getInventory().getItem(Config.TOGGLE_SLOT - 1);
            if (ToggleElytra.isElytra(itemStack) || ToggleElytra.isAir(itemStack) || ToggleElytra.isChestPlate(itemStack)) {
                targetSlot = (Config.ELYTRA_MANUAL_MODE ? 0 : 36) + Config.TOGGLE_SLOT - 1;
            }
        } else {
            Inventory inventory = clientPlayer.getInventory();
            for (int i = 0; i < endSlot; ++i) {
                ItemStack stack = inventory.getItem(i);
                if (airSlot == -1 && ToggleElytra.isAir(stack)) {
                    airSlot = i;
                }
                if (armorSlot == -1 && ToggleElytra.isChestPlate(stack)) {
                    armorSlot = i;
                }
                if (elytraSlot != -1 || !ToggleElytra.isElytra(stack)) continue;
                elytraSlot = i;
            }
            ItemStack currEquip = clientPlayer.getItemBySlot(EquipmentSlot.CHEST);
            if (ToggleElytra.isElytra(currEquip)) {
                int n2 = targetSlot = armorSlot > -1 ? armorSlot : airSlot;
            }
            if (ToggleElytra.isChestPlate(currEquip)) {
                targetSlot = elytraSlot;
            }
            if (ToggleElytra.isAir(currEquip)) {
                int n3 = targetSlot = elytraSlot > -1 ? elytraSlot : armorSlot;
            }
        }
        if (Config.ELYTRA_MANUAL_MODE) {
            if (targetSlot > -1 && Utils.isHotBar(targetSlot)) {
                return targetSlot;
            }
        } else if (targetSlot > -1) {
            AutoRefill.equipmentSwap(interactionManager, clientPlayer, targetSlot);
        }
        return -1;
    }

    public static boolean isElytra(ItemStack itemStack) {
        return itemStack.is(Items.ELYTRA);
    }

    private static boolean isAir(ItemStack itemStack) {
        return itemStack.is(Items.AIR);
    }

    public static boolean isChestPlate(ItemStack itemStack) {
        return itemStack.is(ItemTags.CHEST_ARMOR);
    }
}
