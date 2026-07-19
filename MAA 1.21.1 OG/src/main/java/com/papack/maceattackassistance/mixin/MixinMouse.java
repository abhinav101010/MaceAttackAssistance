/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.option.KeyBinding
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.Mouse
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.network.ClientPlayerEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.papack.maceattackassistance.mixin;

import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.ZoomState;
import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Mouse.class})
public class MixinMouse {
    @Inject(method={"onMouseScroll"}, at={@At(value="HEAD")}, cancellable=true)
    public void mixinOnMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayer = client.player;
        Screen screen = client.currentScreen;
        KeyBinding sprintKey = client.options.sprintKey;
        if (screen == null && ZoomState.zoomMode && sprintKey.isPressed() && clientPlayer != null && clientPlayer.isFallFlying()) {
            ZoomState.zoomFovInOut = ZoomState.zoomFov;
            ZoomState.zoomFovInOut -= (float)(vertical * (double)Config.ZOOM_STEP * 10.0);
            ZoomState.zoomFovInOut = (float)Math.max(5.0, Math.min(140.0, (double)ZoomState.zoomFovInOut));
            ZoomState.TEMPORARY_GAZE_ZOOM_FOV_IN_OUT = true;
            TickScheduler.schedule(6, ZoomState::reset4);
            ci.cancel();
        }
    }
}
