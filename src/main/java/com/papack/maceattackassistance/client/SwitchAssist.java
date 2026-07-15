/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.Formatting
 *  net.minecraft.Text
 *  net.minecraft.MinecraftClient
 *  net.minecraft.MutableText
 *  net.minecraft.ClientPlayerEntity
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
        Object msg;
        switch (key.ordinal()) {
            case 0: {
                Config.AIM_ASSIST = !Config.AIM_ASSIST;
                msg = SwitchAssist.valueToOnOff("Aim Assist : ", Config.AIM_ASSIST);
                break;
            }
            case 1: {
                Config.ATTACK_ASSISTANCE = !Config.ATTACK_ASSISTANCE;
                msg = SwitchAssist.valueToOnOff("Mace Attack Assist : ", Config.ATTACK_ASSISTANCE);
                break;
            }
            case 2: {
                Config.AUTO_WIND_CHARGE_SELECT = !Config.AUTO_WIND_CHARGE_SELECT;
                msg = SwitchAssist.valueToOnOff("Auto WindCharge Selector : ", Config.AUTO_WIND_CHARGE_SELECT);
                break;
            }
            case 3: {
                Config.BREACH_SWAP = !Config.BREACH_SWAP;
                msg = SwitchAssist.valueToOnOff("Basic Swap : ", Config.BREACH_SWAP);
                break;
            }
            case 4: {
                Config.DOUBLE_TAP = !Config.DOUBLE_TAP;
                msg = SwitchAssist.valueToOnOff("Double Tap : ", Config.DOUBLE_TAP);
                break;
            }
            case 5: {
                Config.ELYTRA_BOOST = !Config.ELYTRA_BOOST;
                msg = SwitchAssist.valueToOnOff("Elytra Boost : ", Config.ELYTRA_BOOST);
                break;
            }
            case 7: {
                Config.JUMP_ASSIST = !Config.JUMP_ASSIST;
                msg = SwitchAssist.valueToOnOff("Jump Assist : ", Config.JUMP_ASSIST);
                break;
            }
            case 8: {
                Config.RETURN_TO_PREV_SLOT_BREACH = !Config.RETURN_TO_PREV_SLOT_BREACH;
                msg = SwitchAssist.valueToOnOff("Return To PrevSlot (Breach Swap)  : ", Config.RETURN_TO_PREV_SLOT_BREACH);
                break;
            }
            case 9: {
                Config.RETURN_TO_PREV_SLOT = !Config.RETURN_TO_PREV_SLOT;
                msg = SwitchAssist.valueToOnOff("Return To PrevSlot : ", Config.RETURN_TO_PREV_SLOT);
                break;
            }
            case 10: {
                Config.DOUBLE_TAP_REWEAR = !Config.DOUBLE_TAP_REWEAR;
                msg = SwitchAssist.valueToOnOff("Automatic Rewear : ", Config.DOUBLE_TAP_REWEAR);
                break;
            }
            case 11: {
                Config.SHIELD_DRAINING = !Config.SHIELD_DRAINING;
                msg = SwitchAssist.valueToOnOff("Shield Draining : ", Config.SHIELD_DRAINING);
                break;
            }
            case 12: {
                Config.TARGET_SEARCH_MODE = !Config.TARGET_SEARCH_MODE;
                msg = SwitchAssist.valueToOnOff("Target Search Mode : ", Config.TARGET_SEARCH_MODE);
                break;
            }
            case 13: {
                Config.TOGGLE_JUMP_MODE = !Config.TOGGLE_JUMP_MODE;
                msg = SwitchAssist.valueToLowHigh("Jump Mode : ", Config.TOGGLE_JUMP_MODE);
                break;
            }
            case 6: {
                Config.FRIEND_PROTECTION = !Config.FRIEND_PROTECTION;
                msg = SwitchAssist.valueToOnOff("Friend Protection : ", Config.FRIEND_PROTECTION);
                break;
            }
            case 14: {
                Config.SWORD_SWAP_OR_BREACH_SWAP = switch (Config.SWORD_SWAP_OR_BREACH_SWAP) {
                    case Config.Behavior.BreachSwap -> Config.Behavior.SwordSwap;
                    case Config.Behavior.SwordSwap -> Config.Behavior.Off;
                    default -> Config.Behavior.BreachSwap;
                };
                msg = "Swap Type: " + Config.SWORD_SWAP_OR_BREACH_SWAP.name();
                break;
            }
            case 15: {
                Config.SWORD_OR_AXE = !Config.SWORD_OR_AXE;
                msg = "Sword or Axe: " + (Config.SWORD_OR_AXE ? "Use Sword" : "Use Axe");
                break;
            }
            default: {
                return;
            }
        }
        SwitchAssist.displayMessage((String)msg);
    }

    public static void displayMessage(String msg) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        MutableText text = Text.literal((String)msg);
        if (player != null && Config.DISPLAY_ACTION_BAR != Config.DisplayAt.Off) {
            boolean flag = Config.DISPLAY_ACTION_BAR == Config.DisplayAt.ActionBar;
            MutableText message = flag ? text.formatted(Formatting.BOLD) : text;
            player.sendMessage((Text)message.formatted(Formatting.GREEN), flag);
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

    private static String valueToOnOff(String str, boolean value) {
        return str + (value ? "On" : "Off");
    }

    private static String valueToLowHigh(String str, boolean value) {
        return str + (value ? "Low" : "High");
    }

    public static enum SwitchKey {
        AIM,
        ATTACK,
        AUTO_WIND_CHARGE,
        BREACH_SWAP,
        DOUBLE_TAP,
        ELYTRA_BOOST,
        FRIEND_PROTECTION,
        JUMP,
        RETURN_TO_BREACH,
        RETURN_TO_PREV_SLOT,
        REWEAR,
        SHIELD_DRAINING,
        TARGET_SEARCH,
        TOGGLE_JUMP_MODE,
        TOGGLE_BREACH_SWAP_MODE,
        TOGGLE_SWORD_OR_AXE;

    }
}
