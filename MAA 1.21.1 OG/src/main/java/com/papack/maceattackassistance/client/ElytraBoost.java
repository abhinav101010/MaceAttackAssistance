/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.item.ArmorItem
 *  net.minecraft.item.BlockItem
 *  net.minecraft.item.BucketItem
 *  net.minecraft.item.ElytraItem
 *  net.minecraft.item.EnderPearlItem
 *  net.minecraft.item.EnderEyeItem
 *  net.minecraft.item.FlintAndSteelItem
 *  net.minecraft.item.FishingRodItem
 *  net.minecraft.item.Item
 *  net.minecraft.item.HoeItem
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.KnowledgeBookItem
 *  net.minecraft.item.Items
 *  net.minecraft.item.FilledMapItem
 *  net.minecraft.item.RangedWeaponItem
 *  net.minecraft.item.PotionItem
 *  net.minecraft.item.ShieldItem
 *  net.minecraft.item.ShovelItem
 *  net.minecraft.item.SnowballItem
 *  net.minecraft.item.WritableBookItem
 *  net.minecraft.item.WrittenBookItem
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Position
 *  net.minecraft.util.hit.HitResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.item.ThrowablePotionItem
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.item.ProjectileItem
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.Debug;
import com.papack.maceattackassistance.client.DebugScreen;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.WallClimbing;
import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.util.Hand;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.KnowledgeBookItem;
import net.minecraft.item.Items;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.PotionItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SnowballItem;
import net.minecraft.item.WritableBookItem;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ProjectileItem;

public class ElytraBoost {
    private static Hand windChargeHand;
    private static int cooldownCounter;
    public static boolean flag_elytra_boost;

    public static void elytraBoost(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        if (cooldownCounter > 0) {
            --cooldownCounter;
        }
        if (Config.JUMP_ASSIST && cooldownCounter == 0 && clientPlayer.isFallFlying() && client.options.jumpKey.isPressed() && (!client.options.attackKey.isPressed() || client.targetedEntity == null)) {
            ClientWorld world = client.world;
            if (world == null) {
                return;
            }
            Vec3d playerVelocity = clientPlayer.getVelocity();
            double flyingSpeed = playerVelocity.length();
            double horizontalSpeed = ElytraBoost.getHorizontalSpeed(playerVelocity);
            if (flyingSpeed >= 0.173 && horizontalSpeed >= 0.27 && ElytraBoost.shouldTrigger(client, clientPlayer) && windChargeHand != null) {
                double y = clientPlayer.getY();
                double fractional = y - Math.floor(y);
                double vy = -playerVelocity.getY();
                int tick = vy != 0.0 ? (int)Math.ceil(fractional / vy) : 0;
                DebugScreen.lastY = y;
                Debug.previous_y = -64.0;
                ElytraBoost.elytraBoostValueSetting(tick);
                cooldownCounter = Config.COOL_DOWN_TICKS;
                JobManager.setOrder(StatusType.ELYTRA_BOOST, clientPlayer.getInventory().selectedSlot);
            }
        }
    }

    private static boolean shouldTrigger(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        windChargeHand = Utils.getHandHoldingWindCharge(client, clientPlayer);
        if (windChargeHand == null) {
            return false;
        }
        WallClimbing.ClimbingStatus climbingStatus = WallClimbing.getClimbingStatus(clientPlayer, -1);
        if (climbingStatus.canClimbing() && climbingStatus.offset() == 1) {
            WallClimbing.resetClimbingStatus();
            return true;
        }
        return false;
    }

    private static void elytraBoostValueSetting(int tick) {
        int boostThrow;
        JobManager.setBoostValue(switch (tick) {
            case 0, 1, 2, 3 -> {
                boostThrow = 5;
                yield 4;
            }
            default -> {
                boostThrow = 4;
                yield 3;
            }
        }, boostThrow);
    }

    public static double getHorizontalSpeed(Vec3d velocity) {
        double vx = velocity.getX();
        double vz = velocity.getZ();
        return Math.sqrt(vx * vx + vz * vz);
    }

    public static boolean isNotUsableItems(MinecraftClient client, ItemStack itemStack) {
        Item item = itemStack.getItem();
        HitResult hitResult = client.crosshairTarget;
        BlockPos targetPos = null;
        if (hitResult != null) {
            targetPos = BlockPos.ofFloored((Position)hitResult.getPos());
        }
        if (item instanceof ProjectileItem) {
            return false;
        }
        if (item instanceof RangedWeaponItem) {
            return false;
        }
        if (item instanceof ShieldItem) {
            return false;
        }
        if (item instanceof EnderPearlItem) {
            return false;
        }
        if (item instanceof EnderEyeItem) {
            return false;
        }
        if (item instanceof HoeItem) {
            return false;
        }
        if (item instanceof ShovelItem) {
            return false;
        }
        if (item instanceof ArmorItem && !itemStack.isOf(Items.WOLF_ARMOR)) {
            return false;
        }
        if (item instanceof ElytraItem) {
            return false;
        }
        if (item instanceof BucketItem) {
            return false;
        }
        if (item instanceof FishingRodItem) {
            return false;
        }
        if (item instanceof FlintAndSteelItem) {
            return false;
        }
        if (item instanceof PotionItem) {
            return false;
        }
        if (item instanceof ThrowablePotionItem) {
            return false;
        }
        if (item instanceof SnowballItem) {
            return false;
        }
        if (item instanceof WritableBookItem) {
            return false;
        }
        if (item instanceof WrittenBookItem) {
            return false;
        }
        if (item instanceof KnowledgeBookItem) {
            return false;
        }
        if (item instanceof FilledMapItem) {
            return false;
        }
        if (item.getTranslationKey().contains("_bucket")) {
            return false;
        }
        if (itemStack.get(DataComponentTypes.FOOD) != null) {
            return false;
        }
        return targetPos == null || !(item instanceof BlockItem);
    }

    public static boolean isElytraBoostIdle() {
        return cooldownCounter <= 0;
    }

    static {
        cooldownCounter = 0;
        flag_elytra_boost = false;
    }
}
