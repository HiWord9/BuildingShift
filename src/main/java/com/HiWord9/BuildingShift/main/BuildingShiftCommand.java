package com.HiWord9.BuildingShift.main;

import com.HiWord9.BuildingShift.Constants;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BuildingShiftCommand {

    public static RequiredArgumentBuilder<ServerCommandSource, EntitySelector> withTarget(PlayerTargetExecutable executable) {
        return CommandManager
                .argument("target", EntityArgumentType.player())
                .requires(source -> source.hasPermissionLevel(2))
                .executes(
                        context -> executable.run(
                                context.getSource(),
                                EntityArgumentType.getPlayer(context, "target")
                        )
                );
    }

    public interface PlayerTargetExecutable {
        int run(ServerCommandSource context, ServerPlayerEntity target) throws CommandSyntaxException;
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
                        .then(withTarget(BuildingShiftCommand::onOff)))
        );
    }

    public static int onToggle(ServerCommandSource context) throws CommandSyntaxException {
        context.getPlayerOrThrow();
        return onToggle(context, null);
    }

    public static int onToggle(ServerCommandSource context, ServerPlayerEntity target) {
        ServerPlayerEntity player = target == null ? context.getPlayer() : target;
        boolean enabled = BuildingShift.toggleFor(player);
        outputResult(context, target, enabled);
        return Command.SINGLE_SUCCESS;
    }

    public static int onOn(ServerCommandSource context) throws CommandSyntaxException {
        context.getPlayerOrThrow();
        return onOn(context, null);
    }

    public static int onOn(ServerCommandSource context, ServerPlayerEntity target) {
        ServerPlayerEntity player = target == null ? context.getPlayer() : target;
        BuildingShift.enableFor(player);
        outputResult(context, target, true);
        return Command.SINGLE_SUCCESS;
    }

    public static int onOff(ServerCommandSource context) throws CommandSyntaxException {
        context.getPlayerOrThrow();
        return onOff(context, null);
    }

    public static int onOff(ServerCommandSource context, ServerPlayerEntity target) {
        ServerPlayerEntity player = target == null ? context.getPlayer() : target;
        BuildingShift.disableFor(player);
        outputResult(context, target, false);
        return Command.SINGLE_SUCCESS;
    }

    public static void outputResult(ServerCommandSource context, ServerPlayerEntity target, boolean enabled) {
        ServerPlayerEntity player = target == null ? context.getPlayer() : target;
        Constants.LOGGER.info(
                "Building Shift {} for {}",
                enabled ? "Enabled" : "Disabled",
                player == null ? null : player.getName().getString()
        );
        if (BuildingShift.hasMod(player)) return;

        if (player != null) {
            BuildingShift.overlayStatus(player, enabled);
        }

        String message = String.format("Building Shift Turned %s", enabled ? "on" : "off");
        if (target != null) {
            message = message + String.format(" for %s", target.getName().getString());
        }
        context.sendMessage(Text.of(message).copy().formatted(enabled ? Formatting.GOLD : Formatting.GRAY));
    }
}
