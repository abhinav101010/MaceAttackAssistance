/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.MaceItem
 *  net.minecraft.world.item.enchantment.Enchantment
 *  net.minecraft.world.item.enchantment.Enchantments
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

@Environment(value=EnvType.CLIENT)
public class HotSwap {
    public static int getPrimaryMaceSlotId(LocalPlayer clientPlayer) {
        int hotBarSlotId = -1;
        if (Config.MACE_PRIMARY > -1) {
            ItemStack itemStack = clientPlayer.getInventory().getItem(Config.MACE_PRIMARY);
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

    public static int getBreachMaceSlotId(LocalPlayer clientPlayer) {
        int hotBarSlotId = -1;
        if (Config.MACE_BREACH > -1 && Config.MACE_BREACH < 9) {
            ItemStack itemStack = clientPlayer.getInventory().getItem(Config.MACE_BREACH);
            if (HotSwap.isBreachMace(clientPlayer, itemStack)) {
                hotBarSlotId = Config.MACE_BREACH;
            }
        } else {
            hotBarSlotId = Utils.findItemInHotbar(MaceItem.class, false, (ResourceKey<Enchantment>)Enchantments.BREACH);
        }
        return hotBarSlotId;
    }

    public static boolean isBreachMace(LocalPlayer clientPlayer, ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof MaceItem)) {
            return false;
        }
        return Utils.getEnchantLevel(clientPlayer, itemStack, (ResourceKey<Enchantment>)Enchantments.BREACH) > 0;
    }
}
