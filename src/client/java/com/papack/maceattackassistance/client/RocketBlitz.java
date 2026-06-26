/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.item.FireworkRocketItem
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Environment(value=EnvType.CLIENT)
public class RocketBlitz {
    public static int getRocketSlotId(LocalPlayer clientPlayer) {
        int hotBarSlotId = -1;
        if (Config.ROCKET_BLITZ_SLOT != 0) {
            ItemStack itemStack = clientPlayer.getInventory().getItem(Config.ROCKET_BLITZ_SLOT - 1);
            if (RocketBlitz.isRocket(itemStack)) {
                hotBarSlotId = Config.ROCKET_BLITZ_SLOT - 1;
            }
        } else {
            hotBarSlotId = Utils.findItemInHotbar(FireworkRocketItem.class, true);
        }
        return hotBarSlotId;
    }

    private static boolean isRocket(ItemStack itemStack) {
        return itemStack.is(Items.FIREWORK_ROCKET);
    }
}
