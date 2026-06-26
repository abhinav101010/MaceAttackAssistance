/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.MultiPlayerGameMode
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.item.Items
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.AutoRefill;
import com.papack.maceattackassistance.client.EnderPearlManager;
import com.papack.maceattackassistance.client.FriendKeyHandler;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.KnockbackDisplacement;
import com.papack.maceattackassistance.client.PearlGrapple;
import com.papack.maceattackassistance.client.PrevSlotManager;
import com.papack.maceattackassistance.client.SpearAttacks;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.StunSlam;
import com.papack.maceattackassistance.client.SwitchAssist;
import com.papack.maceattackassistance.client.ToggleElytra;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.ZoomState;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.client.config.ModConfigScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;

public class KeyInput {
    public static void keyInputInGame(Minecraft client, LocalPlayer clientPlayer, MultiPlayerGameMode interactionManager) {
        int slot;
        boolean canZoom;
        int slot2;
        while (Config.CONFIG_SCREEN_KEY.consumeClick()) {
            if (client.options.keyShift.isDown()) {
                if (Config.DEBUG_SCREEN) {
                    PearlGrapple.setPeakPos(null);
                }
                Config.DEBUG_SCREEN = !Config.DEBUG_SCREEN;
                continue;
            }
            if (FabricLoader.getInstance().isModLoaded("cloth-config2")) {
                client.setScreenAndShow(ModConfigScreen.getConfigScreen(null));
                continue;
            }
            Minecraft.getInstance().gui.hud.getChat().addClientSystemMessage(Component.literal("MAA requires \"Cloth Config API\".\nIt is not installed."));
        }
        while (Config.BULK_REFILL_KEY.consumeClick()) {
            if (!JobManager.checkOrderIsEmpty() || !JobManager.keyReleased(client)) continue;
            for (int i = 0; i < 10; ++i) {
                if (!AutoRefill.checkStockQuantity(clientPlayer, i)) continue;
                AutoRefill.autoRefill(interactionManager, clientPlayer, 36 + i);
            }
        }
        while (Config.ROCKET_BLITZ_KEY.consumeClick()) {
            if (!JobManager.checkOrderIsEmpty()) continue;
            slot2 = clientPlayer.getInventory().getSelectedSlot();
            JobManager.setOrder(StatusType.TRIGGER_KEY_ROCKET, slot2);
        }
        if (Config.ROCKET_BLITZ && Config.ROCKET_BLITZ_CAN_EMPTY_SLOT && clientPlayer.isFallFlying() && clientPlayer.getItemInHand(InteractionHand.MAIN_HAND).is(Items.AIR) && client.options.keyUse.isDown() && Utils.isTriggerPressed(client, clientPlayer) && JobManager.checkOrderIsEmpty()) {
            slot2 = clientPlayer.getInventory().getSelectedSlot();
            JobManager.setOrder(StatusType.TRIGGER_KEY_ROCKET, slot2);
        }
        boolean bl = canZoom = !clientPlayer.onGround() && clientPlayer.isFallFlying();
        while (Config.ZOOM_CAMERA_KEY.consumeClick()) {
            ZoomState.zoomCamera(client, clientPlayer, canZoom);
        }
        if (Config.ZOOM_CAMERA && !canZoom) {
            ZoomState.zoomCamera(client, clientPlayer, false);
        }
        if (ZoomState.gazeCounter > 0) {
            --ZoomState.gazeCounter;
        }
        if (Config.PEARL_CATCH_MODE) {
            while (Config.ENDER_PEARL_KEY.consumeClick()) {
                if (!JobManager.checkOrderIsEmpty()) continue;
                boolean isCooldown = EnderPearlManager.isCooldown();
                int slot3 = clientPlayer.getInventory().getSelectedSlot();
                if (isCooldown || slot3 <= -1) continue;
                JobManager.setOrder(StatusType.PEARL_CATCH, slot3);
            }
        } else {
            while (Config.ENDER_PEARL_KEY.consumeClick()) {
                if (JobManager.checkStatus(StatusType.ENDER_PEARL)) continue;
                slot = clientPlayer.getInventory().getSelectedSlot();
                JobManager.setOrder(StatusType.ENDER_PEARL, slot);
            }
        }
        while (Config.AIR_POT_KEY.consumeClick()) {
            if (!JobManager.checkOrderIsEmpty()) continue;
            slot = clientPlayer.getInventory().getSelectedSlot();
            JobManager.setOrder(StatusType.AIR_POT, slot);
        }
        while (Config.INSTANT_PEARL_CATCH_KEY.consumeClick()) {
            if (!JobManager.checkOrderIsEmpty() || (slot = clientPlayer.getInventory().getSelectedSlot()) <= -1) continue;
            JobManager.setOrder(StatusType.INSTANT_PEARL_CATCH, slot);
        }
        while (Config.PEARL_GRAPPLE_DOWNWARD_KEY.consumeClick()) {
            if (!JobManager.checkOrderIsEmpty() || (slot = clientPlayer.getInventory().getSelectedSlot()) <= -1) continue;
            JobManager.setOrder(StatusType.PEARL_CATCH_DOWNWARD, slot);
        }
        while (Config.WIND_CHARGE_KEY.consumeClick()) {
            if (!JobManager.checkOrderIsEmpty()) continue;
            slot = clientPlayer.getInventory().getSelectedSlot();
            JobManager.setOrder(StatusType.WIND_CHARGE, slot);
        }
        while (Config.TOGGLE_ELYTRA_KEY.consumeClick()) {
            if (Config.ELYTRA_MANUAL_MODE) {
                slot = PrevSlotManager.isEmpty() ? clientPlayer.getInventory().getSelectedSlot() : PrevSlotManager.getLastOrderSlot();
                JobManager.setOrder(StatusType.ELYTRA_MANUAL_SWITCH_MODE, slot);
                continue;
            }
            ToggleElytra.toggleElytra();
        }
        while (Config.PEARL_GRAPPLE_KEY.consumeClick()) {
            if (!JobManager.checkOrderIsEmpty()) continue;
            slot = PrevSlotManager.isEmpty() ? clientPlayer.getInventory().getSelectedSlot() : PrevSlotManager.getLastOrderSlot();
            JobManager.setOrder(StatusType.PEARL_GRAPPLE, slot);
        }
        while (Config.SPEAR_LUNGE_KEY.consumeClick() && PrevSlotManager.isEmpty()) {
            SpearAttacks.manualLungeAttackHandler(client, clientPlayer);
        }
        if (!Config.MANUAL_STUN_SLAM_KEY.isUnbound() && Config.MANUAL_STUN_SLAM_KEY.isDown()) {
            StunSlam.manualStunSlamCondition(clientPlayer);
        }
        while (Config.ATTACK_SETTING_KEY.consumeClick()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.ATTACK);
        }
        while (Config.AIM_SETTING_KEY.consumeClick()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.AIM);
        }
        while (Config.JUMP_SETTING_KEY.consumeClick()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.JUMP);
        }
        while (Config.SEARCH_SETTING_KEY.consumeClick()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.TARGET_SEARCH);
        }
        while (Config.RETURN_TO_PREV_SLOT_SETTING_KEY.consumeClick()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.RETURN_TO_PREV_SLOT);
        }
        while (Config.BREACH_SWAP_SETTING_KEY.consumeClick()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.BREACH_SWAP);
        }
        while (Config.DOUBLE_TAP_SETTING_KEY.consumeClick()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.DOUBLE_TAP);
        }
        while (Config.TOGGLE_JUMP_MODE_KEY.consumeClick()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.TOGGLE_JUMP_MODE);
        }
        while (Config.REWEAR_SETTING_KEY.consumeClick()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.REWEAR);
        }
        while (Config.ELYTRA_BOOST_SETTING_KEY.consumeClick()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.ELYTRA_BOOST);
        }
        while (Config.RETURN_TO_PREV_SLOT_BREACH_SETTING_KEY.consumeClick()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.RETURN_TO_BREACH);
        }
        while (Config.AUTO_WIND_CHARGE_SELECT_SETTING_KEY.consumeClick()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.AUTO_WIND_CHARGE);
        }
        while (Config.TOGGLE_BREACH_SWAP_MODE_KEY.consumeClick()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.TOGGLE_BREACH_SWAP_MODE);
        }
        while (Config.TOGGLE_SWORD_OR_AXE_KEY.consumeClick()) {
            SwitchAssist.changeSettings(SwitchAssist.SwitchKey.TOGGLE_SWORD_OR_AXE);
        }
        while (Config.FRIEND_PROTECTION_SETTING_KEY.consumeClick()) {
            FriendKeyHandler.friendKeyHandler(client);
        }
        while (Config.KNOCKBACK_DISPLACEMENT_KEY.consumeClick()) {
            if (Utils.isSpear(clientPlayer)) continue;
            KnockbackDisplacement.trigger();
        }
    }
}
