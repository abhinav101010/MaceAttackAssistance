/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.Hand
 *  net.minecraft.BlockItem
 *  net.minecraft.BucketItem
 *  net.minecraft.EnderPearlItem
 *  net.minecraft.EnderEyeItem
 *  net.minecraft.FlintAndSteelItem
 *  net.minecraft.FishingRodItem
 *  net.minecraft.Item
 *  net.minecraft.HoeItem
 *  net.minecraft.ItemStack
 *  net.minecraft.KnowledgeBookItem
 *  net.minecraft.Items
 *  net.minecraft.FilledMapItem
 *  net.minecraft.RangedWeaponItem
 *  net.minecraft.PotionItem
 *  net.minecraft.ShieldItem
 *  net.minecraft.ShovelItem
 *  net.minecraft.SnowballItem
 *  net.minecraft.WritableBookItem
 *  net.minecraft.WrittenBookItem
 *  net.minecraft.BlockPos
 *  net.minecraft.Position
 *  net.minecraft.HitResult
 *  net.minecraft.HitResult$Type
 *  net.minecraft.Vec3d
 *  net.minecraft.MinecraftClient
 *  net.minecraft.ItemTags
 *  net.minecraft.ThrowablePotionItem
 *  net.minecraft.ClientWorld
 *  net.minecraft.ClientPlayerEntity
 *  net.minecraft.WindChargeItem
 *  net.minecraft.DataComponentTypes
 *  net.minecraft.ProjectileItem
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
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
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
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.WindChargeItem;
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
        if (Config.ELYTRA_BOOST && cooldownCounter == 0 && clientPlayer.isGliding() && client.options.jumpKey.isPressed() && (!client.options.attackKey.isPressed() || client.targetedEntity == null) && JobManager.checkOrderIsEmpty()) {
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
                JobManager.setOrder(StatusType.ELYTRA_BOOST, clientPlayer.getInventory().getSelectedSlot());
            }
        }
    }

    private static boolean shouldTrigger(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        WallClimbing.ClimbingStatus climbingStatus;
        windChargeHand = Utils.getHandHoldingWindCharge(client, clientPlayer);
        if (windChargeHand == null) {
            if (Config.AUTO_WIND_CHARGE_SELECT) {
                if (Utils.isSuccessFoundItItemInHotbar(WindChargeItem.class, true)) {
                    windChargeHand = Hand.MAIN_HAND;
                }
            } else {
                return false;
            }
        }
        if ((climbingStatus = WallClimbing.getClimbingStatus(clientPlayer, -1)).canClimbing() && climbingStatus.offset() == 1) {
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
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return false;
        }
        Item item = itemStack.getItem();
        HitResult hitResult = client.crosshairTarget;
        BlockPos targetPos = null;
        boolean result = hitResult != null;
        boolean isCooldown = ElytraBoost.isCooldown(player, itemStack);
        if (isCooldown) {
            return true;
        }
        if (result) {
            targetPos = BlockPos.ofFloored((Position)hitResult.getPos());
        }
        if (result && hitResult.getType() != HitResult.Type.MISS) {
            if (item instanceof HoeItem) {
                return false;
            }
            if (item instanceof ShovelItem) {
                return false;
            }
        }
        if (item instanceof ShieldItem) {
            return false;
        }
        if (item instanceof ProjectileItem) {
            return false;
        }
        if (item instanceof RangedWeaponItem) {
            return false;
        }
        if (item instanceof EnderPearlItem) {
            return false;
        }
        if (item instanceof EnderEyeItem) {
            return false;
        }
        if (item instanceof SnowballItem) {
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
        if (itemStack.isIn(ItemTags.CHEST_ARMOR)) {
            return false;
        }
        if (itemStack.isIn(ItemTags.HEAD_ARMOR)) {
            return false;
        }
        if (itemStack.isIn(ItemTags.LEG_ARMOR)) {
            return false;
        }
        if (itemStack.isIn(ItemTags.FOOT_ARMOR)) {
            return false;
        }
        if (itemStack.isOf(Items.ELYTRA)) {
            return false;
        }
        if (item.getTranslationKey().contains("_bucket")) {
            return false;
        }
        if (itemStack.contains(DataComponentTypes.FOOD)) {
            return false;
        }
        return targetPos == null || !(item instanceof BlockItem);
    }

    public static boolean isElytraBoostIdle() {
        return cooldownCounter <= 0;
    }

    private static boolean isCooldown(ClientPlayerEntity player, ItemStack itemStack) {
        if (itemStack.isOf(Items.AIR)) {
            return true;
        }
        return player.getItemCooldownManager().isCoolingDown(itemStack);
    }

    static {
        cooldownCounter = 0;
        flag_elytra_boost = false;
    }
}
