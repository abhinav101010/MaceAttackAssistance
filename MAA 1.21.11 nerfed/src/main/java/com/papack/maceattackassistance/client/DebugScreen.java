/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.MinecraftClient
 *  net.minecraft.TextRenderer
 *  net.minecraft.DrawContext
 *  net.minecraft.Screen
 *  net.minecraft.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.Debug;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.PrevSlotManager;
import com.papack.maceattackassistance.client.RefillManager;
import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;

@Environment(value=EnvType.CLIENT)
public class DebugScreen {
    public static double lastY = 0.0;

    public static void debugOverlay(DrawContext drawContext) {
        MinecraftClient client = MinecraftClient.getInstance();
        Screen screen = client.currentScreen;
        ClientPlayerEntity player = client.player;
        TextRenderer textRenderer = client.textRenderer;
        if (player != null && textRenderer != null && screen == null) {
            double vx = player.getVelocity().getX();
            double vz = player.getVelocity().getZ();
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
            drawContext.drawText(textRenderer, "pvp: " + !JobManager.jumpOption(), 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "onGround: " + player.isOnGround(), 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "pitch: " + player.getPitch(), 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "yaw: " + player.getYaw(), 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "v-Y: " + player.getVelocity().getY(), 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "v-X,Z: " + v, 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "velocity length: " + player.getVelocity().length(), 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "Y Diff: " + (player.getY() - lastY), 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "Result: " + Debug.result, 0, ++y * 10, -1, false);
            String jmp = "#".repeat(MaceAttackAssistanceClient.jumpCooldown);
            drawContext.drawText(textRenderer, "Jump Spam: " + jmp, 0, ++y * 10, -1, false);
            ++y;
            drawContext.drawText(textRenderer, "Aim Assist: " + Config.AIM_ASSIST, 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "Attack Assist: " + Config.ATTACK_ASSISTANCE, 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "Breach Swap: " + Config.BREACH_SWAP, 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "Double Tap: " + Config.DOUBLE_TAP, 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "Input Method: " + Config.EXTREME, 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "Jump Assist: " + Config.JUMP_ASSIST, 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "Jump Spam: " + Config.JUMP_SPAM, 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "Prioritize Firework Rocket: " + Config.PRIORITIZE_ROCKET, 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "Prioritize Wind Charge: " + Config.PRIORITIZE_WIND_CHARGE, 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "Return PrevSlot: " + Config.RETURN_TO_PREV_SLOT, 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "Return PrevSlot (B): " + Config.RETURN_TO_PREV_SLOT_BREACH, 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "Rocket Blitz: " + Config.ROCKET_BLITZ, 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "Snapback: " + Config.SNAPBACK, 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "Stun Slam: " + Config.STUN_SLAMMING, 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "Wall Climbing: " + Config.WALL_CLIMBING, 0, ++y * 10, -1, false);
            ++y;
            drawContext.drawText(textRenderer, "running: " + !JobManager.checkOrderIsEmpty(), 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "hasDelayTask: " + TickScheduler.hasPendingOrReadyDelayedTasks(), 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "hasConditionTask: " + TickScheduler.hasPendingOrReadyConditionTasks(), 0, ++y * 10, -1, false);
            ++y;
            drawContext.drawText(textRenderer, "hasPrevSlotTask: " + !PrevSlotManager.isEmpty(), 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "hasRefillTask: " + !RefillManager.isEmpty(), 0, ++y * 10, -1, false);
            drawContext.drawText(textRenderer, "fallDistance: " + player.fallDistance, 0, ++y * 10, -1, false);
        }
    }
}
