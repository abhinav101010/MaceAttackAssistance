/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants
 *  com.mojang.blaze3d.platform.Window
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 */
package com.papack.maceattackassistance.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.papack.maceattackassistance.client.FriendListScreen;
import com.papack.maceattackassistance.client.FriendManager;
import com.papack.maceattackassistance.client.SwitchAssist;
import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.WorldPlayerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class FriendKeyHandler {
    private static int wait = 0;

    public static void tick() {
        if (wait > 0) {
            --wait;
        }
    }

    public static void friendKeyHandler(Minecraft client) {
        boolean flag;
        LocalPlayer player = client.player;
        Window handle = client.getWindow();
        if (player == null) {
            return;
        }
        boolean sneak = client.options.keyShift.isDown();
        boolean ctrl = client.options.keySprint.isDown();
        boolean alt = InputConstants.isKeyDown((Window)handle, (int)342);
        boolean bl = flag = ctrl || sneak || alt;
        if (wait == 0) {
            if (!flag) {
                WorldPlayerManager.clear();
                WorldPlayerManager.setWorldPlayers(client);
                TickScheduler.setDelayTask(1, () -> client.setScreenAndShow((Screen)new FriendListScreen(client.gui.screen())));
            } else if (!sneak && !ctrl) {
                SwitchAssist.changeSettings(SwitchAssist.SwitchKey.FRIEND_PROTECTION);
            } else {
                Player other;
                Entity target = client.crosshairPickEntity;
                if (target instanceof Player && (other = (Player)target) != player) {
                    if (!ctrl && !alt) {
                        FriendManager.removeFriend(other.getUUID());
                        SwitchAssist.displayMessage("Removed: " + other.getName().getString());
                    } else if (!sneak && !alt) {
                        FriendManager.addFriend(other.getName().getString(), other.getUUID());
                        SwitchAssist.displayMessage("Added: " + other.getName().getString());
                    }
                }
            }
            wait = 20;
        }
    }
}
