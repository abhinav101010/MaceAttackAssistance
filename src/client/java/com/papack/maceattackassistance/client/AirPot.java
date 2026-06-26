/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.item.SplashPotionItem
 *  net.minecraft.world.item.WindChargeItem
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.PearlCatch;
import com.papack.maceattackassistance.client.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.WindChargeItem;

public class AirPot {
    private static int delay;

    public static int airPotionCatch(int value) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;
        if (player == null) {
            return 1;
        }
        if (value == 5) {
            delay = AirPot.getDelay(player.getXRot());
            int potionSlot = Utils.findItemInHotbar(SplashPotionItem.class, true);
            if (potionSlot == -1) {
                return 1;
            }
            PearlCatch.setAndThrow(client, potionSlot);
        }
        if (value == 5 - delay) {
            int chargeSlot = Utils.findItemInHotbar(WindChargeItem.class, true);
            if (chargeSlot == -1) {
                return 1;
            }
            PearlCatch.setAndThrow(client, chargeSlot);
        }
        return value;
    }

    private static int getDelay(double pitch) {
        return pitch > -20.0 ? 1 : 2;
    }
}
