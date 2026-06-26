/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.papack.maceattackassistance.mixin;

import com.papack.maceattackassistance.client.ZoomState;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.mixin.CameraInvoker;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Camera.class})
public class MixinCamera {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method={"calculateFov"}, at={@At(value="TAIL")}, cancellable=true)
    private void onUpdateCamera(float partialTicks, CallbackInfoReturnable<Float> cir) {
        Camera camera = (Camera)(Object)this;
        if (this.minecraft.player == null) {
            return;
        }
        if (Config.ZOOM_CAMERA || ZoomState.TEMPORARY_GAZE_BOOST || ZoomState.TEMPORARY_GAZE_ZOOM_RETURN || ZoomState.TEMPORARY_GAZE_ZOOM_FOV || ZoomState.TEMPORARY_GAZE_ZOOM_FOV_IN_OUT) {
            float yaw = camera.yRot();
            ((CameraInvoker)camera).setRotationInvoker(yaw, ZoomState.zoomPitch);
            ZoomState.zoomMode = true;
        } else {
            ZoomState.zoomMode = false;
        }
        if (ZoomState.zoomMode) {
            cir.setReturnValue(Float.valueOf(ZoomState.zoomFov));
        }
    }
}
