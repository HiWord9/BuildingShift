package com.HiWord9.BuildingShift;

import com.HiWord9.BuildingShift.event.KeyInputHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildingShift implements ClientModInitializer {
	public static final String MOD_ID = "building-shift";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean enabled = false;

	public static final String KEY_ON = "building-shift.turned.on";
	public static final String KEY_OFF = "building-shift.turned.off";

	@Override
	public void onInitializeClient() {
		LOGGER.info("Building Shift author likes coka-cola zero btw");
		KeyInputHandler.register();
		ClientCommandRegistrationCallback.EVENT.register(BuildingShift::registerCommand);
	}

	public static void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		BuildingShiftCommand.register(dispatcher);
	}

	public static void setEnabled(boolean enabled) {
		BuildingShift.enabled = enabled;
	}

	public static void toggle(MinecraftClient client) {
		turn(!enabled, client);
	}

	public static void turn(boolean on, MinecraftClient client) {
		BuildingShift.setEnabled(on);
		overlayStatus(client);
	}

	public static void overlayStatus(MinecraftClient client) {
		if (client.player == null) return;
		Text text;
		if (BuildingShift.enabled) {
			text = Text.translatable(KEY_ON).fillStyle(Style.EMPTY.withColor(Formatting.GOLD));
		} else {
			text = Text.translatable(KEY_OFF).fillStyle(Style.EMPTY.withColor(Formatting.GRAY));
		}
		client.player.sendMessage(text, true);
	}
}
