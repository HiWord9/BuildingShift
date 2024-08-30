package com.HiWord9.BuildingShift.server;

import com.HiWord9.BuildingShift.Constants;
import com.HiWord9.BuildingShift.net.InstalledPayload;
import com.HiWord9.BuildingShift.net.PacketHandler;
import com.HiWord9.BuildingShift.net.TurnedPayload;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.S2CPlayChannelEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashSet;

public class BuildingShift implements ModInitializer {
    private static final HashSet<PlayerEntity> enabledPlayers = new HashSet<>();

    // can't translate on server
    public static final Text MESSAGE_ON = Text.of("Building Shift Turned On!").copy().formatted(Formatting.GOLD);
    public static final Text MESSAGE_OFF = Text.of("Building Shift Turned Off!").copy().formatted(Formatting.GRAY);

    @Override
    public void onInitialize() {
        Constants.LOGGER.info("Building Shift server-side initialization");

        CommandRegistrationCallback.EVENT.register(BuildingShift::registerCommand);
        S2CPlayChannelEvents.REGISTER.register((handler, sender, server, channels) -> {
            ServerPlayNetworking.send(handler.player, new InstalledPayload());
        });
        S2CPlayChannelEvents.UNREGISTER.register((handler, sender, server, channels) -> {
            disableFor(handler.player);
        });

        PacketHandler.init();
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                       CommandRegistryAccess commandRegistryAccess,
                                       CommandManager.RegistrationEnvironment registrationEnvironment) {
        BuildingShiftCommand.register(serverCommandSourceCommandDispatcher);
    }

    public static void enableFor(PlayerEntity player) {
        enabledPlayers.add(player);
        ServerPlayNetworking.send((ServerPlayerEntity) player, new TurnedPayload(true));
    }

    public static void disableFor(PlayerEntity player) {
        enabledPlayers.remove(player);
        ServerPlayNetworking.send((ServerPlayerEntity) player, new TurnedPayload(false));
    }

    public static boolean isEnabledFor(PlayerEntity player) {
        return enabledPlayers.contains(player);
    }

    /**
     * Enables BuildingShift for given player if disabled, or disables if enabled.
     *
     * @return true if enabled, false if disabled
     */
    public static boolean toggleFor(PlayerEntity player) {
        if (isEnabledFor(player)) {
            disableFor(player);
            return false;
        }
        enableFor(player);
        return true;
    }

    public static void overlayStatus(PlayerEntity player) {
        overlayStatus(player, isEnabledFor(player));
    }

    public static void overlayStatus(PlayerEntity player, boolean enabled) {
        player.sendMessage(enabled ? MESSAGE_ON : MESSAGE_OFF, true);
    }
}
