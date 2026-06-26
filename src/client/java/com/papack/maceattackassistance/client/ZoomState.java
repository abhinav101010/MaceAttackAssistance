/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.CameraType
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.RenderPipelines
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.util.Mth
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.AutoZoomInOut;
import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.client.CameraType;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

public class ZoomState {
    public static float zoomFov = 70.0f;
    public static boolean zoomMode = false;
    public static float zoomPitch = -4.0f;
    public static float zoomFovInOut = 0.0f;
    public static double lastMouseY = 0.0;
    public static boolean firstFrame = true;
    public static boolean TEMPORARY_GAZE_BOOST = false;
    public static boolean TEMPORARY_GAZE_ZOOM = false;
    public static boolean TEMPORARY_GAZE_ZOOM_RETURN = false;
    public static boolean TEMPORARY_GAZE_ZOOM_FOV = false;
    public static boolean TEMPORARY_GAZE_ZOOM_FOV_IN_OUT = false;
    public static int gazeCounter = 0;

    public static void moveCamera(Minecraft client, LocalPlayer clientPlayer, DeltaTracker context) {
        float tickDelta;
        if (TEMPORARY_GAZE_BOOST) {
            tickDelta = context.getGameTimeDeltaTicks();
            float angle = gazeCounter > 1 ? 0.0f : (float)((double)Config.REFLECTION_ANGLE * (1.0 - 0.5 * (double)gazeCounter));
            zoomPitch = Utils.lerpPitch(zoomPitch, angle, tickDelta);
        }
        if (TEMPORARY_GAZE_ZOOM) {
            tickDelta = context.getGameTimeDeltaTicks();
            zoomPitch = ZoomState.lerpOnTickTime(zoomPitch, -4.0f, tickDelta, 2);
            if (TEMPORARY_GAZE_ZOOM && ZoomState.closeValueComparison(zoomPitch, -4.0f, 0.2f)) {
                ZoomState.reset1();
            }
        }
        if (TEMPORARY_GAZE_ZOOM_FOV) {
            tickDelta = context.getGameTimeDeltaTicks();
            zoomFov = ZoomState.lerpOnTickTime(zoomFov, ((Integer)client.options.fov().get()).intValue(), tickDelta, 2);
            if (TEMPORARY_GAZE_ZOOM_FOV && ZoomState.closeValueComparison(zoomFov, ((Integer)client.options.fov().get()).intValue(), 0.2f)) {
                ZoomState.reset2();
            }
        }
        if (TEMPORARY_GAZE_ZOOM_RETURN) {
            tickDelta = context.getGameTimeDeltaTicks();
            zoomPitch = ZoomState.lerpOnTickTime(zoomPitch, clientPlayer.getXRot(), tickDelta, 2);
            zoomFov = ZoomState.lerpOnTickTime(zoomFov, ((Integer)client.options.fov().get()).intValue(), tickDelta, 2);
            if (TEMPORARY_GAZE_ZOOM_RETURN && ZoomState.closeValueComparison(zoomPitch, clientPlayer.getXRot(), 0.3f) && ZoomState.closeValueComparison(zoomFov, ((Integer)client.options.fov().get()).intValue(), 0.2f)) {
                ZoomState.reset3();
            }
        }
        if (TEMPORARY_GAZE_ZOOM_FOV_IN_OUT) {
            tickDelta = context.getGameTimeDeltaTicks();
            zoomFov = ZoomState.lerpOnTickTime(zoomFov, zoomFovInOut, tickDelta, 2);
            if (TEMPORARY_GAZE_ZOOM_FOV_IN_OUT && ZoomState.closeValueComparison(zoomFov, zoomFovInOut, 0.2f)) {
                ZoomState.reset4();
            }
        }
        if (Config.ZOOM_CAMERA) {
            clientPlayer.setXRot(-4.0f);
            if (!zoomMode || !client.isWindowActive()) {
                firstFrame = true;
                return;
            }
            double mouseY = client.mouseHandler.ypos();
            if (firstFrame) {
                lastMouseY = mouseY;
                firstFrame = false;
                return;
            }
            double deltaY = mouseY - lastMouseY;
            lastMouseY = mouseY;
            float sensitivity = ((Double)client.options.sensitivity().get()).floatValue();
            float multiplier = sensitivity * 0.15f;
            if (client.gui.screen() == null) {
                zoomPitch += (float)(deltaY * (double)multiplier);
            }
            zoomPitch = Mth.clamp((float)zoomPitch, (float)-90.0f, (float)90.0f);
        }
    }

    public static void resetZoomPitch() {
        zoomPitch = -4.0f;
        lastMouseY = 0.0;
        firstFrame = true;
    }

    public static void zoomCamera(Minecraft client, LocalPlayer clientPlayer, boolean canZoom) {
        ZoomJob job = ZoomJob.NONE;
        if (!Config.ZOOM_CAMERA && canZoom) {
            job = ZoomJob.START;
        }
        if (Config.ZOOM_CAMERA) {
            client.options.setCameraType(CameraType.FIRST_PERSON);
            job = Config.CAMERA_RETURN_BEHAVIOR || AutoZoomInOut.isCancel() ? ZoomJob.RETURN_PITCH : ZoomJob.RETURN_CONTINUE;
        }
        switch (job.ordinal()) {
            case 0: {
                CameraType perspective = Config.ZOOM_VIEW == Config.ReconView.FIRST_PERSON ? CameraType.FIRST_PERSON : CameraType.THIRD_PERSON_BACK;
                ZoomState.setValue(client, clientPlayer);
                client.options.setCameraType(perspective);
                TEMPORARY_GAZE_ZOOM = true;
                TickScheduler.setDelayTask(3, ZoomState::reset1);
                break;
            }
            case 1: {
                clientPlayer.setXRot(zoomPitch);
                TEMPORARY_GAZE_ZOOM_FOV = true;
                TickScheduler.setDelayTask(3, ZoomState::reset2);
                break;
            }
            case 2: {
                TEMPORARY_GAZE_ZOOM_RETURN = true;
                TickScheduler.setDelayTask(3, ZoomState::reset3);
                break;
            }
        }
        if (job != ZoomJob.NONE) {
            Config.ZOOM_CAMERA = !Config.ZOOM_CAMERA;
            String str = Config.ZOOM_CAMERA ? "On" : "Off";
            MutableComponent msg = Component.literal((String)("Recon Camera Mode: " + str));
            clientPlayer.sendSystemMessage((Component)msg.withStyle(ChatFormatting.GREEN).withStyle(ChatFormatting.BOLD));
        }
    }

    public static void init() {
        ZoomState.resetZoomPitch();
    }

    private static boolean closeValueComparison(float currentValue, float targetValue, float diff) {
        return currentValue < targetValue + diff && currentValue > targetValue - diff;
    }

    public static float lerpOnTickTime(float start, float end, float delta, int tick) {
        if (tick == 0) {
            tick = 1;
        }
        return start + delta * (end - start) / (float)tick;
    }

    private static void reset1() {
        TEMPORARY_GAZE_ZOOM = false;
    }

    private static void reset2() {
        if (TEMPORARY_GAZE_ZOOM_FOV) {
            TEMPORARY_GAZE_ZOOM_FOV = false;
            Config.ZOOM_CAMERA = false;
            ZoomState.resetZoomPitch();
        }
    }

    private static void reset3() {
        if (TEMPORARY_GAZE_ZOOM_RETURN) {
            TEMPORARY_GAZE_ZOOM_RETURN = false;
            Config.ZOOM_CAMERA = false;
            ZoomState.resetZoomPitch();
        }
    }

    public static void reset4() {
        TEMPORARY_GAZE_ZOOM_FOV_IN_OUT = false;
    }

    public static void setValue(Minecraft client, LocalPlayer clientPlayer) {
        ZoomState.resetZoomPitch();
        zoomPitch = clientPlayer.getXRot();
        zoomFov = ((Integer)client.options.fov().get()).intValue();
    }

    public static void renderCrosshair(GuiGraphicsExtractor drawContext) {
        if (Utils.shouldRenderCustomCrosshair()) {
            drawContext.blitSprite(RenderPipelines.CROSSHAIR, Config.CUSTOM_CROSSHAIR_TEXTURE, (drawContext.guiWidth() - 15) / 2, (drawContext.guiHeight() - 15) / 2, 15, 15);
        } else {
            drawContext.blitSprite(RenderPipelines.CROSSHAIR, Config.CROSSHAIR_TEXTURE, (drawContext.guiWidth() - 15) / 2, (drawContext.guiHeight() - 15) / 2, 15, 15);
        }
    }

    public static enum ZoomJob {
        START,
        RETURN_PITCH,
        RETURN_CONTINUE,
        NONE;

    }
}
