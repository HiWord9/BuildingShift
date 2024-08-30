package com.HiWord9.BuildingShift.net;

import com.HiWord9.BuildingShift.Constants;
import com.HiWord9.BuildingShift.client.BuildingShiftClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientPacketHandler {

    @Environment(EnvType.CLIENT)
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(InstalledPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                receivedInstalled(payload, context);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(TurnedPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                receivedTurned(payload, context);
            });
        });
    }

    private static void receivedTurned(TurnedPayload payload, ClientPlayNetworking.Context context) {
        boolean enabled = payload.isEnabled();
        BuildingShiftClient.turnInternal(enabled);
    }

    private static void receivedInstalled(InstalledPayload payload, ClientPlayNetworking.Context context) {
        Constants.LOGGER.info("Building Shift is installed on current server! Enabling special integration");
        BuildingShiftClient.installedOnServer = true;
    }
}
