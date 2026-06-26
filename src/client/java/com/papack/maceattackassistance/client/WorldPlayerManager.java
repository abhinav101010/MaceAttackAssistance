/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientPacketListener
 *  net.minecraft.client.multiplayer.PlayerInfo
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.player.PlayerSkin
 *  net.minecraft.world.scores.PlayerTeam
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.FriendManager;
import com.papack.maceattackassistance.client.WorldPlayerData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.scores.PlayerTeam;

public class WorldPlayerManager {
    private static final List<WorldPlayerData> worldPlayers = new ArrayList<WorldPlayerData>();

    public static void clear() {
        worldPlayers.clear();
    }

    public static void setWorldPlayers(Minecraft client) {
        ClientPacketListener networkHandler = client.getConnection();
        if (networkHandler != null) {
            Collection<PlayerInfo> entryCollection = networkHandler.getListedOnlinePlayers();
            for (PlayerInfo entry : entryCollection) {
                if (entry == null) continue;
                UUID uuid = entry.getProfile().id();
                WorldPlayerManager.addPlayer(entry.getProfile().name(), uuid, entry.getSkin(), entry.getTeam(), FriendManager.isFriend(uuid));
            }
        }
    }

    public static void addPlayer(String name, UUID uuid, PlayerSkin skin, PlayerTeam team, boolean friend) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && !player.getUUID().equals(uuid) && !WorldPlayerManager.isContainsUuid(uuid)) {
            worldPlayers.add(new WorldPlayerData(name, uuid, skin, team, friend));
        }
    }

    public static boolean isContainsName(String name) {
        return worldPlayers.stream().anyMatch(f -> f.name().equals(name));
    }

    public static boolean isContainsUuid(UUID uuid) {
        return worldPlayers.stream().anyMatch(f -> f.uuid().equals(uuid));
    }

    public static UUID getUUID(String name) {
        for (WorldPlayerData playerData : worldPlayers) {
            if (!playerData.name().equals(name)) continue;
            return playerData.uuid();
        }
        return null;
    }

    public static WorldPlayerData getPlayerByName(String name) {
        for (WorldPlayerData playerData : worldPlayers) {
            if (!playerData.name().equals(name)) continue;
            return playerData;
        }
        return null;
    }

    public static boolean removePlayerByName(String name) {
        return worldPlayers.removeIf(p -> p.name().equals(name));
    }

    public static boolean removePlayerByUuid(UUID uuid) {
        return worldPlayers.removeIf(p -> p.uuid().equals(uuid));
    }

    public static List<WorldPlayerData> getWorldPlayers() {
        return worldPlayers;
    }

    public static PlayerTeam getTeam(UUID uuid) {
        for (WorldPlayerData playerData : worldPlayers) {
            if (!playerData.uuid().equals(uuid)) continue;
            return playerData.team();
        }
        return null;
    }

    public static List<WorldPlayerData> getSortedList() {
        ArrayList<WorldPlayerData> friendList = new ArrayList<WorldPlayerData>();
        ArrayList<WorldPlayerData> notFriendList = new ArrayList<WorldPlayerData>();
        for (WorldPlayerData data : worldPlayers) {
            if (data.friend()) {
                friendList.add(data);
                continue;
            }
            notFriendList.add(data);
        }
        WorldPlayerManager.clear();
        worldPlayers.addAll(friendList);
        worldPlayers.addAll(notFriendList);
        return worldPlayers;
    }
}
