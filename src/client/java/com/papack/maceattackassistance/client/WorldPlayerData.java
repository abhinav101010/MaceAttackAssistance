/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.PlayerSkin
 *  net.minecraft.world.scores.PlayerTeam
 */
package com.papack.maceattackassistance.client;

import java.util.UUID;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.scores.PlayerTeam;

public record WorldPlayerData(String name, UUID uuid, PlayerSkin skin, PlayerTeam team, boolean friend) {
}
