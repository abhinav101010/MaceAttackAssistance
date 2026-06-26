/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants$Key
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.Entity
 */
package com.papack.maceattackassistance.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.papack.maceattackassistance.client.PearlGrapple;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.mixin.KeyMappingInvoker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;

public class KnockbackDisplacement {
    private static int gazeShiftTicks;
    private static boolean attacked;
    private static float offsetYawKnockback;
    private static boolean useOffset;
    private static boolean active;
    private static int tick;
    private static boolean prevBreach;
    private static float startYawKnockback;
    private static float targetYawKnockback;

    public static void trigger() {
        boolean beyondOffset;
        if (active) {
            return;
        }
        Minecraft client = Minecraft.getInstance();
        LocalPlayer localPlayer = client.player;
        if (!(localPlayer instanceof LocalPlayer)) {
            return;
        }
        LocalPlayer player = localPlayer;
        Entity target = PearlGrapple.findNearestMobForGrapple(Config.KBD_RANGE);
        if (target == null) {
            return;
        }
        boolean shouldAttack = player.distanceTo(target) <= 3.0f;
        startYawKnockback = player.getYRot();
        targetYawKnockback = KnockbackDisplacement.getTargetYaw(player, target);
        float delta = KnockbackDisplacement.wrapDegrees(startYawKnockback - targetYawKnockback);
        boolean right = delta > 0.0f;
        float offset = right ? (float)Config.KBD_FLICK_ANGLE : (float)(-Config.KBD_FLICK_ANGLE);
        offsetYawKnockback = KnockbackDisplacement.wrapDegrees(targetYawKnockback + offset);
        boolean bl = beyondOffset = Math.abs(delta) >= Math.abs(offset);
        if (shouldAttack && (beyondOffset || !Config.KBD_AUTO_FLICK) || !shouldAttack && Config.KBD_INSTANT_AIM) {
            useOffset = false;
            gazeShiftTicks = 1;
            if (shouldAttack) {
                KeyMapping.click((InputConstants.Key)((KeyMappingInvoker)client.options.keyAttack).accessorBoundKey());
            }
            attacked = true;
        } else if (shouldAttack) {
            useOffset = true;
            gazeShiftTicks = 2;
            attacked = false;
        } else {
            return;
        }
        active = true;
        tick = 0;
        prevBreach = Config.BREACH_SWAP;
        Config.BREACH_SWAP = false;
    }

    private static float getTargetYaw(LocalPlayer player, Entity target) {
        double dx = target.getX() - player.getX();
        double dz = target.getZ() - player.getZ();
        return (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
    }

    public static void onClientTickForKnockBack(Minecraft client) {
        if (!active) {
            return;
        }
        if (useOffset && tick == 0 && !attacked) {
            KeyMapping.click((InputConstants.Key)((KeyMappingInvoker)client.options.keyAttack).accessorBoundKey());
            attacked = true;
        }
        if (++tick > gazeShiftTicks) {
            active = false;
            Config.BREACH_SWAP = prevBreach;
        }
    }

    public static void onRenderTickForKnockBack(Minecraft client, float tickDelta) {
        float yaw;
        if (!active) {
            return;
        }
        LocalPlayer localPlayer = client.player;
        if (!(localPlayer instanceof LocalPlayer)) {
            return;
        }
        LocalPlayer player = localPlayer;
        if (!useOffset) {
            float t = Math.min(1.0f, (float)tick + tickDelta);
            float eased = KnockbackDisplacement.ease(t);
            yaw = KnockbackDisplacement.lerpAngle(startYawKnockback, targetYawKnockback, eased);
        } else if (tick == 0) {
            float eased = KnockbackDisplacement.ease(tickDelta);
            yaw = KnockbackDisplacement.lerpAngle(startYawKnockback, offsetYawKnockback, eased);
        } else if (tick == 1) {
            float eased = KnockbackDisplacement.ease(tickDelta);
            yaw = KnockbackDisplacement.lerpAngle(offsetYawKnockback, targetYawKnockback, eased);
        } else {
            yaw = targetYawKnockback;
        }
        player.setYRot(yaw);
        player.setYHeadRot(yaw);
    }

    private static float ease(float t) {
        return (float)Math.sin(Math.PI * (double)t * 0.5);
    }

    private static float lerpAngle(float start, float end, float t) {
        float delta = KnockbackDisplacement.wrapDegrees(end - start);
        return start + delta * t;
    }

    private static float wrapDegrees(float degrees) {
        if ((degrees %= 360.0f) >= 180.0f) {
            degrees -= 360.0f;
        }
        if (degrees < -180.0f) {
            degrees += 360.0f;
        }
        return degrees;
    }

    static {
        attacked = false;
        active = false;
        tick = 0;
    }
}
