package com.HiWord9.BuildingShift.server;

import com.HiWord9.BuildingShift.Constants;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class BuildingShift implements ModInitializer {
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
}
