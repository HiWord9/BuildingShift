package com.HiWord9.BuildingShift.networking;

import com.HiWord9.BuildingShift.Constants;
import com.HiWord9.BuildingShift.main.BuildingShift;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;

public class PacketHandler {
    public static void init() {
        PayloadTypeRegistry.playS2C().register(InstalledPayload.ID, InstalledPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(TurnedPayload.ID, TurnedPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(InstalledPayload.ID, InstalledPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(TurnedPayload.ID, TurnedPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(
                TurnedPayload.ID,
                (payload, context) -> {
                    context.server().execute(() -> {
                        receivedTurned(payload, context);
                    });
                }
        );
        ServerPlayNetworking.registerGlobalReceiver(
                InstalledPayload.ID,
                (payload, context) -> {
                    context.server().execute(() -> {
                        receivedInstalled(payload, context);
                    });
                }
        );
    }

    private static void receivedInstalled(InstalledPayload payload, ServerPlayNetworking.Context context) {
        Constants.LOGGER.info("{} Has Building Shift installed on client! Enabling special integration", context.player().getName().getString());
        BuildingShift.markAsHasMod(context.player());
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
