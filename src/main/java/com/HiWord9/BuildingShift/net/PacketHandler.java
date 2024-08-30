package com.HiWord9.BuildingShift.net;

import com.HiWord9.BuildingShift.server.BuildingShift;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;

public class PacketHandler {
    public static void init() {
        PayloadTypeRegistry.playS2C().register(InstalledPayload.ID, InstalledPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(TurnedPayload.ID, TurnedPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(TurnedPayload.ID, TurnedPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(
                TurnedPayload.ID,
                (payload, context) -> {
                    context.server().execute(() -> {
                        receivedTurned(payload, context);
                    });
                }
        );
    }

    private static void receivedTurned(TurnedPayload payload, ServerPlayNetworking.Context context) {
        boolean enabled = payload.isEnabled();
        PlayerEntity player = context.player();
        if (enabled) {
            BuildingShift.enableFor(player);
        } else {
            BuildingShift.disableFor(player);
        }
    }
}
