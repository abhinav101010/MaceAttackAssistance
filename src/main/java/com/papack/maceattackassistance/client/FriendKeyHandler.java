/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.Window
 *  net.minecraft.Entity
 *  net.minecraft.PlayerEntity
 *  net.minecraft.MinecraftClient
 *  net.minecraft.InputUtil
 *  net.minecraft.Screen
 *  net.minecraft.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.FriendListScreen;
import com.papack.maceattackassistance.client.FriendManager;
import com.papack.maceattackassistance.client.SwitchAssist;
import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.WorldPlayerManager;
import net.minecraft.client.util.Window;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;

public class FriendKeyHandler {
    private static int wait = 0;

    public static void tick() {
        if (wait > 0) {
            --wait;
        }
    }

    public static void friendKeyHandler(MinecraftClient client) {
        boolean flag;
        ClientPlayerEntity player = client.player;
        Window handle = client.getWindow();
        if (player == null) {
            return;
        }
        boolean sneak = client.options.sneakKey.isPressed();
        boolean ctrl = client.options.sprintKey.isPressed();
        boolean alt = InputUtil.isKeyPressed((Window)handle, (int)342);
        boolean bl = flag = ctrl || sneak || alt;
        if (wait == 0) {
            if (!flag) {
                WorldPlayerManager.clear();
                WorldPlayerManager.setWorldPlayers(client);
                TickScheduler.setDelayTask(1, () -> client.setScreen((Screen)new FriendListScreen(client.currentScreen)));
            } else if (!sneak && !ctrl) {
                SwitchAssist.changeSettings(SwitchAssist.SwitchKey.FRIEND_PROTECTION);
            } else {
                PlayerEntity other;
                Entity target = client.targetedEntity;
                if (target instanceof PlayerEntity && (other = (PlayerEntity)target) != player) {
                    if (!ctrl && !alt) {
                        FriendManager.removeFriend(other.getUuid());
                        SwitchAssist.displayMessage("Removed: " + other.getName().getString());
                    } else if (!sneak && !alt) {
                        FriendManager.addFriend(other.getName().getString(), other.getUuid());
                        SwitchAssist.displayMessage("Added: " + other.getName().getString());
                    }
                }
            }
            wait = 20;
        }
    }
}
