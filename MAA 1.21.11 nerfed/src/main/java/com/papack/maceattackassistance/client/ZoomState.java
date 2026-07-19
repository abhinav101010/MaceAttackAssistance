/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.RenderPipelines
 *  net.minecraft.Formatting
 *  net.minecraft.Text
 *  net.minecraft.MinecraftClient
 *  net.minecraft.DrawContext
 *  net.minecraft.MathHelper
 *  net.minecraft.MutableText
 *  net.minecraft.Perspective
 *  net.minecraft.ClientPlayerEntity
 *  net.minecraft.RenderTickCounter
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.AutoZoomInOut;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.util.Formatting;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import net.minecraft.text.MutableText;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;

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

    public static void moveCamera(MinecraftClient client, ClientPlayerEntity clientPlayer, RenderTickCounter context) {
        float tickDelta;
        if (TEMPORARY_GAZE_BOOST) {
            tickDelta = context.getDynamicDeltaTicks();
            float angle = gazeCounter > 1 ? 0.0f : (float)((double)Config.REFLECTION_ANGLE * (1.0 - 0.5 * (double)gazeCounter));
            zoomPitch = Utils.lerpPitch(zoomPitch, angle, tickDelta);
        }
        if (TEMPORARY_GAZE_ZOOM) {
            tickDelta = context.getDynamicDeltaTicks();
            zoomPitch = ZoomState.lerpOnTickTime(zoomPitch, -4.0f, tickDelta, 2);
            if (TEMPORARY_GAZE_ZOOM && ZoomState.closeValueComparison(zoomPitch, -4.0f, 0.2f)) {
                ZoomState.reset1();
            }
        }
        if (TEMPORARY_GAZE_ZOOM_FOV) {
            tickDelta = context.getDynamicDeltaTicks();
            zoomFov = ZoomState.lerpOnTickTime(zoomFov, ((Integer)client.options.getFov().getValue()).intValue(), tickDelta, 2);
            if (TEMPORARY_GAZE_ZOOM_FOV && ZoomState.closeValueComparison(zoomFov, ((Integer)client.options.getFov().getValue()).intValue(), 0.2f)) {
                ZoomState.reset2();
            }
        }
        if (TEMPORARY_GAZE_ZOOM_RETURN) {
            tickDelta = context.getDynamicDeltaTicks();
            zoomPitch = ZoomState.lerpOnTickTime(zoomPitch, clientPlayer.getPitch(), tickDelta, 2);
            zoomFov = ZoomState.lerpOnTickTime(zoomFov, ((Integer)client.options.getFov().getValue()).intValue(), tickDelta, 2);
            if (TEMPORARY_GAZE_ZOOM_RETURN && ZoomState.closeValueComparison(zoomPitch, clientPlayer.getPitch(), 0.3f) && ZoomState.closeValueComparison(zoomFov, ((Integer)client.options.getFov().getValue()).intValue(), 0.2f)) {
                ZoomState.reset3();
            }
        }
        if (TEMPORARY_GAZE_ZOOM_FOV_IN_OUT) {
            tickDelta = context.getDynamicDeltaTicks();
            zoomFov = ZoomState.lerpOnTickTime(zoomFov, zoomFovInOut, tickDelta, 2);
            if (TEMPORARY_GAZE_ZOOM_FOV_IN_OUT && ZoomState.closeValueComparison(zoomFov, zoomFovInOut, 0.2f)) {
                ZoomState.reset4();
            }
        }
        if (Config.ZOOM_CAMERA) {
            clientPlayer.setPitch(-4.0f);
            if (!zoomMode || !client.isWindowFocused()) {
                firstFrame = true;
                return;
            }
            double mouseY = client.mouse.getY();
            if (firstFrame) {
                lastMouseY = mouseY;
                firstFrame = false;
                return;
            }
            double deltaY = mouseY - lastMouseY;
            lastMouseY = mouseY;
            float sensitivity = ((Double)client.options.getMouseSensitivity().getValue()).floatValue();
            float multiplier = sensitivity * 0.15f;
            if (client.currentScreen == null) {
                zoomPitch += (float)(deltaY * (double)multiplier);
            }
            zoomPitch = MathHelper.clamp((float)zoomPitch, (float)-90.0f, (float)90.0f);
        }
    }

    public static void resetZoomPitch() {
        zoomPitch = -4.0f;
        lastMouseY = 0.0;
        firstFrame = true;
    }

    public static void zoomCamera(MinecraftClient client, ClientPlayerEntity clientPlayer, boolean canZoom) {
        ZoomJob job = ZoomJob.NONE;
        if (!Config.ZOOM_CAMERA && canZoom) {
            job = ZoomJob.START;
        }
        if (Config.ZOOM_CAMERA) {
            client.options.setPerspective(Perspective.FIRST_PERSON);
            job = Config.CAMERA_RETURN_BEHAVIOR || AutoZoomInOut.isCancel() ? ZoomJob.RETURN_PITCH : ZoomJob.RETURN_CONTINUE;
        }
        switch (job.ordinal()) {
            case 0: {
                Perspective perspective = Config.ZOOM_VIEW == Config.ReconView.FIRST_PERSON ? Perspective.FIRST_PERSON : Perspective.THIRD_PERSON_BACK;
                ZoomState.setValue(client, clientPlayer);
                client.options.setPerspective(perspective);
                TEMPORARY_GAZE_ZOOM = true;
                TickScheduler.setDelayTask(3, ZoomState::reset1);
                break;
            }
            case 1: {
                clientPlayer.setPitch(zoomPitch);
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
            MutableText msg = Text.literal((String)("Recon Camera Mode: " + str));
            clientPlayer.sendMessage((Text)msg.formatted(Formatting.GREEN).formatted(Formatting.BOLD), true);
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

    public static void setValue(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        ZoomState.resetZoomPitch();
        zoomPitch = clientPlayer.getPitch();
        zoomFov = ((Integer)client.options.getFov().getValue()).intValue();
    }

    public static void renderCrosshair(DrawContext drawContext) {
        if (Utils.shouldRenderCustomCrosshair()) {
            drawContext.drawGuiTexture(RenderPipelines.CROSSHAIR, Config.CUSTOM_CROSSHAIR_TEXTURE, (drawContext.getScaledWindowWidth() - 15) / 2, (drawContext.getScaledWindowHeight() - 15) / 2, 15, 15);
        } else {
            drawContext.drawGuiTexture(RenderPipelines.CROSSHAIR, Config.CROSSHAIR_TEXTURE, (drawContext.getScaledWindowWidth() - 15) / 2, (drawContext.getScaledWindowHeight() - 15) / 2, 15, 15);
        }
    }

    public static enum ZoomJob {
        START,
        RETURN_PITCH,
        RETURN_CONTINUE,
        NONE;

    }

    public static class MAAClientState {
        private static final int DEFAULT_LEVEL = 1;
        public static boolean receivedFirstMessage = false;
        public static int allowedLevels = 1;
        public static boolean antiCheat = false;

        public static void clear() {
            receivedFirstMessage = false;
            allowedLevels = 1;
            antiCheat = false;
        }
    }

    public static class KeyManager {
        public static boolean keyManager() {
            return !JobManager.JUMP_OPTION && !JobManager.jumpOption();
        }
    }
}
