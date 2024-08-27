package com.HiWord9.BuildingShift.client.mixin;

import com.HiWord9.BuildingShift.Constants;
import com.HiWord9.BuildingShift.client.BuildingShiftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Unique
    Identifier INDICATOR = Identifier.of(Constants.MOD_ID, "textures/indicator.png");

    @Inject(
            method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void injected(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!BuildingShiftClient.enabled) return;
        final int scaledWidth = context.getScaledWindowWidth();
        final int scaledHeight = context.getScaledWindowHeight();
        context.drawTexture(
                INDICATOR,
                (scaledWidth - 15) / 2, (scaledHeight - 15) / 2,
                0, 0,
                15, 15,
                15, 15
        );
    }
}
