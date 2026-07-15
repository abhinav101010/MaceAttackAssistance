/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.Uuids
 *  net.minecraft.CustomPayload
 *  net.minecraft.CustomPayload$Id
 *  net.minecraft.RegistryByteBuf
 *  net.minecraft.PacketCodec
 */
package com.papack.maceattackassistance.network;

import com.papack.maceattackassistance.network.MaaNetWorkConstants;
import java.util.UUID;
import net.minecraft.util.Uuids;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record MaaPayload(UUID uuid) implements CustomPayload
{
    public static final CustomPayload.Id<MaaPayload> ID = new CustomPayload.Id(MaaNetWorkConstants.MAA_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, MaaPayload> CODEC = PacketCodec.tuple((PacketCodec)Uuids.PACKET_CODEC, MaaPayload::uuid, MaaPayload::new);

    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
