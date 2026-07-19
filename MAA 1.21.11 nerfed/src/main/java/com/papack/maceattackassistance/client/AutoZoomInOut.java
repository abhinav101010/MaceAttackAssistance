/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.KeyBinding
 *  net.minecraft.MinecraftClient
 *  net.minecraft.InputUtil$Key
 *  net.minecraft.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.ZoomState;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.mixin.KeyBindingInvoker;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.network.ClientPlayerEntity;

public class AutoZoomInOut {
    private static boolean inCancelling = false;
    private static boolean flag_inZoom = false;

    public static boolean isCancel() {
        return inCancelling;
    }

    public static void autoZoomInOut() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }
        if (Config.ZOOM_CAMERA && Config.RECON_QUICK_ATTACK && (client.options.sneakKey.isPressed() || client.options.useKey.isPressed()) && client.options.attackKey.isPressed() && client.options.jumpKey.isPressed() && (!ZoomState.TEMPORARY_GAZE_ZOOM_FOV_IN_OUT || flag_inZoom) && !inCancelling) {
            inCancelling = true;
            KeyBinding.onKeyPressed((InputUtil.Key)((KeyBindingInvoker)Config.ZOOM_CAMERA_KEY).accessorBoundKey());
            TickScheduler.setDelayTask(3, () -> {
                inCancelling = false;
            });
        }
        if (Config.ZOOM_CAMERA && Config.RECON_QUICK_ZOOM && !flag_inZoom && client.options.attackKey.isPressed() && !ZoomState.TEMPORARY_GAZE_ZOOM_FOV_IN_OUT) {
            flag_inZoom = true;
            ZoomState.zoomFovInOut = ZoomState.zoomFov;
            ZoomState.zoomFovInOut -= (float)(2 * Config.ZOOM_STEP * 10);
            ZoomState.zoomFovInOut = (float)Math.max(5.0, Math.min(140.0, (double)ZoomState.zoomFovInOut));
            ZoomState.TEMPORARY_GAZE_ZOOM_FOV_IN_OUT = true;
            TickScheduler.setDelayTask(6, ZoomState::reset4);
        }
        if (Config.ZOOM_CAMERA && Config.RECON_QUICK_ZOOM && flag_inZoom && !client.options.attackKey.isPressed() && !ZoomState.TEMPORARY_GAZE_ZOOM_FOV_IN_OUT) {
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
