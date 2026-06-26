/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.UUIDUtil
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 */
package com.papack.maceattackassistance.network;

import com.papack.maceattackassistance.network.MaaNetWorkConstants;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record MaaPayload(UUID uuid) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<MaaPayload> ID = new CustomPacketPayload.Type(MaaNetWorkConstants.MAA_PACKET_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, MaaPayload> CODEC = StreamCodec.composite((StreamCodec)UUIDUtil.STREAM_CODEC, MaaPayload::uuid, MaaPayload::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
