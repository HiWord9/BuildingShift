package com.HiWord9.BuildingShift;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class BuildingShiftCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("buildingshift")
                .then(literal("toggle")
                        .executes(context -> onToggle(context.getSource())))
                .then(literal("on")
                        .executes(context -> onOn(context.getSource())))
                .then(literal("off")
                        .executes(context -> onOff(context.getSource()))));
    }

    public static int onToggle(FabricClientCommandSource context) {
        BuildingShift.toggle(context.getClient());
        return Command.SINGLE_SUCCESS;
    }

    public static int onOn(FabricClientCommandSource context) {
        BuildingShift.turn(true, context.getClient());
        return Command.SINGLE_SUCCESS;
    }

    public static int onOff(FabricClientCommandSource context) {
        BuildingShift.turn(false, context.getClient());
        return Command.SINGLE_SUCCESS;
    }
}
