/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.Team
 *  net.minecraft.SkinTextures
 */
package com.papack.maceattackassistance.client;

import java.util.UUID;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.player.SkinTextures;

public record WorldPlayerData(String name, UUID uuid, SkinTextures skin, Team team, boolean friend) {
}
