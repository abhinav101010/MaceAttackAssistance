package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

@Environment(value=EnvType.CLIENT)
public class FriendManager {
    private static final Set<String> friends = new HashSet<>();

    public static boolean addFriend(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }
        String normalized = normalize(username);
        if (friends.add(normalized)) {
            clearCurrentTargetIfFriend(normalized);
            return true;
        }
        return false;
    }

    public static boolean removeFriend(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }
        return friends.remove(normalize(username));
    }

    public static boolean isFriend(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }
        return friends.contains(normalize(username));
    }

    public static boolean isFriend(Entity entity) {
        if (entity == null || !(entity instanceof PlayerEntity)) {
            return false;
        }
        return isFriend(entity.getName().getString());
    }

    public static List<String> getFriends() {
        return new ArrayList<>(friends);
    }

    public static void setFriends(List<String> list) {
        friends.clear();
        if (list != null) {
            for (String name : list) {
                if (name != null && !name.isBlank()) {
                    friends.add(normalize(name));
                }
            }
        }
        clearCurrentTargetIfAnyFriend();
    }

    public static void clearFriends() {
        friends.clear();
        clearCurrentTargetIfAnyFriend();
    }

    public static boolean isValidCombatTarget(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (!entity.isAlive()) {
            return false;
        }
        if (!entity.isLiving()) {
            return false;
        }
        if (entity instanceof PlayerEntity && isFriend(entity)) {
            return false;
        }
        return true;
    }

    private static String normalize(String username) {
        return username.trim().toLowerCase(Locale.ROOT);
    }

    private static void clearCurrentTargetIfFriend(String normalized) {
        MaceAttackAssistanceClient.clearTargetIfFriend(normalized);
    }

    private static void clearCurrentTargetIfAnyFriend() {
        MaceAttackAssistanceClient.clearTargetIfAnyFriend();
    }
}
