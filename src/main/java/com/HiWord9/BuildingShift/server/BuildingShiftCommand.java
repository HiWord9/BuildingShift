package com.HiWord9.BuildingShift.server;

import com.HiWord9.BuildingShift.Constants;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
//                        .requires(source -> source.getPlayer() != null)
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
        return onToggle(context, null);
    }

    public static int onToggle(ServerCommandSource context, ServerPlayerEntity player) {
        ServerPlayerEntity target = player == null ? context.getPlayer() : player;
        boolean enabled = BuildingShift.toggleFor(target);
        if (target != null) {
            BuildingShift.overlayStatus(target, enabled);
        }
        logResult(context, player, enabled);
        return Command.SINGLE_SUCCESS;
    }

    public static int onOn(ServerCommandSource context) {
        return onOn(context, null);
    }

    public static int onOn(ServerCommandSource context, ServerPlayerEntity player) {
        ServerPlayerEntity target = player == null ? context.getPlayer() : player;
        BuildingShift.enableFor(target);
        if (target != null) {
            BuildingShift.overlayStatus(target, true);
        }
        logResult(context, player, true);
        return Command.SINGLE_SUCCESS;
    }

    public static int onOff(ServerCommandSource context) {
        return onOff(context, null);
    }

    public static int onOff(ServerCommandSource context, ServerPlayerEntity player) {
        ServerPlayerEntity target = player == null ? context.getPlayer() : player;
        BuildingShift.disableFor(target);
        if (target != null) {
            BuildingShift.overlayStatus(target, false);
        }
        logResult(context, player, false);
        return Command.SINGLE_SUCCESS;
    }

    public static void logResult(ServerCommandSource context, ServerPlayerEntity player, boolean enabled) {
        ServerPlayerEntity target = player == null ? context.getPlayer() : player;
        String message = String.format("Building Shift Turned %s", enabled ? "on" : "off");
        if (player != null) {
            message = message + String.format(" for %s", player.getName().getString());
        }
        Constants.LOGGER.debug(
                "Building Shift {} for {}",
                enabled ? "Enabled" : "Disabled",
                target == null ? null : target.getName().getString()
        );
        context.sendMessage(Text.of(message).copy().formatted(enabled ? Formatting.GOLD : Formatting.GRAY));
    }
}
