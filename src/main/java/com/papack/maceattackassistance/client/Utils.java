/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.Hand
 *  net.minecraft.Entity
 *  net.minecraft.EquipmentSlot
 *  net.minecraft.EquipmentSlot$Type
 *  net.minecraft.LivingEntity
 *  net.minecraft.EnderDragonPart
 *  net.minecraft.EnderDragonEntity
 *  net.minecraft.PlayerEntity
 *  net.minecraft.ProjectileUtil
 *  net.minecraft.EnderPearlEntity
 *  net.minecraft.Item
 *  net.minecraft.ItemStack
 *  net.minecraft.Items
 *  net.minecraft.Enchantment
 *  net.minecraft.World
 *  net.minecraft.AirBlock
 *  net.minecraft.Blocks
 *  net.minecraft.BlockPos
 *  net.minecraft.Box
 *  net.minecraft.HitResult$Type
 *  net.minecraft.Vec2f
 *  net.minecraft.Vec3d
 *  net.minecraft.BlockState
 *  net.minecraft.MinecraftClient
 *  net.minecraft.ItemTags
 *  net.minecraft.RaycastContext
 *  net.minecraft.RaycastContext$FluidHandling
 *  net.minecraft.RaycastContext$ShapeType
 *  net.minecraft.BlockHitResult
 *  net.minecraft.EntityHitResult
 *  net.minecraft.RegistryKey
 *  net.minecraft.ClientWorld
 *  net.minecraft.TagKey
 *  net.minecraft.RegistryEntry
 *  net.minecraft.RegistryEntry$Reference
 *  net.minecraft.ClientPlayerEntity
 *  net.minecraft.RegistryKeys
 *  net.minecraft.WindChargeItem
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.ElytraBoost;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.config.Config;
import java.util.List;
import java.util.Objects;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.world.World;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.registry.RegistryKey;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.item.WindChargeItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Utils {
    public static boolean canDoubleTap() {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        if (clientPlayer == null) {
            return false;
        }
        if (!Config.DOUBLE_TAP) {
            return false;
        }
        return clientPlayer.isGliding();
    }

    public static boolean checkPlayerUUID(Entity entity) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayer = client.player;
        boolean chkUuid = false;
        if (clientPlayer != null) {
            chkUuid = entity.getUuid().equals(clientPlayer.getUuid());
        }
        return chkUuid;
    }

    public static boolean isInAttackableRange(ClientPlayerEntity clientPlayer, Entity targetEntity) {
        if (clientPlayer != null && targetEntity != null) {
            double dz;
            double dx = clientPlayer.getX() - targetEntity.getX();
            return Math.sqrt(dx * dx + (dz = clientPlayer.getZ() - targetEntity.getZ()) * dz) <= 3.0;
        }
        return false;
    }

    public static boolean isNotUsingElytra(ClientPlayerEntity clientPlayer) {
        return !clientPlayer.isGliding() || !clientPlayer.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA);
    }

    public static boolean isOnSlimeBlock(ClientPlayerEntity clientPlayer) {
        BlockPos pos = BlockPos.ofFloored((double)clientPlayer.getX(), (double)(clientPlayer.getY() - 1.0), (double)clientPlayer.getZ());
        BlockState state = clientPlayer.getEntityWorld().getBlockState(pos);
        return state.isOf(Blocks.SLIME_BLOCK);
    }

    public static boolean isSimpleVisibleFromPlayer(MinecraftClient client, ClientPlayerEntity clientPlayer, Entity target) {
        Vec3d playerEyePos = clientPlayer.getEyePos();
        Vec3d targetEyePos = target.getEyePos();
        ClientWorld world = client.world;
        if (world != null) {
            BlockHitResult hitResultEye = world.raycast(new RaycastContext(playerEyePos, targetEyePos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)clientPlayer));
            return hitResultEye.getType() != HitResult.Type.BLOCK;
        }
        return false;
    }

    public static boolean isUsingElytra(ClientPlayerEntity clientPlayer) {
        return clientPlayer.isGliding() || clientPlayer.getEquippedStack(EquipmentSlot.CHEST).getItem().equals(Items.ELYTRA.asItem());
    }

    public static boolean isVisibleFromPlayer(MinecraftClient client, ClientPlayerEntity clientPlayer, Entity target) {
        Vec3d playerEyePos = clientPlayer.getEyePos();
        Vec3d targetEyePos = target.getEyePos();
        Vec3d targetCenterPos = target.getEntityPos();
        ClientWorld world = client.world;
        if (world != null) {
            BlockHitResult hitResultEye = world.raycast(new RaycastContext(playerEyePos, targetEyePos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)clientPlayer));
            BlockHitResult hitResultCenter = world.raycast(new RaycastContext(playerEyePos, targetCenterPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)clientPlayer));
            return hitResultEye.getType() != HitResult.Type.BLOCK || hitResultCenter.getType() != HitResult.Type.BLOCK;
        }
        return false;
    }

    public static boolean shouldRenderCustomCrosshair() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayer = client.player;
        if (clientPlayer == null) {
            return false;
        }
        if (Config.CROSSHAIR_MODE == Config.CrosshairMode.Off) {
            return false;
        }
        if (Config.CROSSHAIR_MODE == Config.CrosshairMode.While_Gliding) {
            return clientPlayer.isGliding();
        }
        return clientPlayer.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA);
    }

    public static boolean verifyGround(ClientPlayerEntity clientPlayer, int height) {
        return Utils.verifyGround((LivingEntity)clientPlayer, height);
    }

    public static boolean verifyGround(LivingEntity livingEntity, int height) {
        if (height < 1) {
            return true;
        }
        World world = livingEntity.getEntityWorld();
        BlockPos entityPos = livingEntity.getBlockPos();
        for (int i = 1; i <= height; ++i) {
            BlockPos footPosition = entityPos.add(0, -1 * i, 0);
            if (world.getBlockState(footPosition).getBlock() instanceof AirBlock) continue;
            return false;
        }
        return true;
    }

    public static double xzDistance(ClientPlayerEntity clientPlayer, Entity target) {
        double dx = clientPlayer.getX() - target.getX();
        double dz = clientPlayer.getZ() - target.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }

    public static double xzSquareDistance(Vec3d futurePos, Vec3d targetPos) {
        double dx = futurePos.getX() - targetPos.getX();
        double dz = futurePos.getZ() - targetPos.getZ();
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

    public static Hand getHandHoldingWindCharge(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        ItemStack mainHandStack = clientPlayer.getMainHandStack();
        if (mainHandStack.isOf(Items.WIND_CHARGE)) {
            return Hand.MAIN_HAND;
        }
        if (clientPlayer.getOffHandStack().isOf(Items.WIND_CHARGE) && (Config.PRIORITIZE_WIND_CHARGE || ElytraBoost.isNotUsableItems(client, mainHandStack))) {
            return Hand.OFF_HAND;
        }
        return null;
    }

    public static int findItemInHotbarByTags(TagKey<Item> tags) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayer = client.player;
        if (clientPlayer == null) {
            return -1;
        }
        int currentSlot = clientPlayer.getInventory().getSelectedSlot();
        for (int i = 0; i <= 8; ++i) {
            if (i == currentSlot || !clientPlayer.getInventory().getStack(i).isIn(tags)) continue;
            return i;
        }
        return -1;
    }

    public static int findItemInHotbar(Class<? extends Item> targetClass, boolean isLeftStart) {
        return Utils.findItemInHotbar(targetClass, isLeftStart, null);
    }

    public static int findItemInHotbar(Class<? extends Item> targetClass, boolean isLeftStart, @Nullable RegistryKey<Enchantment> enchantment) {
        int i;
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayer = client.player;
        if (clientPlayer == null) {
            return -1;
        }
        int n = i = isLeftStart ? 0 : 8;
        while (isLeftStart ? i <= 8 : i >= 0) {
            ItemStack stack = clientPlayer.getInventory().getStack(i);
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
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayer = client.player;
        if (clientPlayer == null) {
            return -1;
        }
        for (int i = 9; i < 36; ++i) {
            ItemStack stack = clientPlayer.getInventory().getStack(i);
            Item item = stack.getItem();
            if (item != targetItem) continue;
            return i;
        }
        return -1;
    }

    public static int getEnchantLevel(ClientPlayerEntity clientPlayer, ItemStack itemStack, RegistryKey<Enchantment> enchantments) {
        World world = clientPlayer.getEntityWorld();
        if (world == null) {
            return -1;
        }
        RegistryEntry.Reference enchantment = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(enchantments);
        return itemStack.getEnchantments().getLevel((RegistryEntry)enchantment);
    }

    public static Vec3d getDragonPos(EnderDragonEntity dragon, float delta) {
        EnderDragonPart hitPart = dragon.getBodyParts()[1];
        Entity obj = (Entity)Objects.requireNonNullElse(hitPart, dragon);
        return obj.getLerpedPos(delta).add(0.0, obj.getBoundingBox().getLengthY() / 2.0, 0.0);
    }

    public static Vec3d getTargetPos(Entity target, boolean eyePos, float delta) {
        Vec3d VanillaChestLootTableGenerator;
        double eyeHeight = target.getEyeHeight(target.getPose());
        if (target instanceof EnderDragonEntity) {
            EnderDragonEntity enderDragonEntity = (EnderDragonEntity)target;
            VanillaChestLootTableGenerator = Utils.getDragonPos(enderDragonEntity, delta);
        } else {
            VanillaChestLootTableGenerator = eyePos ? target.getLerpedPos(delta).add(0.0, eyeHeight, 0.0) : target.getEntityPos();
        }
        return VanillaChestLootTableGenerator;
    }

    public static Hand findToSetWindCharge(ClientPlayerEntity clientPlayer) {
        if (!Config.AUTO_WIND_CHARGE_SELECT) {
            return null;
        }
        int findWindChargeSlot = Utils.findItemInHotbar(WindChargeItem.class, true);
        if (findWindChargeSlot > -1) {
            ItemStack stack = clientPlayer.getInventory().getStack(findWindChargeSlot);
            if (clientPlayer.getItemCooldownManager().isCoolingDown(stack)) {
                return null;
            }
            MaceAttackAssistanceClient.afterJump(clientPlayer, clientPlayer.getInventory().getSelectedSlot(), -1);
            clientPlayer.getInventory().setSelectedSlot(findWindChargeSlot);
            return Hand.MAIN_HAND;
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
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            return ((EntityHitResult)client.crosshairTarget).getEntity();
        }
        return null;
    }

    public static boolean canStunSlam(Entity entity) {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
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
        if (clientPlayer.getVelocity().getY() > 0.0) {
            return false;
        }
        return !clientPlayer.isGliding();
    }

    public static int speedOverThreshold(ClientPlayerEntity clientPlayer) {
        Vec3d vec = clientPlayer.getVelocity();
        double length = vec.length();
        double vx = vec.getX();
        double vz = vec.getZ();
        double v = Math.sqrt(vx * vx + vz * vz);
        if (!clientPlayer.isOnGround()) {
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

    public static int getHandToSlot(ClientPlayerEntity clientPlayer, Hand hand) {
        if (hand == Hand.OFF_HAND) {
            return 9;
        }
        return clientPlayer.getInventory().getSelectedSlot();
    }

    public static Vec3d simulateFuturePos(Vec3d startPos, Vec3d velocity, int ticksAhead) {
        Vec3d pos = startPos;
        Vec3d vel = velocity;
        for (int i = 0; i < ticksAhead; ++i) {
            vel = vel.add(0.0, -0.08, 0.0);
            vel = vel.multiply(0.91, 0.91, 0.91);
            pos = pos.add(vel);
        }
        return pos;
    }

    public static LivingEntity findCrosshairClosestTarget(PlayerEntity player, double range, double radius) {
        World world = player.getEntityWorld();
        Vec3d eyePos = player.getCameraPosVec(1.0f);
        Vec3d lookVec = player.getRotationVec(1.0f).normalize();
        Vec3d endPos = eyePos.add(lookVec.multiply(range));
        Box box = player.getBoundingBox().stretch(lookVec.multiply(range)).expand(radius);
        List<LivingEntity> candidates = world.getEntitiesByClass(LivingEntity.class, box, e -> e != player && e.isAlive() && MaceAttackAssistanceClient.isAllowedTarget((Entity)e));
        LivingEntity bestTarget = null;
        double bestCrosshairDist = Double.MAX_VALUE;
        for (LivingEntity target : candidates) {
            BlockHitResult hit;
            Vec3d targetPos = target.getCameraPosVec(1.0f);
            double crosshairDist = Utils.distancePointToSegment(targetPos, eyePos, endPos);
            if (!(crosshairDist <= radius) || (hit = world.raycast(new RaycastContext(eyePos, targetPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)player))).getType() != HitResult.Type.MISS && hit.getPos().squaredDistanceTo(eyePos) < target.squaredDistanceTo((Entity)player) || !(crosshairDist < bestCrosshairDist)) continue;
            bestCrosshairDist = crosshairDist;
            bestTarget = target;
        }
        return bestTarget;
    }

    private static double distancePointToSegment(Vec3d point, Vec3d segA, Vec3d segB) {
        Vec3d ab = segB.subtract(segA);
        Vec3d ap = point.subtract(segA);
        double t = ap.dotProduct(ab) / ab.lengthSquared();
        t = Math.max(0.0, Math.min(1.0, t));
        Vec3d closest = segA.add(ab.multiply(t));
        return point.distanceTo(closest);
    }

    public static boolean isWearingArmor(Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity)entity;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack;
                if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR && slot.getType() != EquipmentSlot.Type.ANIMAL_ARMOR || (stack = living.getEquippedStack(slot)).isEmpty()) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean checkRightBtnAndSpear(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        ItemStack mainHandStack = clientPlayer.getMainHandStack();
        ItemStack offHandStack = clientPlayer.getOffHandStack();
        return (mainHandStack.isIn(ItemTags.SPEARS) || offHandStack.isIn(ItemTags.SPEARS)) && client.options.useKey.isPressed();
    }

    public static boolean checkLeftClickBtnAndSpear(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        ItemStack mainHandStack = clientPlayer.getMainHandStack();
        return mainHandStack.isIn(ItemTags.SPEARS) && client.options.attackKey.isPressed();
    }

    public static boolean isSpear(ClientPlayerEntity clientPlayer) {
        ItemStack mainHandStack = clientPlayer.getMainHandStack();
        return mainHandStack.isIn(ItemTags.SPEARS);
    }

    public static boolean isSwordOrAxe(ClientPlayerEntity clientPlayer) {
        ItemStack mainHandStack = clientPlayer.getMainHandStack();
        return mainHandStack.isIn(ItemTags.SWORDS) || mainHandStack.isIn(ItemTags.AXES);
    }

    public static boolean waitingToAttack(ClientPlayerEntity clientPlayer) {
        return clientPlayer.getAttackCooldownProgress(0.0f) >= 0.3f;
    }

    public static boolean canAttack(MinecraftClient client) {
        return client.targetedEntity != null;
    }

    @Nullable
    public static LivingEntity getLivingEntityInView(ClientPlayerEntity player, double minRange, double maxRange) {
        Box box;
        Vec3d look;
        Vec3d end;
        double maxRangeSqr = maxRange * maxRange;
        Vec3d start = player.getCameraPosVec(1.0f);
        EntityHitResult hit = ProjectileUtil.raycast((Entity)player, (Vec3d)start, (Vec3d)(end = start.add((look = player.getRotationVec(1.0f)).multiply(maxRange))), (Box)(box = player.getBoundingBox().stretch(look.multiply(maxRange)).expand(1.0)), entity -> entity instanceof LivingEntity && entity != player && !entity.isSpectator() && entity.isAlive(), (double)maxRangeSqr);
        if (hit == null) {
            return null;
        }
        Entity entity2 = hit.getEntity();
        double getDistance = player.distanceTo(entity2);
        if (getDistance > minRange && getDistance <= maxRange) {
            return (LivingEntity)entity2;
        }
        return null;
    }

    public static float rangeByWeapons(@NotNull ClientPlayerEntity clientPlayer) {
        return Utils.isSpear(clientPlayer) ? 4.5f : 3.0f;
    }

    public static Entity findNearestEnderPearl(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        double MAX_RANGE = 10.0;
        double closestDistance = 10.0;
        Entity nearestMob = null;
        Vec3d playerPos = clientPlayer.getEntityPos();
        Box pBox = clientPlayer.getBoundingBox();
        Box searchArea = pBox.expand(10.0);
        if (client.world != null) {
            List<Entity> nearbyEntities = client.world.getOtherEntities((Entity)clientPlayer, searchArea, entity -> {
                if (!(entity instanceof EnderPearlEntity)) return false;
                EnderPearlEntity enderPearl = (EnderPearlEntity)entity;
                if (!clientPlayer.getUuid().equals(Objects.requireNonNull(enderPearl.getOwner()).getUuid())) return false;
                return true;
            });
            for (Entity entity2 : nearbyEntities) {
                double getDistance = playerPos.distanceTo(entity2.getEntityPos());
                if (!(getDistance < closestDistance)) continue;
                closestDistance = getDistance;
                nearestMob = entity2;
            }
        }
        return nearestMob;
    }

    public static Vec2f getLookAngles(ClientPlayerEntity player, Vec3d targetPos) {
        Vec3d eyes = player.getEyePos();
        double dx = targetPos.x - eyes.x;
        double dy = targetPos.y - eyes.y;
        double dz = targetPos.z - eyes.z;
        double distXZ = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        float pitch = (float)(-Math.toDegrees(Math.atan2(dy, distXZ)));
        return new Vec2f(yaw, pitch);
    }
}
