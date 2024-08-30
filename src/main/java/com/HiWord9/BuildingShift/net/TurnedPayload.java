package com.HiWord9.BuildingShift.net;

import com.HiWord9.BuildingShift.Constants;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TurnedPayload(boolean enabled) implements CustomPayload {

    public static final PacketCodec<PacketByteBuf, TurnedPayload> CODEC = CustomPayload
            .codecOf(
                    TurnedPayload::write,
                    TurnedPayload::new
            );

    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeBoolean(enabled);
    }

    public static final CustomPayload.Id<TurnedPayload> ID = new Id<>(
            Identifier.of(Constants.MOD_ID, "turned")
    );

    public TurnedPayload(PacketByteBuf packetByteBuf) {
        this(packetByteBuf.readBoolean());
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
