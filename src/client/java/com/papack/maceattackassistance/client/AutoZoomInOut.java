/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants$Key
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 */
package com.papack.maceattackassistance.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.ZoomState;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.mixin.KeyMappingInvoker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class AutoZoomInOut {
    private static boolean inCancelling = false;
    private static boolean flag_inZoom = false;

    public static boolean isCancel() {
        return inCancelling;
    }

    public static void autoZoomInOut() {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;
        if (player == null) {
            return;
        }
        if (Config.ZOOM_CAMERA && Config.RECON_QUICK_ATTACK && (client.options.keyShift.isDown() || client.options.keyUse.isDown()) && client.options.keyAttack.isDown() && client.options.keyJump.isDown() && (!ZoomState.TEMPORARY_GAZE_ZOOM_FOV_IN_OUT || flag_inZoom) && !inCancelling) {
            inCancelling = true;
            KeyMapping.click((InputConstants.Key)((KeyMappingInvoker)Config.ZOOM_CAMERA_KEY).accessorBoundKey());
            TickScheduler.setDelayTask(3, () -> {
                inCancelling = false;
            });
        }
        if (Config.ZOOM_CAMERA && Config.RECON_QUICK_ZOOM && !flag_inZoom && client.options.keyAttack.isDown() && !ZoomState.TEMPORARY_GAZE_ZOOM_FOV_IN_OUT) {
            flag_inZoom = true;
            ZoomState.zoomFovInOut = ZoomState.zoomFov;
            ZoomState.zoomFovInOut -= (float)(2 * Config.ZOOM_STEP * 10);
            ZoomState.zoomFovInOut = (float)Math.max(5.0, Math.min(140.0, (double)ZoomState.zoomFovInOut));
            ZoomState.TEMPORARY_GAZE_ZOOM_FOV_IN_OUT = true;
            TickScheduler.setDelayTask(6, ZoomState::reset4);
        }
        if (Config.ZOOM_CAMERA && Config.RECON_QUICK_ZOOM && flag_inZoom && !client.options.keyAttack.isDown() && !ZoomState.TEMPORARY_GAZE_ZOOM_FOV_IN_OUT) {
            ZoomState.zoomFovInOut = ZoomState.zoomFov;
            ZoomState.zoomFovInOut += (float)(2 * Config.ZOOM_STEP * 10);
            ZoomState.zoomFovInOut = (float)Math.max(5.0, Math.min(140.0, (double)ZoomState.zoomFovInOut));
            ZoomState.TEMPORARY_GAZE_ZOOM_FOV_IN_OUT = true;
            TickScheduler.setDelayTask(6, () -> {
                flag_inZoom = false;
                ZoomState.reset4();
            });
        }
    }
}
