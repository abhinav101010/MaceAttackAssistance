/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.font.TextRenderer
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.network.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.Debug;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
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
            drawContext.drawText(textRenderer, "pitch: " + player.getPitch(), 0, 20, -1, false);
            drawContext.drawText(textRenderer, "yaw: " + player.getYaw(), 0, 30, -1, false);
            drawContext.drawText(textRenderer, "v-Y: " + player.getVelocity().getY(), 0, 40, -1, false);
            drawContext.drawText(textRenderer, "v-X,Z: " + v, 0, 50, -1, false);
            drawContext.drawText(textRenderer, "velocity length: " + player.getVelocity().length(), 0, 60, -1, false);
            drawContext.drawText(textRenderer, "Y Diff: " + (player.getY() - lastY), 0, 70, -1, false);
            drawContext.drawText(textRenderer, "Result: " + Debug.result, 0, 80, -1, false);
            String jmp = "#".repeat(MaceAttackAssistanceClient.jumpCooldown);
            drawContext.drawText(textRenderer, "Jump Spam: " + jmp, 0, 100, -1, false);
            drawContext.drawText(textRenderer, "onGround: " + player.isOnGround(), 0, 120, -1, false);
        }
    }
}
