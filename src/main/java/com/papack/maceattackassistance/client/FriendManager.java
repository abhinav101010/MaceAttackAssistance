/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.reflect.TypeToken
 */
package com.papack.maceattackassistance.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.papack.maceattackassistance.client.FriendData;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.config.Config;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class FriendManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type LIST_TYPE = new TypeToken<List<FriendData>>(){}.getType();
    private static final List<FriendData> friends = new ArrayList<FriendData>();
    private static Path filePath;

    public static void init() {
        Path configDir = Config.MAA_CONF_DIR;
        filePath = configDir.resolve("friends.json");
        try {
            if (!Files.exists(configDir, new LinkOption[0])) {
                Files.createDirectories(configDir, new FileAttribute[0]);
            }
            if (!Files.exists(filePath, new LinkOption[0])) {
                FriendManager.save();
            }
            FriendManager.load();
        }
        catch (IOException e) {
            MaceAttackAssistanceClient.LOGGER.error("Failed to initialize FriendManager", (Throwable)e);
        }
    }

    public static void load() {
        try (InputStreamReader reader = new InputStreamReader((InputStream)new FileInputStream(filePath.toFile()), StandardCharsets.UTF_8);){
            List loaded = (List)GSON.fromJson((Reader)reader, LIST_TYPE);
            friends.clear();
            if (loaded != null) {
                friends.addAll(loaded);
            }
        }
        catch (IOException e) {
            MaceAttackAssistanceClient.LOGGER.error("Failed to load friends.json", (Throwable)e);
        }
    }

    public static void save() {
        try (OutputStreamWriter writer = new OutputStreamWriter((OutputStream)new FileOutputStream(filePath.toFile()), StandardCharsets.UTF_8);){
            GSON.toJson(friends, LIST_TYPE, (Appendable)writer);
        }
        catch (IOException e) {
            MaceAttackAssistanceClient.LOGGER.error("Failed to save friends.json", (Throwable)e);
        }
    }

    public static void addFriend(String name, UUID uuid) {
        if (!FriendManager.isFriend(uuid)) {
            friends.add(new FriendData(name, uuid));
            FriendManager.save();
        }
    }

    public static void removeFriend(UUID uuid) {
        friends.removeIf(f -> f.uuid().equals(uuid));
        FriendManager.save();
    }

    public static boolean isFriend(UUID uuid) {
        return friends.stream().anyMatch(f -> f.uuid().equals(uuid));
    }

    public static UUID getUUID(String name) {
        for (FriendData friendData : friends) {
            if (!friendData.name().equals(name)) continue;
            return friendData.uuid();
        }
        return null;
    }

    public static Collection<? extends String> getFriendNameList() {
        return friends.stream().map(FriendData::name).toList();
    }
}
