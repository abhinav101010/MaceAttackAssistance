/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.util.Formatting
 *  net.minecraft.text.Text
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.text.MutableText
 *  net.minecraft.client.network.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.client.config.ConfigOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Formatting;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.client.network.ClientPlayerEntity;

@Environment(value=EnvType.CLIENT)
public class SwitchAssist {
    public static void changeSettings(SwitchKey key) {
        MutableText msg;
        switch (key.ordinal()) {
            case 0: {
                Config.ATTACK_ASSISTANCE = !Config.ATTACK_ASSISTANCE;
                msg = Text.literal((String)"Mace Attack Assist : ").append(Config.ATTACK_ASSISTANCE ? "On" : "Off");
                break;
            }
            case 1: {
                Config.AIM_ASSIST = !Config.AIM_ASSIST;
                msg = Text.literal((String)"Aim Assist : ").append(Config.AIM_ASSIST ? "On" : "Off");
                break;
            }
            case 2: {
                Config.JUMP_ASSIST = !Config.JUMP_ASSIST;
                msg = Text.literal((String)"Jump Assist : ").append(Config.JUMP_ASSIST ? "On" : "Off");
                break;
            }
            case 3: {
                Config.TARGET_SEARCH_MODE = !Config.TARGET_SEARCH_MODE;
                msg = Text.literal((String)"Target Search Mode : ").append(Config.TARGET_SEARCH_MODE ? "On" : "Off");
                break;
            }
            case 4: {
                Config.RETURN_TO_PREV_SLOT = !Config.RETURN_TO_PREV_SLOT;
                msg = Text.literal((String)"Return To PrevSlot : ").append(Config.RETURN_TO_PREV_SLOT ? "On" : "Off");
                break;
            }
            default: {
                return;
            }
        }
        SwitchAssist.displayMessage(msg);
    }

    public static void displayMessage(MutableText msg) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            MutableText message = Config.DISPLAY_ACTION_BAR ? msg.formatted(Formatting.BOLD) : msg;
            player.sendMessage((Text)message.formatted(Formatting.GREEN), Config.DISPLAY_ACTION_BAR);
        }
        try {
            ConfigOperation.existFile();
            ConfigOperation.saveFile();
            MaceAttackAssistanceClient.LOGGER.info("save config : completed");
        }
        catch (Exception e) {
            MaceAttackAssistanceClient.LOGGER.error("save config : failed");
        }
    }

    public static enum SwitchKey {
        ATTACK,
        AIM,
        JUMP,
        TARGET_SEARCH,
        RETURN_TO_PREV_SLOT;

    }
}
