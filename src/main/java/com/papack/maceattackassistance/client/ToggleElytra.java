/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.EquipmentSlot
 *  net.minecraft.PlayerInventory
 *  net.minecraft.ItemStack
 *  net.minecraft.Items
 *  net.minecraft.MinecraftClient
 *  net.minecraft.ItemTags
 *  net.minecraft.ClientPlayerInteractionManager
 *  net.minecraft.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.AutoRefill;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;

@Environment(value=EnvType.CLIENT)
public class ToggleElytra {
    public static int toggleElytra() {
        int endSlot;
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayer = client.player;
        ClientPlayerInteractionManager interactionManager = client.interactionManager;
        if (clientPlayer == null || interactionManager == null) {
            return -1;
        }
        int airSlot = -1;
        int armorSlot = -1;
        int elytraSlot = -1;
        int targetSlot = -1;
        int n = endSlot = Config.ALSO_SEARCH_INVENTORY ? 36 : 9;
        if (Config.TOGGLE_SLOT != 0) {
            ItemStack itemStack = clientPlayer.getInventory().getStack(Config.TOGGLE_SLOT - 1);
            if (ToggleElytra.isElytra(itemStack) || ToggleElytra.isAir(itemStack) || ToggleElytra.isChestPlate(itemStack)) {
                targetSlot = (Config.ELYTRA_MANUAL_MODE ? 0 : 36) + Config.TOGGLE_SLOT - 1;
            }
        } else {
            PlayerInventory inventory = clientPlayer.getInventory();
            for (int i = 0; i < endSlot; ++i) {
                ItemStack stack = inventory.getStack(i);
                if (airSlot == -1 && ToggleElytra.isAir(stack)) {
                    airSlot = i;
                }
                if (armorSlot == -1 && ToggleElytra.isChestPlate(stack)) {
                    armorSlot = i;
                }
                if (elytraSlot != -1 || !ToggleElytra.isElytra(stack)) continue;
                elytraSlot = i;
            }
            ItemStack currEquip = clientPlayer.getEquippedStack(EquipmentSlot.CHEST);
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
        return itemStack.isOf(Items.ELYTRA);
    }

    private static boolean isAir(ItemStack itemStack) {
        return itemStack.isOf(Items.AIR);
    }

    public static boolean isChestPlate(ItemStack itemStack) {
        return itemStack.isIn(ItemTags.CHEST_ARMOR);
    }
}
