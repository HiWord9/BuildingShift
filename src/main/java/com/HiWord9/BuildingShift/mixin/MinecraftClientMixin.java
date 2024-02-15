package com.HiWord9.BuildingShift.mixin;

import com.HiWord9.BuildingShift.BuildingShift;
import net.minecraft.block.BlockState;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = MinecraftClient.class, priority = 800)
public class MinecraftClientMixin {
	@ModifyArg(
			method = "doItemUse",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;"
			),
			index = 2
	)
	private BlockHitResult injected(BlockHitResult hitResult) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (!BuildingShift.enabled || client.isInSingleplayer()) return hitResult;

		if (client.world == null || client.player == null) return hitResult;

		if (client.options.attackKey.isPressed()) return hitResult;

		if (
				!(client.player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof BlockItem) &&
				!(client.player.getStackInHand(Hand.OFF_HAND).getItem() instanceof BlockItem)
		) return hitResult;

		BlockPos oldBlockPos = hitResult.getBlockPos();
		BlockState oldTargetedBlockState = client.world.getBlockState(oldBlockPos);
		if (
				oldTargetedBlockState.isReplaceable() ||
				oldTargetedBlockState.getBlock() instanceof SlabBlock ||
				oldTargetedBlockState.getBlock() instanceof MultifaceGrowthBlock
		) return hitResult;

		BlockPos newBlockPos = hitResult.getBlockPos().add(hitResult.getSide().getVector());
		BlockState newTargetedBlockState = client.world.getBlockState(newBlockPos);
		if (
				!newTargetedBlockState.isReplaceable() ||
				(
						newTargetedBlockState.getBlock() instanceof SnowBlock &&
						newTargetedBlockState.get(SnowBlock.LAYERS) > 1
				)
		) return hitResult;

		Vec3d newPos = hitResult.getPos().add(Vec3d.of(hitResult.getSide().getVector()));

		return new BlockHitResult(
				newPos,
				hitResult.getSide(),
				newBlockPos,
				hitResult.isInsideBlock());
	}
}
