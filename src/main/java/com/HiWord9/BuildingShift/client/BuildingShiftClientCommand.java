package com.HiWord9.BuildingShift.client;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class BuildingShiftClientCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("buildingshift")
                .requires(source -> !BuildingShiftClient.installedOnServer)
                .then(literal("toggle")
                        .executes(context -> onToggle()))
                .then(literal("on")
                        .executes(context -> onOn()))
                .then(literal("off")
                        .executes(context -> onOff())));
    }

    public static int onToggle() {
        BuildingShiftClient.toggle();
        return Command.SINGLE_SUCCESS;
    }

    public static int onOn() {
        BuildingShiftClient.turn(true);
        return Command.SINGLE_SUCCESS;
    }

    public static int onOff() {
        BuildingShiftClient.turn(false);
        return Command.SINGLE_SUCCESS;
    }
}
