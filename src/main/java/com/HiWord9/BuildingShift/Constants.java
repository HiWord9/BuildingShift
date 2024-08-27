package com.HiWord9.BuildingShift;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
    public static final String MOD_ID = "building-shift";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final String KEY_ON = "building-shift.turned.on";
    private static final String KEY_OFF = "building-shift.turned.off";

    public static final Text MESSAGE_ON = Text.of("Building Shift Turned On!").copy().formatted(Formatting.GOLD);
    public static final Text MESSAGE_OFF = Text.of("Building Shift Turned Off!").copy().formatted(Formatting.GRAY);
}
