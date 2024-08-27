package com.HiWord9.BuildingShift.server;

import com.HiWord9.BuildingShift.Constants;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.HashSet;

public class BuildingShift implements ModInitializer {
    private static final HashSet<PlayerEntity> enabledPlayers = new HashSet<>();

    @Override
    public void onInitialize() {
        Constants.LOGGER.info("Building Shift server-side initializing");
        CommandRegistrationCallback.EVENT.register(BuildingShift::registerCommand);
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                       CommandRegistryAccess commandRegistryAccess,
                                       CommandManager.RegistrationEnvironment registrationEnvironment) {
        BuildingShiftCommand.register(serverCommandSourceCommandDispatcher);
    }

    public static void enableFor(PlayerEntity player) {
        enabledPlayers.add(player);
    }

    public static void disableFor(PlayerEntity player) {
        enabledPlayers.remove(player);
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
        player.sendMessage(enabled ? Constants.MESSAGE_ON : Constants.MESSAGE_OFF, true);
    }
}
