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

public class BuildingShiftClient implements ClientModInitializer {
	public static boolean enabled = false;

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
        if (client.player == null) return;
		BuildingShift.overlayStatus(client.player, enabled);;
	}
}
