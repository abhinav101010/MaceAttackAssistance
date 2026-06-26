/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants
 *  com.mojang.blaze3d.platform.Window
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.multiplayer.PlayerInfo
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Holder$Reference
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.EquipmentSlot$Type
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.boss.enderdragon.EnderDragon
 *  net.minecraft.world.entity.boss.enderdragon.EnderDragonPart
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.ProjectileUtil
 *  net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl
 *  net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.WindChargeItem
 *  net.minecraft.world.item.enchantment.Enchantment
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.AirBlock
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec2
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.papack.maceattackassistance.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.papack.maceattackassistance.client.ElytraBoost;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.mixin.MultiPlayerGameModeInvoker;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WindChargeItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Utils {
    public static float getRange() {
        return (float)Config.ATTACK_RANGE * 0.1f;
    }

    public static float getRangeStun() {
        return (float)Config.ATTACK_RANGE_STUN * 0.1f;
    }

    public static float getRangeSpear() {
        return (float)Config.ATTACK_RANGE_SPEAR * 0.1f;
    }

    public static boolean canDoubleTap() {
        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer == null) {
            return false;
        }
        if (!Config.DOUBLE_TAP) {
            return false;
        }
        return clientPlayer.isFallFlying();
    }

    public static boolean checkPlayerUUID(Entity entity) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer clientPlayer = client.player;
        boolean chkUuid = false;
        if (clientPlayer != null) {
            chkUuid = entity.getUUID().equals(clientPlayer.getUUID());
        }
        return chkUuid;
    }

    public static boolean isInAttackableRange(LocalPlayer clientPlayer, Entity targetEntity) {
        if (clientPlayer != null && targetEntity != null) {
            double dz;
            double dx = clientPlayer.getX() - targetEntity.getX();
            return Math.sqrt(dx * dx + (dz = clientPlayer.getZ() - targetEntity.getZ()) * dz) <= (double)Utils.getRange();
        }
        return false;
    }

    public static boolean isNotUsingElytra(LocalPlayer clientPlayer) {
        // Fix: Player is not using elytra if they're not fall flying OR not wearing elytra
        return !clientPlayer.isFallFlying() || !clientPlayer.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA);
    }

    public static boolean isOnSlimeBlock(LocalPlayer clientPlayer) {
        BlockPos pos = BlockPos.containing((double)clientPlayer.getX(), (double)(clientPlayer.getY() - 1.0), (double)clientPlayer.getZ());
        BlockState state = clientPlayer.level().getBlockState(pos);
        return state.is(Blocks.SLIME_BLOCK);
    }

    public static boolean isSimpleVisibleFromPlayer(Minecraft client, LocalPlayer clientPlayer, Entity target) {
        Vec3 playerEyePos = clientPlayer.getEyePosition();
        Vec3 targetEyePos = target.getEyePosition();
        ClientLevel world = client.level;
        if (world != null) {
            BlockHitResult hitResultEye = world.clip(new ClipContext(playerEyePos, targetEyePos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)clientPlayer));
            return hitResultEye.getType() != HitResult.Type.BLOCK;
        }
        return false;
    }

    public static boolean isUsingElytra(LocalPlayer clientPlayer) {
        return clientPlayer.isFallFlying() || clientPlayer.getItemBySlot(EquipmentSlot.CHEST).getItem().equals(Items.ELYTRA.asItem());
    }

    public static boolean isVisibleFromPlayer(Minecraft client, LocalPlayer clientPlayer, Entity target) {
        Vec3 playerEyePos = clientPlayer.getEyePosition();
        Vec3 targetEyePos = target.getEyePosition();
        Vec3 targetCenterPos = target.position();
        ClientLevel world = client.level;
        if (world != null) {
            BlockHitResult hitResultEye = world.clip(new ClipContext(playerEyePos, targetEyePos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)clientPlayer));
            BlockHitResult hitResultCenter = world.clip(new ClipContext(playerEyePos, targetCenterPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)clientPlayer));
            return hitResultEye.getType() != HitResult.Type.BLOCK || hitResultCenter.getType() != HitResult.Type.BLOCK;
        }
        return false;
    }

    public static boolean shouldRenderCustomCrosshair() {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer clientPlayer = client.player;
        if (clientPlayer == null) {
            return false;
        }
        if (Config.CROSSHAIR_MODE == Config.CrosshairMode.Off) {
            return false;
        }
        if (Config.CROSSHAIR_MODE == Config.CrosshairMode.While_Gliding) {
            return clientPlayer.isFallFlying();
        }
        return clientPlayer.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA);
    }

    public static boolean verifyGround(LocalPlayer clientPlayer, int height) {
        return Utils.verifyGround((LivingEntity)clientPlayer, height);
    }

    public static boolean verifyGround(LivingEntity livingEntity, int height) {
        if (height < 1) {
            return true;
        }
        Level world = livingEntity.level();
        BlockPos entityPos = livingEntity.blockPosition();
        for (int i = 1; i <= height; ++i) {
            BlockPos footPosition = entityPos.offset(0, -1 * i, 0);
            if (world.getBlockState(footPosition).getBlock() instanceof AirBlock) continue;
            return false;
        }
        return true;
    }

    public static double xzDistance(LocalPlayer clientPlayer, Entity target) {
        double dx = clientPlayer.getX() - target.getX();
        double dz = clientPlayer.getZ() - target.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }

    public static double xzSquareDistance(Vec3 futurePos, Vec3 targetPos) {
        double dx = futurePos.x() - targetPos.x();
        double dz = futurePos.z() - targetPos.z();
        return dx * dx + dz * dz;
    }

    public static float lerpPitch(float start, float end, float delta) {
        return start + delta * (end - start);
    }

    public static float lerpYaw(float start, float end, float delta) {
        return start + delta * Utils.normalizeAngle(end - start);
    }

    public static float normalizeAngle(float angle) {
        if ((angle %= 360.0f) > 180.0f) {
            angle -= 360.0f;
        } else if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    public static InteractionHand getHandHoldingWindCharge(Minecraft client, LocalPlayer clientPlayer) {
        ItemStack mainHandStack = clientPlayer.getMainHandItem();
        if (mainHandStack.is(Items.WIND_CHARGE)) {
            return InteractionHand.MAIN_HAND;
        }
        if (clientPlayer.getOffhandItem().is(Items.WIND_CHARGE) && (Config.PRIORITIZE_WIND_CHARGE || ElytraBoost.isNotUsableItems(client, mainHandStack))) {
            return InteractionHand.OFF_HAND;
        }
        return null;
    }

    public static int findItemInHotbarByTags(TagKey<Item> tags) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer clientPlayer = client.player;
        if (clientPlayer == null) {
            return -1;
        }
        int currentSlot = clientPlayer.getInventory().getSelectedSlot();
        for (int i = 0; i <= 8; ++i) {
            if (i == currentSlot || !clientPlayer.getInventory().getItem(i).is(tags)) continue;
            return i;
        }
        return -1;
    }

    public static int findItemInHotbar(Class<? extends Item> targetClass, boolean isLeftStart) {
        return Utils.findItemInHotbar(targetClass, isLeftStart, null);
    }

    public static int findItemInHotbar(Class<? extends Item> targetClass, boolean isLeftStart, @Nullable ResourceKey<Enchantment> enchantment) {
        int i;
        Minecraft client = Minecraft.getInstance();
        LocalPlayer clientPlayer = client.player;
        if (clientPlayer == null) {
            return -1;
        }
        int n = i = isLeftStart ? 0 : 8;
        while (isLeftStart ? i <= 8 : i >= 0) {
            ItemStack stack = clientPlayer.getInventory().getItem(i);
            Item item = stack.getItem();
            if (targetClass.isInstance(item)) {
                if (enchantment == null) {
                    return i;
                }
                if (Utils.getEnchantLevel(clientPlayer, stack, enchantment) > 0) {
                    return i;
                }
            }
            i += isLeftStart ? 1 : -1;
        }
        return -1;
    }

    public static int findItemInInventory(Item targetItem) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer clientPlayer = client.player;
        if (clientPlayer == null) {
            return -1;
        }
        for (int i = 9; i < 36; ++i) {
            ItemStack stack = clientPlayer.getInventory().getItem(i);
            Item item = stack.getItem();
            if (item != targetItem) continue;
            return i;
        }
        return -1;
    }

    public static int getEnchantLevel(LocalPlayer clientPlayer, ItemStack itemStack, ResourceKey<Enchantment> enchantments) {
        Level world = clientPlayer.level();
        Holder.Reference enchantment = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantments);
        return itemStack.getEnchantments().getLevel((Holder)enchantment);
    }

    public static Vec3 getDragonPos(EnderDragon dragon, float delta) {
        EnderDragonPart hitPart = dragon.getSubEntities()[1];
        return hitPart.getPosition(delta).add(0.0, hitPart.getBoundingBox().getYsize() / 2.0, 0.0);
    }

    public static Vec3 getTargetPos(Entity target, boolean eyePos, float delta) {
        Vec3 vec3;
        double eyeHeight = target.getEyeHeight(target.getPose());
        if (target instanceof EnderDragon) {
            EnderDragon enderDragonEntity = (EnderDragon)target;
            vec3 = Utils.getDragonPos(enderDragonEntity, delta);
        } else {
            vec3 = eyePos ? target.getPosition(delta).add(0.0, eyeHeight, 0.0) : target.position();
        }
        return vec3;
    }

    public static InteractionHand findToSetWindCharge(LocalPlayer clientPlayer) {
        if (!Config.AUTO_WIND_CHARGE_SELECT) {
            return null;
        }
        int findWindChargeSlot = Utils.findItemInHotbar(WindChargeItem.class, true);
        if (findWindChargeSlot > -1) {
            ItemStack stack = clientPlayer.getInventory().getItem(findWindChargeSlot);
            if (clientPlayer.getCooldowns().isOnCooldown(stack)) {
                return null;
            }
            MaceAttackAssistanceClient.afterJump(clientPlayer, clientPlayer.getInventory().getSelectedSlot(), -1);
            clientPlayer.getInventory().setSelectedSlot(findWindChargeSlot);
            Minecraft mc = Minecraft.getInstance();
            if (mc.gameMode != null) {
                ((MultiPlayerGameModeInvoker)mc.gameMode).syncSelectedSlotInvoker();
            }
            return InteractionHand.MAIN_HAND;
        }
        return null;
    }

    public static boolean isSuccessFoundItItemInHotbar(Class<? extends Item> targetClass, boolean isLeftStart) {
        return Utils.findItemInHotbar(targetClass, isLeftStart) > -1;
    }

    public static boolean isHotBar(int slot) {
        return slot > -1 && slot < 9;
    }

    public static Entity getCrosshairEntity() {
        Minecraft client = Minecraft.getInstance();
        if (client.hitResult != null && client.hitResult.getType() == HitResult.Type.ENTITY) {
            return ((EntityHitResult)client.hitResult).getEntity();
        }
        return null;
    }

    public static boolean canStunSlam(Entity entity) {
        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer == null) {
            return false;
        }
        if (!(entity instanceof LivingEntity)) {
            return false;
        }
        LivingEntity target = (LivingEntity)entity;
        if (!target.isBlocking()) {
            return false;
        }
        if (!Config.STUN_SLAMMING) {
            return false;
        }
        if (clientPlayer.getDeltaMovement().y() > 0.0) {
            return false;
        }
        return !clientPlayer.isFallFlying();
    }

    public static int speedOverThreshold(LocalPlayer clientPlayer) {
        Vec3 vec = clientPlayer.getDeltaMovement();
        double length = vec.length();
        double vx = vec.x();
        double vz = vec.z();
        double v = Math.sqrt(vx * vx + vz * vz);
        if (!clientPlayer.onGround()) {
            if (length > 1.74) {
                return 2;
            }
            if (v > 0.82 && length > 1.65) {
                return 2;
            }
            if (v > 0.55) {
                return 1;
            }
        }
        return 0;
    }

    public static int getHandToSlot(LocalPlayer clientPlayer, InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND) {
            return 9;
        }
        return clientPlayer.getInventory().getSelectedSlot();
    }

    public static Vec3 simulateFuturePos(Vec3 startPos, Vec3 velocity, int ticksAhead) {
        Vec3 pos = startPos;
        Vec3 vel = velocity;
        for (int i = 0; i < ticksAhead; ++i) {
            vel = vel.add(0.0, -0.08, 0.0);
            vel = vel.multiply(0.91, 0.91, 0.91);
            pos = pos.add(vel);
        }
        return pos;
    }

    public static LivingEntity findCrosshairClosestTarget(Player player, double range, double radius) {
        Level world = player.level();
        Vec3 eyePos = player.getEyePosition(1.0f);
        Vec3 lookVec = player.getViewVector(1.0f).normalize();
        Vec3 endPos = eyePos.add(lookVec.scale(range));
        AABB box = player.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(radius);
        List<LivingEntity> candidates = world.getEntitiesOfClass(LivingEntity.class, box, e -> e != player && e.isAlive() && MaceAttackAssistanceClient.isAllowedTarget((Entity)e));
        LivingEntity bestTarget = null;
        double bestCrosshairDist = Double.MAX_VALUE;
        for (LivingEntity target : candidates) {
            BlockHitResult hit;
            Vec3 targetPos = target.getEyePosition(1.0f);
            double crosshairDist = Utils.distancePointToSegment(targetPos, eyePos, endPos);
            if (!(crosshairDist <= radius) || (hit = world.clip(new ClipContext(eyePos, targetPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)player))).getType() != HitResult.Type.MISS && hit.getLocation().distanceToSqr(eyePos) < target.distanceToSqr((Entity)player) || !(crosshairDist < bestCrosshairDist)) continue;
            bestCrosshairDist = crosshairDist;
            bestTarget = target;
        }
        return bestTarget;
    }

    private static double distancePointToSegment(Vec3 point, Vec3 segA, Vec3 segB) {
        Vec3 ab = segB.subtract(segA);
        Vec3 ap = point.subtract(segA);
        double t = ap.dot(ab) / ab.lengthSqr();
        t = Math.clamp(t, 0.0, 1.0);
        Vec3 closest = segA.add(ab.scale(t));
        return point.distanceTo(closest);
    }

    public static boolean isWearingArmor(Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity)entity;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack;
                if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR && slot.getType() != EquipmentSlot.Type.ANIMAL_ARMOR || (stack = living.getItemBySlot(slot)).isEmpty()) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean checkRightBtnAndSpear(Minecraft client, LocalPlayer clientPlayer) {
        ItemStack mainHandStack = clientPlayer.getMainHandItem();
        ItemStack offHandStack = clientPlayer.getOffhandItem();
        // Fix: Check for both right-click (use) and left-click (attack) for spear lunges
        return (mainHandStack.is(ItemTags.SPEARS) || offHandStack.is(ItemTags.SPEARS)) && (client.options.keyUse.isDown() || client.options.keyAttack.isDown());
    }

    public static boolean checkLeftClickBtnAndSpear(Minecraft client, LocalPlayer clientPlayer) {
        ItemStack mainHandStack = clientPlayer.getMainHandItem();
        return mainHandStack.is(ItemTags.SPEARS) && client.options.keyAttack.isDown();
    }

    public static boolean isSpear(LocalPlayer clientPlayer) {
        ItemStack mainHandStack = clientPlayer.getMainHandItem();
        return mainHandStack.is(ItemTags.SPEARS);
    }

    public static boolean waitingToAttack(LocalPlayer clientPlayer, float threshold) {
        return clientPlayer.getAttackStrengthScale(0.0f) >= threshold;
    }

    @Nullable
    public static LivingEntity getLivingEntityInView(LocalPlayer player, double minRange, double maxRange) {
        AABB box;
        Vec3 look;
        Vec3 end;
        Vec3 start = player.getEyePosition(1.0f);
        // Fix: Use actual maxRange, not maxRangeSqr for the raycast distance
        EntityHitResult hit = ProjectileUtil.getEntityHitResult((Entity)player, (Vec3)start, (Vec3)(end = start.add((look = player.getViewVector(1.0f)).scale(maxRange))), (AABB)(box = player.getBoundingBox().expandTowards(look.scale(maxRange)).inflate(1.0)), entity -> entity instanceof LivingEntity && entity != player && !entity.isSpectator() && entity.isAlive(), maxRange);
        if (hit == null) {
            return null;
        }
        Entity entity2 = hit.getEntity();
        double distance = player.distanceTo(entity2);
        if (distance > minRange && distance <= maxRange) {
            return (LivingEntity)entity2;
        }
        return null;
    }

    public static double rangeByWeapons(@NotNull LocalPlayer clientPlayer) {
        return Utils.isSpear(clientPlayer) ? (double)Utils.getRangeSpear() : (double)Utils.getRange();
    }

    public static Entity findNearestEnderPearl(Minecraft client, LocalPlayer clientPlayer) {
        double MAX_RANGE = 30.0;
        double closestDistance = 30.0;
        Entity nearestEntity = null;
        Vec3 playerPos = clientPlayer.position();
        AABB pBox = clientPlayer.getBoundingBox();
        AABB searchArea = pBox.inflate(30.0);
        if (client.level != null) {
            List<ThrownEnderpearl> pearls = client.level.getEntitiesOfClass(ThrownEnderpearl.class, searchArea, pearl -> pearl.getOwner() == clientPlayer);
            for (Entity entity : pearls) {
                double distance = playerPos.distanceTo(entity.position());
                if (!(distance < closestDistance)) continue;
                closestDistance = distance;
                nearestEntity = entity;
            }
        }
        return nearestEntity;
    }

    public static Entity findNearestSplashPotion(Minecraft client, LocalPlayer clientPlayer) {
        double MAX_RANGE = 30.0;
        double closestDistance = 30.0;
        Entity nearestEntity = null;
        Vec3 playerPos = clientPlayer.position();
        AABB pBox = clientPlayer.getBoundingBox();
        AABB searchArea = pBox.inflate(30.0);
        if (client.level != null) {
            List<ThrownSplashPotion> pearls = client.level.getEntitiesOfClass(ThrownSplashPotion.class, searchArea, splashPotion -> splashPotion.getOwner() == clientPlayer);
            for (Entity entity : pearls) {
                double distance = playerPos.distanceTo(entity.position());
                if (!(distance < closestDistance)) continue;
                closestDistance = distance;
                nearestEntity = entity;
            }
        }
        return nearestEntity;
    }

    public static Vec2 getLookAngles(LocalPlayer player, Vec3 targetPos) {
        Vec3 eyes = player.getEyePosition();
        double dx = targetPos.x - eyes.x;
        double dy = targetPos.y - eyes.y;
        double dz = targetPos.z - eyes.z;
        double distXZ = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        float pitch = (float)(-Math.toDegrees(Math.atan2(dy, distXZ)));
        return new Vec2(yaw, pitch);
    }

    public static boolean aimingElytra(LocalPlayer clientPlayer) {
        if (!clientPlayer.isFallFlying()) {
            return false;
        }
        Minecraft client = Minecraft.getInstance();
        return client.options.keySprint.isDown();
    }

    public static int getMyPing() {
        PlayerInfo playerInfo;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getConnection() != null && minecraft.player != null && (playerInfo = minecraft.getConnection().getPlayerInfo(minecraft.player.getUUID())) != null) {
            return playerInfo.getLatency();
        }
        return 0;
    }

    public static boolean compareHandItemsWithTags(LocalPlayer player, InteractionHand hand, TagKey<Item> itemTags) {
        return player.getItemInHand(hand).is(itemTags);
    }

    public static boolean isTriggerPressed(Minecraft client, LocalPlayer clientPlayer) {
        Window windowHandle = client.getWindow();
        int keys = Utils.isSpear(clientPlayer) ? Config.ROCKET_TRIGGER_SPEAR.getGlfwKey() : Config.ROCKET_TRIGGER.getGlfwKey();
        return keys > -1 && InputConstants.isKeyDown((Window)windowHandle, (int)keys);
    }
}
