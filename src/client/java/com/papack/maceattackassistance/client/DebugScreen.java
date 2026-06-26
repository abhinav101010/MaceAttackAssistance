/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.Entity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.Debug;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.MAAState;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.PrevSlotManager;
import com.papack.maceattackassistance.client.RefillManager;
import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;

@Environment(value=EnvType.CLIENT)
public class DebugScreen {
    public static double lastY = 0.0;

    public static void debugOverlay(GuiGraphicsExtractor drawContext) {
        Minecraft client = Minecraft.getInstance();
        Screen screen = client.gui.screen();
        LocalPlayer player = client.player;
        Font textRenderer = client.font;
        Entity target = client.crosshairPickEntity;
        if (target == null) {
            target = MaceAttackAssistanceClient.getTargetMob();
        }
        if (player != null && screen == null) {
            double vx = player.getDeltaMovement().x();
            double vz = player.getDeltaMovement().z();
            double v = Math.sqrt(vx * vx + vz * vz);
            double current_y = player.getY();
            if (current_y < Debug.previous_y) {
                Debug.result = Debug.previous_y - lastY;
            } else {
                Debug.previous_y = current_y;
                if (Debug.counter > 0) {
                    --Debug.counter;
                }
            }
            int y = 0;
            drawContext.text(textRenderer, "pvp: " + !MAAState.x(), 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "onGround: " + player.onGround(), 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "pitch: " + player.getXRot(), 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "yaw: " + player.getYRot(), 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "v-Y: " + player.getDeltaMovement().y(), 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "v-X,Z: " + v, 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "velocity length: " + player.getDeltaMovement().length(), 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "Y Diff: " + (player.getY() - lastY), 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "Result: " + Debug.result, 0, ++y * 10, -1, false);
            String jmp = "#".repeat(MaceAttackAssistanceClient.jumpCooldown);
            drawContext.text(textRenderer, "Jump Spam: " + jmp, 0, ++y * 10, -1, false);
            ++y;
            drawContext.text(textRenderer, "Aim Assist: " + Config.AIM_ASSIST, 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "Attack Assist: " + Config.ATTACK_ASSISTANCE, 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "Breach Swap: " + Config.BREACH_SWAP, 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "Double Tap: " + Config.DOUBLE_TAP, 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "Input Method: " + Config.EXTREME, 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "Jump Assist: " + Config.JUMP_ASSIST, 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "Jump Spam: " + Config.JUMP_SPAM, 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "Prioritize Firework Rocket: " + Config.PRIORITIZE_ROCKET, 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "Prioritize Wind Charge: " + Config.PRIORITIZE_WIND_CHARGE, 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "Return PrevSlot: " + Config.RETURN_TO_PREV_SLOT, 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "Return PrevSlot (B): " + Config.RETURN_TO_PREV_SLOT_BREACH, 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "Rocket Blitz: " + Config.ROCKET_BLITZ, 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "Snapback: " + Config.SNAPBACK, 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "Stun Slam: " + Config.STUN_SLAMMING, 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "Wall Climbing: " + Config.WALL_CLIMBING, 0, ++y * 10, -1, false);
            ++y;
            drawContext.text(textRenderer, "running: " + !JobManager.checkOrderIsEmpty(), 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "hasDelayTask: " + TickScheduler.hasPendingOrReadyDelayedTasks(), 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "hasConditionTask: " + TickScheduler.hasPendingOrReadyConditionTasks(), 0, ++y * 10, -1, false);
            ++y;
            drawContext.text(textRenderer, "hasPrevSlotTask: " + !PrevSlotManager.isEmpty(), 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "hasRefillTask: " + !RefillManager.isEmpty(), 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "fallDistance: " + player.fallDistance, 0, ++y * 10, -1, false);
            drawContext.text(textRenderer, "distanceTo: " + String.valueOf(target != null ? Float.valueOf(player.distanceTo(target)) : ""), 0, ++y * 10, -1, false);
        }
    }
}
