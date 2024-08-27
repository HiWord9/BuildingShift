package com.HiWord9.BuildingShift.server.mixin;

import com.HiWord9.BuildingShift.server.BuildingShift;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Redirect(method = "shouldCancelInteraction", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSneaking()Z"))
    private boolean injected(PlayerEntity instance) {
        return BuildingShift.isEnabledFor(instance) || instance.isSneaking();
    }
}
