package com.HiWord9.BuildingShift.client;

import com.HiWord9.BuildingShift.Constants;
import com.HiWord9.BuildingShift.client.event.KeyInputHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BuildingShiftClient implements ClientModInitializer {
	public static boolean enabled = false;

	public static final String KEY_ON = "building-shift.turned.on";
	public static final String KEY_OFF = "building-shift.turned.off";

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

	public static void toggle(MinecraftClient client) {
		turn(!enabled, client);
	}

	public static void turn(boolean on, MinecraftClient client) {
		BuildingShiftClient.setEnabled(on);
		overlayStatus(client);
	}

	public static void overlayStatus(MinecraftClient client) {
		if (client.player == null) return;
		Text text;
		if (BuildingShiftClient.enabled) {
			text = Text.translatable(KEY_ON).fillStyle(Style.EMPTY.withColor(Formatting.GOLD));
		} else {
			text = Text.translatable(KEY_OFF).fillStyle(Style.EMPTY.withColor(Formatting.GRAY));
		}
		client.player.sendMessage(text, true);
	}
}
