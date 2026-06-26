/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.BucketItem
 *  net.minecraft.world.item.EnderEyeItem
 *  net.minecraft.world.item.EnderpearlItem
 *  net.minecraft.world.item.FishingRodItem
 *  net.minecraft.world.item.FlintAndSteelItem
 *  net.minecraft.world.item.HoeItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.KnowledgeBookItem
 *  net.minecraft.world.item.MapItem
 *  net.minecraft.world.item.PotionItem
 *  net.minecraft.world.item.ProjectileItem
 *  net.minecraft.world.item.ProjectileWeaponItem
 *  net.minecraft.world.item.ShieldItem
 *  net.minecraft.world.item.ShovelItem
 *  net.minecraft.world.item.SnowballItem
 *  net.minecraft.world.item.ThrowablePotionItem
 *  net.minecraft.world.item.WindChargeItem
 *  net.minecraft.world.item.WritableBookItem
 *  net.minecraft.world.item.WrittenBookItem
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.Debug;
import com.papack.maceattackassistance.client.DebugScreen;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.WallClimbing;
import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.KnowledgeBookItem;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.item.WindChargeItem;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ElytraBoost {
    private static InteractionHand windChargeHand;
    private static int cooldownCounter;
    public static boolean flag_elytra_boost;

    public static void elytraBoost(Minecraft client, LocalPlayer clientPlayer) {
        if (cooldownCounter > 0) {
            --cooldownCounter;
        }
        if (Config.ELYTRA_BOOST && cooldownCounter == 0 && clientPlayer.isFallFlying() && client.options.keyJump.isDown() && (!client.options.keyAttack.isDown() || client.crosshairPickEntity == null) && JobManager.checkOrderIsEmpty()) {
            ClientLevel world = client.level;
            if (world == null) {
                return;
            }
            Vec3 playerVelocity = clientPlayer.getDeltaMovement();
            double flyingSpeed = playerVelocity.length();
            double horizontalSpeed = ElytraBoost.getHorizontalSpeed(playerVelocity);
            if (flyingSpeed >= 0.173 && horizontalSpeed >= 0.27 && ElytraBoost.shouldTrigger(client, clientPlayer) && windChargeHand != null) {
                double y = clientPlayer.getY();
                double fractional = y - Math.floor(y);
                double vy = -playerVelocity.y();
                int tick = vy != 0.0 ? (int)Math.ceil(fractional / vy) : 0;
                DebugScreen.lastY = y;
                Debug.previous_y = -64.0;
                ElytraBoost.elytraBoostValueSetting(tick);
                cooldownCounter = Config.COOL_DOWN_TICKS;
                JobManager.setOrder(StatusType.ELYTRA_BOOST, clientPlayer.getInventory().getSelectedSlot());
            }
        }
    }

    private static boolean shouldTrigger(Minecraft client, LocalPlayer clientPlayer) {
        WallClimbing.ClimbingStatus climbingStatus;
        windChargeHand = Utils.getHandHoldingWindCharge(client, clientPlayer);
        if (windChargeHand == null) {
            if (Config.AUTO_WIND_CHARGE_SELECT) {
                if (Utils.isSuccessFoundItItemInHotbar(WindChargeItem.class, true)) {
                    windChargeHand = InteractionHand.MAIN_HAND;
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

    public static double getHorizontalSpeed(Vec3 velocity) {
        double vx = velocity.x();
        double vz = velocity.z();
        return Math.sqrt(vx * vx + vz * vz);
    }

    public static boolean isNotUsableItems(Minecraft client, ItemStack itemStack) {
        LocalPlayer player = client.player;
        if (player == null) {
            return false;
        }
        Item item = itemStack.getItem();
        HitResult hitResult = client.hitResult;
        BlockPos targetPos = null;
        boolean result = hitResult != null;
        boolean isCooldown = ElytraBoost.isCooldown(player, itemStack);
        if (isCooldown) {
            return true;
        }
        if (result) {
            targetPos = BlockPos.containing((Position)hitResult.getLocation());
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
        if (item instanceof ProjectileWeaponItem) {
            return false;
        }
        if (item instanceof EnderpearlItem) {
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
        if (item instanceof MapItem) {
            return false;
        }
        if (itemStack.is(ItemTags.CHEST_ARMOR)) {
            return false;
        }
        if (itemStack.is(ItemTags.HEAD_ARMOR)) {
            return false;
        }
        if (itemStack.is(ItemTags.LEG_ARMOR)) {
            return false;
        }
        if (itemStack.is(ItemTags.FOOT_ARMOR)) {
            return false;
        }
        if (itemStack.is(Items.ELYTRA)) {
            return false;
        }
        if (item.getDescriptionId().contains("_bucket")) {
            return false;
        }
        if (itemStack.get(DataComponents.FOOD) != null) {
            return false;
        }
        return targetPos == null || !(item instanceof BlockItem);
    }

    public static boolean isElytraBoostIdle() {
        return cooldownCounter <= 0;
    }

    private static boolean isCooldown(LocalPlayer player, ItemStack itemStack) {
        if (itemStack.is(Items.AIR)) {
            return true;
        }
        return player.getCooldowns().isOnCooldown(itemStack);
    }

    static {
        cooldownCounter = 0;
        flag_elytra_boost = false;
    }
}
