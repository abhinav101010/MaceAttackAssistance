/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.Text
 *  net.minecraft.MinecraftClient
 *  net.minecraft.ClientPlayerInteractionManager
 *  net.minecraft.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.AutoRefill;
import com.papack.maceattackassistance.client.FriendKeyHandler;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.PrevSlotManager;
import com.papack.maceattackassistance.client.SpearAttacks;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.SwitchAssist;
import com.papack.maceattackassistance.client.ToggleElytra;
import com.papack.maceattackassistance.client.ZoomState;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.client.config.ModConfigScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;

public class KeyInput {
    public static void keyInputInGame(MinecraftClient client, ClientPlayerEntity clientPlayer, ClientPlayerInteractionManager interactionManager) {
        int slot;
        boolean canZoom;
        while (Config.CONFIG_SCREEN_KEY.wasPressed()) {
            if (client.options.sneakKey.isPressed()) {
                Config.DEBUG_SCREEN = !Config.DEBUG_SCREEN;
                continue;
            }
            if (FabricLoader.getInstance().isModLoaded("cloth-config2")) {
                client.setScreen(ModConfigScreen.getConfigScreen(null));
                continue;
            }
            clientPlayer.sendMessage((Text)Text.literal((String)"MAA requires \"Cloth Config API\".\nIt is not installed."), false);
        }
        while (Config.BULK_REFILL_KEY.wasPressed()) {
            if (!JobManager.checkOrderIsEmpty() || !JobManager.keyReleased(client)) continue;
            for (int i = 0; i < 10; ++i) {
                if (!AutoRefill.checkStockQuantity(clientPlayer, i)) continue;
                AutoRefill.autoRefill(interactionManager, clientPlayer, 36 + i);
            }
        }
        boolean bl = canZoom = !clientPlayer.isOnGround() && clientPlayer.isGliding();
        while (Config.ZOOM_CAMERA_KEY.wasPressed()) {
            ZoomState.zoomCamera(client, clientPlayer, canZoom);
        }
        if (Config.ZOOM_CAMERA && !canZoom) {
            ZoomState.zoomCamera(client, clientPlayer, false);
        }
        if (ZoomState.gazeCounter > 0) {
            --ZoomState.gazeCounter;
        }
        while (Config.ENDER_PEARL_KEY.wasPressed()) {
            if (JobManager.checkStatus(StatusType.ENDER_PEARL)) continue;
            slot = clientPlayer.getInventory().getSelectedSlot();
            JobManager.setOrder(StatusType.ENDER_PEARL, slot);
        }
        while (Config.PEARL_CATCH_KEY.wasPressed()) {
            if (!Config.PEARL_CATCH || JobManager.checkStatus(StatusType.PEARL_CATCH)) continue;
            slot = clientPlayer.getInventory().getSelectedSlot();
            JobManager.setOrder(StatusType.PEARL_CATCH, slot);
        }
        while (Config.TOGGLE_ELYTRA_KEY.wasPressed()) {
            if (Config.ELYTRA_MANUAL_MODE) {
                slot = PrevSlotManager.isEmpty() ? clientPlayer.getInventory().getSelectedSlot() : PrevSlotManager.getLastOrderSlot();
                JobManager.setOrder(StatusType.ELYTRA_MANUAL_SWITCH_MODE, slot);
                continue;
            }
            ToggleElytra.toggleElytra();
        }
        while (Config.SPEAR_LUNGE.wasPressed()) {
            SpearAttacks.manualLungeAttack(client, clientPlayer);
        }
        while (Config.ATTACK_SETTING_KEY.wasPressed()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.ATTACK);
        }
        while (Config.AIM_SETTING_KEY.wasPressed()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.AIM);
        }
        while (Config.JUMP_SETTING_KEY.wasPressed()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.JUMP);
        }
        while (Config.SEARCH_SETTING_KEY.wasPressed()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.TARGET_SEARCH);
        }
        while (Config.RETURN_TO_PREV_SLOT_SETTING_KEY.wasPressed()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.RETURN_TO_PREV_SLOT);
        }
        while (Config.BREACH_SWAP_SETTING_KEY.wasPressed()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.BREACH_SWAP);
        }
        while (Config.DOUBLE_TAP_SETTING_KEY.wasPressed()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.DOUBLE_TAP);
        }
        while (Config.TOGGLE_JUMP_MODE_KEY.wasPressed()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.TOGGLE_JUMP_MODE);
        }
        while (Config.REWEAR_SETTING_KEY.wasPressed()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.REWEAR);
        }
        while (Config.ELYTRA_BOOST_SETTING_KEY.wasPressed()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.ELYTRA_BOOST);
        }
        while (Config.RETURN_TO_PREV_SLOT_BREACH_SETTING_KEY.wasPressed()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.RETURN_TO_BREACH);
        }
        while (Config.AUTO_WIND_CHARGE_SELECT_SETTING_KEY.wasPressed()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.AUTO_WIND_CHARGE);
        }
        while (Config.SHIELD_DRAINING_SETTING_KEY.wasPressed()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.SHIELD_DRAINING);
        }
        while (Config.TOGGLE_BREACH_SWAP_MODE_KEY.wasPressed()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.TOGGLE_BREACH_SWAP_MODE);
        }
        while (Config.TOGGLE_SWORD_OR_AXE_KEY.wasPressed()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.TOGGLE_SWORD_OR_AXE);
        }
        while (Config.FRIEND_PROTECTION_SETTING_KEY.wasPressed()) {
            FriendKeyHandler.friendKeyHandler(client);
        }
    }
}
