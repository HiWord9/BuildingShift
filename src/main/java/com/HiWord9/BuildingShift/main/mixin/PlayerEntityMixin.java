package com.HiWord9.BuildingShift.main.mixin;

import com.HiWord9.BuildingShift.client.BuildingShiftClient;
import com.HiWord9.BuildingShift.main.BuildingShift;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Redirect(method = "shouldCancelInteraction", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSneaking()Z"))
    private boolean injected(PlayerEntity instance) {
        if (instance instanceof ServerPlayerEntity) {
            return BuildingShift.isEnabledFor(instance) || instance.isSneaking();
        }
        return (BuildingShiftClient.enabled) || instance.isSneaking();
    }
}
