/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants$Key
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.item.EnderpearlItem
 *  net.minecraft.world.item.WindChargeItem
 *  net.minecraft.world.phys.Vec2
 */
package com.papack.maceattackassistance.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.papack.maceattackassistance.client.InstantAim;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.mixin.KeyMappingInvoker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.WindChargeItem;
import net.minecraft.world.phys.Vec2;

public class PearlCatch {
    private static final float MaxAdjustValue = 17.0f;
    private static float prevPitch;

    public static float getPrevPitch() {
        return prevPitch;
    }

    private static void setPrevPitch(LocalPlayer player) {
        prevPitch = player.getXRot();
    }

    public static void autoPearlCatch(int value) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;
        if (player == null) {
            return;
        }
        if (value == 8) {
            PearlCatch.setPrevPitch(player);
            if (Config.ALWAYS_USE_UPWARD_ANGLE || PearlCatch.getPrevPitch() > (float)Config.PEARL_CATCH_UPWARD_ANGLE) {
                InstantAim.startInstantAim(Config.PEARL_CATCH_UPWARD_ANGLE, 80L);
            } else {
                value = 6;
            }
        }
        if (value == 5) {
            int pearlSlot = Utils.findItemInHotbar(EnderpearlItem.class, true);
            if (pearlSlot == -1) {
                value = 1;
            } else {
                PearlCatch.setAndThrow(client, pearlSlot);
            }
        }
        if (value == 3 - Config.PEARL_CATCH_DELAY) {
            float currentPitch = player.getXRot();
            int chargeSlot = Utils.findItemInHotbar(WindChargeItem.class, true);
            if (currentPitch <= -15.0f && chargeSlot > -1) {
                Entity enderPearl = Utils.findNearestEnderPearl(client, player);
                if (enderPearl != null) {
                    Vec2 yawPitch = Utils.getLookAngles(player, enderPearl.position());
                    player.setYRot(yawPitch.x);
                }
                player.setXRot(currentPitch + PearlCatch.getAdjustment(currentPitch));
                PearlCatch.setAndThrow(client, chargeSlot);
            }
        }
    }

    public static void instantPearlCatch(int value) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;
        if (player == null) {
            return;
        }
        if (value == 8) {
            PearlCatch.setPrevPitch(player);
            InstantAim.startInstantAim(-90.0f, 80L);
        }
        if (value == 6) {
            int pearlSlot = Utils.findItemInHotbar(EnderpearlItem.class, true);
            PearlCatch.setAndThrow(client, pearlSlot);
        }
        if (value == 4 - Config.PEARL_CATCH_DELAY) {
            int chargeSlot = Utils.findItemInHotbar(WindChargeItem.class, true);
            PearlCatch.setAndThrow(client, chargeSlot);
        }
        if (value == 1) {
            InstantAim.startInstantAim(PearlCatch.getPrevPitch(), 80L);
        }
    }

    public static void setAndThrow(Minecraft client, int slot) {
        if (slot > -1) {
            KeyMapping.click((InputConstants.Key)((KeyMappingInvoker)client.options.keyHotbarSlots[slot]).accessorBoundKey());
            KeyMapping.click((InputConstants.Key)((KeyMappingInvoker)client.options.keyUse).accessorBoundKey());
        }
    }

    public static float getAdjustment(float pitch) {
        float maxAdjustment = 17.0f + (float)(3 * Config.PEARL_CATCH_DELAY);
        if (pitch >= 0.0f) {
            return maxAdjustment;
        }
        if (pitch <= -85.0f) {
            return 0.0f;
        }
        float t = -pitch / 85.0f;
        return maxAdjustment * (1.0f - t);
    }

    public static void downwardPearlCatch(int value) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;
        if (player == null) {
            return;
        }
        if (value == 8) {
            PearlCatch.setPrevPitch(player);
            if (Config.ALWAYS_USE_DOWNWARD_ANGLE || PearlCatch.getPrevPitch() < (float)Config.PEARL_CATCH_DOWNWARD_ANGLE) {
                InstantAim.startInstantAim(Config.PEARL_CATCH_DOWNWARD_ANGLE, 80L);
            } else {
                value = 6;
            }
        }
        if (value == 5) {
            int chargeSlot = Utils.findItemInHotbar(WindChargeItem.class, true);
            if (chargeSlot == -1) {
                value = 1;
            } else {
                PearlCatch.setAndThrow(client, chargeSlot);
            }
        }
        if (value == 4) {
            float pearlPitch = PearlCatch.getPearlPitch(player.getXRot());
            int pearlSlot = Utils.findItemInHotbar(EnderpearlItem.class, true);
            player.setXRot(pearlPitch);
            PearlCatch.setAndThrow(client, pearlSlot);
        }
    }

    public static float calculateAdjustmentAngle(float wcPitch) {
        if (wcPitch >= 90.0f) {
            return 0.0f;
        }
        if (wcPitch <= 45.0f) {
            return 10.5f;
        }
        if (wcPitch >= 75.0f) {
            return PearlCatch.interpolate(wcPitch, 75.0f, 90.0f, 2.0f, 0.0f);
        }
        if (wcPitch >= 60.0f) {
            return PearlCatch.interpolate(wcPitch, 60.0f, 75.0f, 6.0f, 2.0f);
        }
        if (wcPitch >= 47.5f) {
            return PearlCatch.interpolate(wcPitch, 47.5f, 60.0f, 9.0f, 6.0f);
        }
        if (wcPitch >= 45.0f) {
            return PearlCatch.interpolate(wcPitch, 45.0f, 47.5f, 10.5f, 9.0f);
        }
        return 0.0f;
    }

    private static float interpolate(float x, float x1, float x2, float y1, float y2) {
        return y1 + (x - x1) * (y2 - y1) / (x2 - x1);
    }

    public static float getPearlPitch(float wcPitch) {
        return wcPitch - PearlCatch.calculateAdjustmentAngle(wcPitch);
    }
}
