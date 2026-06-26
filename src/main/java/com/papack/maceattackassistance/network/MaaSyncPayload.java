/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.UUIDUtil
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  org.jspecify.annotations.NonNull
 */
package com.papack.maceattackassistance.network;

import com.papack.maceattackassistance.network.MaaNetWorkConstants;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

public record MaaSyncPayload(int level, UUID uuid) implements CustomPacketPayload
{
    public static final // Could not load outer class - annotation placement on inner may be incorrect
    CustomPacketPayload.Type<@NonNull MaaSyncPayload> TYPE = new CustomPacketPayload.Type(MaaNetWorkConstants.MAA_SYNC_PACKET_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, MaaSyncPayload> CODEC = StreamCodec.composite((StreamCodec)ByteBufCodecs.INT, MaaSyncPayload::level, (StreamCodec)UUIDUtil.STREAM_CODEC, MaaSyncPayload::uuid, MaaSyncPayload::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
