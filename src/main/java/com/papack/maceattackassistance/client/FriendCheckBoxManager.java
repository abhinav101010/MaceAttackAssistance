/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.Text
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.FriendCheckBoxData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.text.Text;

public class FriendCheckBoxManager {
    private static final List<FriendCheckBoxData> checkBoxDataList = new ArrayList<FriendCheckBoxData>();

    public static void clear() {
        checkBoxDataList.clear();
    }

    public static void addCheckBoxData(String realName, Text displayName) {
        checkBoxDataList.add(new FriendCheckBoxData(realName, displayName));
    }

    public static String getRealName(Text displayName) {
        for (FriendCheckBoxData checkBoxData : checkBoxDataList) {
            if (!checkBoxData.displayName().equals((Object)displayName)) continue;
            return checkBoxData.realName();
        }
        return null;
    }
}
