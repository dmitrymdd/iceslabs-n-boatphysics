package io.github.dmitrymdd.iceslabs.mixin;

import io.github.dmitrymdd.iceslabs.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Makes entities standing on an ice slab slide like on full ice.
 * <p>
 * Vanilla looks for the "movement" block at y - 0.500001, which for an entity standing on a
 * bottom slab is the block BELOW the slab, so the slab's friction is ignored. We scan the whole
 * footprint of the bounding box just under the feet and return the ice slab if there is one.
 * Applies to every entity (players, mobs, dropped items), like vanilla ice.
 */
@Mixin(Entity.class)
public abstract class IceSlabFrictionMixin {

	@Inject(method = "getBlockPosBelowThatAffectsMyMovement", at = @At("HEAD"), cancellable = true)
	private void iceslabs$useIceSlabUnderFeet(CallbackInfoReturnable<BlockPos> cir) {
		Entity self = (Entity) (Object) this;
		if (!self.onGround()) {
			return;
		}

		AABB box = self.getBoundingBox();
		int minX = Mth.floor(box.minX);
		int maxX = Mth.floor(box.maxX - 1.0E-3);
		int minZ = Mth.floor(box.minZ);
		int maxZ = Mth.floor(box.maxZ - 1.0E-3);
		int y = Mth.floor(box.minY - 0.05);

		Level level = self.level();
		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				BlockPos pos = new BlockPos(x, y, z);
				if (ModBlocks.isIceSlab(level.getBlockState(pos))) {
					cir.setReturnValue(pos);
					return;
				}
			}
		}
	}
}
