package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.mixin.KeyBindingInvoker;
import com.papack.maceattackassistance.mixin.MinecraftClientInvoker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.client.util.InputUtil;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.client.network.ClientPlayerEntity;

public class SpearAttacks {
    public static boolean SPEAR_SLAM_ACTIVE = false;
    private static int spearSlamCooldown = 0;

    public static void spearAssist(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        if (ZoomState.KeyManager.keyManager()) {
            return;
        }

        if (spearSlamCooldown > 0) {
            spearSlamCooldown--;
        }

        if (Config.SPEAR_ASSIST && client.options.attackKey.isPressed()) {
            boolean isThereTarget;
            int currentSlot = clientPlayer.getInventory().getSelectedSlot();
            Entity target = client.targetedEntity;
            boolean bl = isThereTarget = Utils.getLivingEntityInView(clientPlayer, 0.0, 4.5) != null || target != null || !Config.WEAPON_SWING;
            if (ZoomState.MAAClientState.antiCheat) {
                return;
            }
            if (JobManager.jumpOption() && target instanceof PlayerEntity) {
                return;
            }
            if (client.options.attackKey.isPressed() && Utils.waitingToAttack(clientPlayer)) {
                boolean isFalling = clientPlayer.getVelocity().getY() < -0.447;
                if (Utils.isSpear(clientPlayer) && isThereTarget) {

                    if (Config.SPEAR_SLAM && isFalling
                            && !SPEAR_SLAM_ACTIVE
                            && spearSlamCooldown <= 0
                            && isValidSpearSlamTarget(target)
                            && !isInExclusionState(clientPlayer)) {
                        int maceSlot = HotSwap.getPrimaryMaceSlotId(clientPlayer);
                        if (maceSlot > -1 && maceSlot != currentSlot) {
                            SPEAR_SLAM_ACTIVE = true;
                            final int spearSlot = currentSlot;
                            final int maceSlotFinal = maceSlot;
                            final MinecraftClient clientFinal = client;
                            final ClientPlayerEntity playerFinal = clientPlayer;

                            TickScheduler.setDelayTask(1, () -> {
                                if (clientFinal.player == null) return;
                                clientFinal.player.getInventory().setSelectedSlot(maceSlotFinal);
                                ((MinecraftClientInvoker) clientFinal).doAttackInvoker();
                                if (Config.SPEAR_SLAM_PARTICLES || Config.SPEAR_SLAM_SOUND) {
                                    SpearSlamEffects.playSlamEffect(clientFinal, playerFinal, clientFinal.targetedEntity);
                                }
                                TickScheduler.setDelayTask(1, () -> {
                                    if (clientFinal.player == null) return;
                                    PrevSlotManager.setPrevSlot(StatusType.NONE, spearSlot, 0);
                                });
                            });
                            spearSlamCooldown = Config.SPEAR_SLAM_COOLDOWN;
                        }
                    } else if (isFalling && Config.SPEAR_SWAP && target != null && target.isAlive()) {
                        int weaponSlot = HotSwap.getPrimaryMaceSlotId(clientPlayer);
                        if (weaponSlot > -1) {
                            KeyBinding.onKeyPressed((InputUtil.Key)((KeyBindingInvoker)client.options.hotbarKeys[weaponSlot]).accessorBoundKey());
                            if (currentSlot != weaponSlot && Config.RETURN_TO_PREV_SLOT) {
                                PrevSlotManager.setPrevSlot(StatusType.NONE, currentSlot, 2);
                            }
                        }
                    } else if (!isFalling && !client.options.sprintKey.isPressed()) {
                        int weaponSlot = Utils.findItemInHotbarByTags((TagKey<Item>)ItemTags.SWORDS);
                        if (weaponSlot > -1) {
                            KeyBinding.onKeyPressed((InputUtil.Key)((KeyBindingInvoker)client.options.hotbarKeys[weaponSlot]).accessorBoundKey());
                            if (currentSlot != weaponSlot && Config.RETURN_TO_PREV_SLOT) {
                                PrevSlotManager.setPrevSlot(StatusType.NONE, currentSlot, 2);
                            }
                        }
                    }
                } else if (!isFalling && Utils.getLivingEntityInView(clientPlayer, 3.0, 4.5) != null) {
                    SpearAttacks.autoLungeAttack(client, clientPlayer);
                }
            }
        }
    }

    private static boolean isInExclusionState(ClientPlayerEntity player) {
        return player.isGliding()
            || player.isSwimming()
            || player.isSubmergedInWater()
            || player.hasVehicle()
            || player.isSpectator();
    }

    private static boolean isValidSpearSlamTarget(Entity target) {
        if (target == null) return false;
        if (!target.isAlive()) return false;
        if (!(target instanceof LivingEntity)) return false;
        if (Config.FRIEND_PROTECTION && FriendManager.isFriend(target.getUuid())) return false;
        return true;
    }

    public static void manualLungeAttack(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        int currentSlot = clientPlayer.getInventory().getSelectedSlot();
        int spearSlot = Utils.findItemInHotbarByTags((TagKey<Item>)ItemTags.SPEARS);
        if (spearSlot > -1) {
            KeyBinding.onKeyPressed((InputUtil.Key)((KeyBindingInvoker)client.options.attackKey).accessorBoundKey());
            KeyBinding.onKeyPressed((InputUtil.Key)((KeyBindingInvoker)client.options.hotbarKeys[spearSlot]).accessorBoundKey());
            if (currentSlot != spearSlot && Config.RETURN_TO_PREV_SLOT) {
                PrevSlotManager.setPrevSlot(StatusType.NONE, currentSlot, 2);
            }
        }
    }

    public static void autoLungeAttack(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        int currentSlot = clientPlayer.getInventory().getSelectedSlot();
        int spearSlot = Utils.findItemInHotbarByTags((TagKey<Item>)ItemTags.SPEARS);
        if (spearSlot > -1) {
            KeyBinding.onKeyPressed((InputUtil.Key)((KeyBindingInvoker)client.options.hotbarKeys[spearSlot]).accessorBoundKey());
            if (currentSlot != spearSlot && Config.RETURN_TO_PREV_SLOT) {
                PrevSlotManager.setPrevSlot(StatusType.NONE, currentSlot, 2);
            }
        }
    }
}
