package com.HiWord9.BuildingShift.client;

import com.HiWord9.BuildingShift.Constants;
import com.HiWord9.BuildingShift.client.event.KeyInputHandler;
import com.HiWord9.BuildingShift.server.BuildingShift;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BuildingShiftClient implements ClientModInitializer {
	public static boolean enabled = false;

	public static final Text MESSAGE_ON = Text.translatable("building-shift.turned.on").formatted(Formatting.GOLD);
	public static final Text MESSAGE_OFF = Text.translatable("building-shift.turned.off").formatted(Formatting.GRAY);

	@Override
	public void onInitializeClient() {
		Constants.LOGGER.info("Building Shift author likes coka-cola zero btw");
		KeyInputHandler.register();
		ClientCommandRegistrationCallback.EVENT.register(BuildingShiftClient::registerCommand);
	}

	public static void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		BuildingShiftClientCommand.register(dispatcher);
	}

	public static void setEnabled(boolean enabled) {
		BuildingShiftClient.enabled = enabled;
	}

	public static void toggle() {
		turn(!enabled);
	}

	public static void turn(boolean on) {
		BuildingShiftClient.setEnabled(on);
		PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
		BuildingShift.overlayStatus(player, enabled);
	}

	public static void overlayStatus(PlayerEntity player) {
		overlayStatus(player, enabled);
	}

	public static void overlayStatus(PlayerEntity player, boolean enabled) {
		player.sendMessage(enabled ? MESSAGE_ON : MESSAGE_OFF, true);
	}
}
