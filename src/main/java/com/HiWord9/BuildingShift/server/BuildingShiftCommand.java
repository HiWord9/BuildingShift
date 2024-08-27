package com.HiWord9.BuildingShift.server;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class BuildingShiftCommand {

    static RequiredArgumentBuilder<ServerCommandSource, EntitySelector> target = CommandManager
            .argument("target", EntityArgumentType.player())
            .requires(source -> source.hasPermissionLevel(2));


    public static RequiredArgumentBuilder<ServerCommandSource, EntitySelector> withTarget(PlayerTargetExecutable executable) {
        return target.executes(
                context -> executable.run(
                        context.getSource(),
                        EntityArgumentType.getPlayer(context, "target")
                )
        );
    }

    public interface PlayerTargetExecutable {
        int run(ServerCommandSource context, ServerPlayerEntity player) throws CommandSyntaxException;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager
                .literal("buildingshift")
                .then(CommandManager
                        .literal("toggle")
                        .executes(context -> onToggle(context.getSource()))
                        .then(withTarget(BuildingShiftCommand::onToggle)))
                .then(CommandManager
                        .literal("on")
                        .executes(context -> onOn(context.getSource()))
                        .then(withTarget(BuildingShiftCommand::onOn)))
                .then(CommandManager
                        .literal("off")
                        .executes(context -> onOff(context.getSource()))
                        .then(withTarget(BuildingShiftCommand::onOff))));
    }

    public static int onToggle(ServerCommandSource context) {
        return onToggle(context, context.getPlayer());
    }

    public static int onToggle(ServerCommandSource context, ServerPlayerEntity player) {
//        BuildingShiftClient.toggle(context.getClient());
        System.out.println("onToggle");
        return Command.SINGLE_SUCCESS;
    }

    public static int onOn(ServerCommandSource context) {
        return onOn(context, context.getPlayer());
    }

    public static int onOn(ServerCommandSource context, ServerPlayerEntity player) {
//        BuildingShiftClient.turn(true, context.getClient());
        System.out.println("onOn");
        return Command.SINGLE_SUCCESS;
    }

    public static int onOff(ServerCommandSource context) {
        return onOff(context, context.getPlayer());
    }

    public static int onOff(ServerCommandSource context, ServerPlayerEntity player) {
//        BuildingShiftClient.turn(false, context.getClient());
        System.out.println("onOff");
        return Command.SINGLE_SUCCESS;
    }
}
