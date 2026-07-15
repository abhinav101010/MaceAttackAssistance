/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.FireworkRocketItem
 *  net.minecraft.ItemStack
 *  net.minecraft.Items
 *  net.minecraft.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.network.ClientPlayerEntity;

@Environment(value=EnvType.CLIENT)
public class RocketBlitz {
    public static int getRocketSlotId(ClientPlayerEntity clientPlayer) {
        int hotBarSlotId = -1;
        if (Config.ROCKET_BLITZ_SLOT != 0) {
            ItemStack itemStack = clientPlayer.getInventory().getStack(Config.ROCKET_BLITZ_SLOT - 1);
            if (RocketBlitz.isRocket(itemStack)) {
                hotBarSlotId = Config.ROCKET_BLITZ_SLOT - 1;
            }
        } else {
            hotBarSlotId = Utils.findItemInHotbar(FireworkRocketItem.class, true);
        }
        return hotBarSlotId;
    }

    private static boolean isRocket(ItemStack itemStack) {
        return itemStack.isOf(Items.FIREWORK_ROCKET);
    }
}
