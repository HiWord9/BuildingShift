package com.HiWord9.BuildingShift.server.mixin;

import com.HiWord9.BuildingShift.client.BuildingShiftClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Redirect(method = "shouldCancelInteraction", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSneaking()Z"))
    private boolean injected(PlayerEntity instance) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!client.isInSingleplayer() || client.player == null) return instance.isSneaking();
        boolean preCancel = BuildingShiftClient.enabled && instance.getGameProfile().equals(client.player.getGameProfile());
        return preCancel || instance.isSneaking();
    }
}
