package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;

public class PearlCatch {
    private static boolean active = false;
    private static int savedSlot = -1;
    private static int tickCounter = 0;
    private static int delayCounter = 0;
    private static boolean windChargeFound = false;

    public static boolean isActive() {
        return active;
    }

    public static void activate(ClientPlayerEntity clientPlayer) {
        if (active) return;
        if (!Config.PEARL_CATCH) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || clientPlayer == null) return;

        int pearlSlot = findEnderPearlSlot(clientPlayer);
        if (pearlSlot < 0) return;

        if (!hasWindCharge(clientPlayer)) return;

        savedSlot = clientPlayer.getInventory().getSelectedSlot();
        active = true;
        tickCounter = 5;
        delayCounter = 0;
        windChargeFound = false;

        clientPlayer.getInventory().setSelectedSlot(pearlSlot);
    }

    public static void tick(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        if (!active || clientPlayer == null) return;

        ClientPlayerInteractionManager interactionManager = client.interactionManager;
        if (interactionManager == null) return;

        switch (tickCounter) {
            case 5: {
                break;
            }
            case 4: {
                interactionManager.interactItem(clientPlayer, Hand.MAIN_HAND);
                break;
            }
            case 3: {
                delayCounter = Config.PEARL_CATCH_DELAY;
                break;
            }
            case 2: {
                if (delayCounter > 0) {
                    delayCounter--;
                    return;
                }
                break;
            }
            case 1: {
                int windChargeSlot = findWindChargeSlot(clientPlayer);
                if (windChargeSlot < 0) {
                    deactivate();
                    return;
                }

                clientPlayer.getInventory().setSelectedSlot(windChargeSlot);

                Entity pearl = Utils.findNearestEnderPearl(client, clientPlayer);
                if (pearl != null) {
                    Vec3d playerEyePos = clientPlayer.getEyePos();
                    Vec3d pearlPos = pearl.getEntityPos();
                    double distance = playerEyePos.distanceTo(pearlPos);

                    if (distance <= Config.PEARL_CATCH_RANGE) {
                        Vec3d pearlVel = pearl.getVelocity();
                        int bestTick = findBestInterceptTick(playerEyePos, pearlPos, pearlVel);

                        Vec3d targetPos = Utils.simulateFuturePos(pearlPos, pearlVel, bestTick);

                        double dx = targetPos.x - playerEyePos.x;
                        double dy = targetPos.y - playerEyePos.y;
                        double dz = targetPos.z - playerEyePos.z;
                        double distXZ = Math.sqrt(dx * dx + dz * dz);
                        float yaw = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
                        float pitch = (float)(-Math.toDegrees(Math.atan2(dy, distXZ)));

                        clientPlayer.setYaw(yaw);
                        clientPlayer.setPitch(pitch);
                    }
                }

                interactionManager.interactItem(clientPlayer, Hand.MAIN_HAND);
                break;
            }
            case 0: {
                deactivate();
                return;
            }
        }

        tickCounter--;
    }

    private static int findBestInterceptTick(Vec3d playerEyePos, Vec3d pearlPos, Vec3d pearlVel) {
        int bestTick = 3;
        double bestDist = Double.MAX_VALUE;

        for (int t = 1; t <= 10; t++) {
            Vec3d predictedPearl = Utils.simulateFuturePos(pearlPos, pearlVel, t);

            Vec3d dirToTarget = predictedPearl.subtract(playerEyePos);
            double dist = dirToTarget.length();
            if (dist < 0.5) continue;

            Vec3d throwVel = dirToTarget.normalize().multiply(1.5);
            Vec3d wcPos = playerEyePos;
            Vec3d wcVel = throwVel;

            for (int s = 0; s < t; s++) {
                wcVel = wcVel.multiply(0.99, 0.99, 0.99);
                wcVel = wcVel.add(0.0, -0.03, 0.0);
                wcPos = wcPos.add(wcVel);
            }

            double separation = wcPos.distanceTo(predictedPearl);
            if (separation < bestDist) {
                bestDist = separation;
                bestTick = t;
            }
        }

        return bestTick;
    }

    public static void deactivate() {
        if (!active) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null && savedSlot >= 0) {
            client.player.getInventory().setSelectedSlot(savedSlot);
        }

        active = false;
        savedSlot = -1;
        tickCounter = 0;
        delayCounter = 0;
        windChargeFound = false;
    }

    private static int findEnderPearlSlot(ClientPlayerEntity clientPlayer) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = clientPlayer.getInventory().getStack(i);
            if (stack.isOf(Items.ENDER_PEARL)) {
                return i;
            }
        }
        return -1;
    }

    private static int findWindChargeSlot(ClientPlayerEntity clientPlayer) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = clientPlayer.getInventory().getStack(i);
            if (stack.isOf(Items.WIND_CHARGE)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean hasWindCharge(ClientPlayerEntity clientPlayer) {
        return findWindChargeSlot(clientPlayer) >= 0;
    }
}
