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

    public static void toggleFor(PlayerEntity player) {
        if (isEnabledFor(player)) {
            disableFor(player);
        } else {
            enableFor(player);
        }
    }
}
