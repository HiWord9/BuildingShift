package com.HiWord9.BuildingShift.mixin;

import com.HiWord9.BuildingShift.BuildingShift;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    Identifier INDICATOR = new Identifier(BuildingShift.MOD_ID, "textures/indicator.png");

    @Inject(
            method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void injected(DrawContext context, CallbackInfo ci) {
        if (!BuildingShift.enabled) return;
        context.drawTexture(INDICATOR, (scaledWidth - 15) / 2, (scaledHeight - 15) / 2, 0, 0, 15, 15, 15, 15);
    }
}
