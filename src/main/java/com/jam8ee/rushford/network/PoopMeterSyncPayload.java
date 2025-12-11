package com.jam8ee.rushford.network;

import com.jam8ee.rushford.Rushford;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PoopMeterSyncPayload(int poopLevel) implements CustomPayload {

    public static final CustomPayload.Id<PoopMeterSyncPayload> ID =
            new CustomPayload.Id<>(Identifier.of(Rushford.MOD_ID, "poop_meter_sync"));

    public static final PacketCodec<RegistryByteBuf, PoopMeterSyncPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.INTEGER, PoopMeterSyncPayload::poopLevel,
                    PoopMeterSyncPayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
