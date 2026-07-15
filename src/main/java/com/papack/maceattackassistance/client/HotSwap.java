/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.ItemStack
 *  net.minecraft.Enchantment
 *  net.minecraft.Enchantments
 *  net.minecraft.RegistryKey
 *  net.minecraft.ClientPlayerEntity
 *  net.minecraft.MaceItem
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryKey;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.MaceItem;

@Environment(value=EnvType.CLIENT)
public class HotSwap {
    public static int getPrimaryMaceSlotId(ClientPlayerEntity clientPlayer) {
        int hotBarSlotId = -1;
        if (Config.MACE_PRIMARY > -1) {
            ItemStack itemStack = clientPlayer.getInventory().getStack(Config.MACE_PRIMARY);
            if (HotSwap.isMace(itemStack)) {
                hotBarSlotId = Config.MACE_PRIMARY;
            }
        } else {
            hotBarSlotId = Utils.findItemInHotbar(MaceItem.class, true);
        }
        return hotBarSlotId;
    }

    public static boolean isMace(ItemStack itemStack) {
        return itemStack.getItem() instanceof MaceItem;
    }

    public static int getBreachMaceSlotId(ClientPlayerEntity clientPlayer) {
        int hotBarSlotId = -1;
        if (Config.MACE_BREACH > -1 && Config.MACE_BREACH < 9) {
            ItemStack itemStack = clientPlayer.getInventory().getStack(Config.MACE_BREACH);
            if (HotSwap.isBreachMace(clientPlayer, itemStack)) {
                hotBarSlotId = Config.MACE_BREACH;
            }
        } else {
            hotBarSlotId = Utils.findItemInHotbar(MaceItem.class, false, (RegistryKey<Enchantment>)Enchantments.BREACH);
        }
        return hotBarSlotId;
    }

    public static boolean isBreachMace(ClientPlayerEntity clientPlayer, ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof MaceItem)) {
            return false;
        }
        return Utils.getEnchantLevel(clientPlayer, itemStack, (RegistryKey<Enchantment>)Enchantments.BREACH) > 0;
    }
}
