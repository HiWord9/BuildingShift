package com.HiWord9.BuildingShift.networking;

import com.HiWord9.BuildingShift.Constants;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record InstalledPayload() implements CustomPayload {

    public static final PacketCodec<PacketByteBuf, InstalledPayload> CODEC = CustomPayload
            .codecOf(
                    (installedPayload, packetByteBuf) -> {},
                    InstalledPayload::new
            );

    public static final CustomPayload.Id<InstalledPayload> ID = new Id<>(
            Identifier.of(Constants.MOD_ID, "installed")
    );

    public InstalledPayload(PacketByteBuf packetByteBuf) {
        this();
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
