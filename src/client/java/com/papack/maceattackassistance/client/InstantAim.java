/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Mth
 */
package com.papack.maceattackassistance.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class InstantAim {
    private static float startPitch;
    private static float targetPitch;
    private static long startTime;
    private static long DURATION_MS;
    private static boolean isMoving;

    public static void tick(Minecraft client) {
        if (isMoving && client.player != null) {
            long currentTime = System.currentTimeMillis();
            long elapsed = currentTime - startTime;
            if (elapsed >= DURATION_MS) {
                client.player.setXRot(targetPitch);
                isMoving = false;
            } else {
                float t = (float)elapsed / (float)DURATION_MS;
                t = t * t * (3.0f - 2.0f * t);
                float nextPitch = Mth.lerp((float)t, (float)startPitch, (float)targetPitch);
                client.player.setXRot(nextPitch);
            }
        }
    }

    public static void startInstantAim(float pitch, long duration) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) {
            return;
        }
        DURATION_MS = duration;
        startPitch = client.player.getXRot();
        targetPitch = Mth.clamp((float)pitch, (float)-90.0f, (float)90.0f);
        startTime = System.currentTimeMillis();
        isMoving = true;
    }

    static {
        DURATION_MS = 80L;
        isMoving = false;
    }
}
