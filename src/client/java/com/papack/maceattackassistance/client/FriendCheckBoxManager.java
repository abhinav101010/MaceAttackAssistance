/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.FriendCheckBoxData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;

public class FriendCheckBoxManager {
    private static final List<FriendCheckBoxData> checkBoxDataList = new ArrayList<FriendCheckBoxData>();

    public static void clear() {
        checkBoxDataList.clear();
    }

    public static void addCheckBoxData(String realName, Component displayName) {
        checkBoxDataList.add(new FriendCheckBoxData(realName, displayName));
    }

    public static String getRealName(Component displayName) {
        for (FriendCheckBoxData checkBoxData : checkBoxDataList) {
            if (!checkBoxData.displayName().equals((Object)displayName)) continue;
            return checkBoxData.realName();
        }
        return null;
    }
}
