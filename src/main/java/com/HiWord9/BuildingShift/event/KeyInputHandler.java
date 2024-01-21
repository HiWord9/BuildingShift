package com.HiWord9.BuildingShift.event;

import com.HiWord9.BuildingShift.BuildingShift;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
	public static final String KEY_CATEGORY = "key.category.building-shift";
	public static final String KEY_TOGGLE = "key.building-shift.toggle";

	public static KeyBinding toggleKey;

	public static void registerKeyInput() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (toggleKey.wasPressed()) {
				BuildingShift.toggle(client);
			}
		});
	}

	public static void register() {
		toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				KEY_TOGGLE,
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_G,
				KEY_CATEGORY
		));

		registerKeyInput();
	}
}
