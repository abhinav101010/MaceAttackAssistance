/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.client.network.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.network.ClientPlayerEntity;

@Environment(value=EnvType.CLIENT)
public class RocketBlitz {
    public static int getRocketSlotId(ClientPlayerEntity clientPlayer) {
        int hotBarSlotId;
        block2: {
            block1: {
                hotBarSlotId = -1;
                if (Config.ROCKET_BLITZ_SLOT == 0) break block1;
                ItemStack itemStack = clientPlayer.getInventory().getStack(Config.ROCKET_BLITZ_SLOT - 1);
                if (!RocketBlitz.isRocket(itemStack)) break block2;
                hotBarSlotId = Config.ROCKET_BLITZ_SLOT - 1;
                break block2;
            }
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = clientPlayer.getInventory().getStack(i);
                if (!RocketBlitz.isRocket(itemStack)) continue;
                hotBarSlotId = i;
                break;
            }
        }
        return hotBarSlotId;
    }

    private static boolean isRocket(ItemStack itemStack) {
        return itemStack.isOf(Items.FIREWORK_ROCKET);
    }
}
