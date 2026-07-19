/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.item.ItemStack
 *  net.minecraft.enchantment.Enchantments
 *  net.minecraft.world.World
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.registry.entry.RegistryEntry$class_6883
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.registry.RegistryKeys
 *  net.minecraft.item.MaceItem
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.world.World;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.item.MaceItem;

@Environment(value=EnvType.CLIENT)
public class HotSwap {
    public static int getPrimaryMaceSlotId(ClientPlayerEntity clientPlayer) {
        int hotBarSlotId;
        block2: {
            block1: {
                hotBarSlotId = -1;
                if (Config.MACE_PRIMARY <= -1 || Config.MACE_PRIMARY >= 9) break block1;
                ItemStack itemStack = clientPlayer.getInventory().getStack(Config.MACE_PRIMARY);
                if (!HotSwap.isMace(itemStack)) break block2;
                hotBarSlotId = Config.MACE_PRIMARY;
                break block2;
            }
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = clientPlayer.getInventory().getStack(i);
                if (!HotSwap.isMace(itemStack)) continue;
                hotBarSlotId = i;
                break;
            }
        }
        return hotBarSlotId;
    }

    private static boolean isMace(ItemStack itemStack) {
        return itemStack.getItem() instanceof MaceItem;
    }

    public static int getBreachMaceSlotId(ClientPlayerEntity clientPlayer) {
        int hotBarSlotId;
        block2: {
            block1: {
                hotBarSlotId = -1;
                if (Config.MACE_BREACH <= -1 || Config.MACE_BREACH >= 9) break block1;
                ItemStack itemStack = clientPlayer.getInventory().getStack(Config.MACE_BREACH);
                if (!HotSwap.isBreachMace(clientPlayer, itemStack)) break block2;
                hotBarSlotId = Config.MACE_BREACH;
                break block2;
            }
            for (int i = 8; i > -1; --i) {
                ItemStack itemStack = clientPlayer.getInventory().getStack(i);
                if (!HotSwap.isBreachMace(clientPlayer, itemStack)) continue;
                hotBarSlotId = i;
                break;
            }
        }
        return hotBarSlotId;
    }

    private static boolean isBreachMace(ClientPlayerEntity clientPlayer, ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof MaceItem)) {
            return false;
        }
        return HotSwap.hasBreachEnchantment(MinecraftClient.getInstance().world, itemStack) > 0;
    }

    public static int hasBreachEnchantment(World world, ItemStack itemStack) {
        if (world != null && itemStack != null) {
            var entry = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.BREACH.getValue());
            if (entry.isPresent()) {
                return itemStack.getEnchantments().getLevel(entry.get());
            }
        }
        return -1;
    }
}
