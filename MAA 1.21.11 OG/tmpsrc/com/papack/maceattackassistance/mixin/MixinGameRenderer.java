/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.render.Camera
 *  net.minecraft.client.render.GameRenderer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.papack.maceattackassistance.mixin;

import com.papack.maceattackassistance.client.ZoomState;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.mixin.CameraInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GameRenderer.class})
public class MixinGameRenderer {
    @Inject(method={"getFov"}, at={@At(value="TAIL")}, cancellable=true)
    private void onUpdateCamera(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        if (MinecraftClient.getInstance().player == null) {
            return;
        }
        if (Config.ZOOM_CAMERA || ZoomState.TEMPORARY_GAZE_BOOST || ZoomState.TEMPORARY_GAZE_ZOOM_RETURN || ZoomState.TEMPORARY_GAZE_ZOOM_FOV || ZoomState.TEMPORARY_GAZE_ZOOM_FOV_IN_OUT) {
            float yaw = camera.getYaw();
            ((CameraInvoker)camera).setRotationInvoker(yaw, ZoomState.zoomPitch);
            ZoomState.zoomMode = true;
        } else {
            ZoomState.zoomMode = false;
        }
        if (ZoomState.zoomMode) {
            cir.setReturnValue((Object)ZoomState.zoomFov);
        }
    }
}
