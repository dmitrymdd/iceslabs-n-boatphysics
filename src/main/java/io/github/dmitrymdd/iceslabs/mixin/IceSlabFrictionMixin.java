package io.github.dmitrymdd.iceslabs.mixin;

import io.github.dmitrymdd.iceslabs.ModBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Makes entities standing on an ice slab slide like on full ice.
 * <p>
 * Vanilla looks for the "velocity affecting" block at y - 0.500001, which for an entity standing
 * on a bottom slab is the block BELOW the slab, so the slab's slipperiness is ignored. We scan the
 * whole footprint of the bounding box just under the feet and return the ice slab if there is one.
 * Applies to every entity (players, mobs, dropped items), like vanilla ice.
 */
@Mixin(Entity.class)
public abstract class IceSlabFrictionMixin {

	@Inject(method = "getVelocityAffectingPos", at = @At("HEAD"), cancellable = true)
	private void iceslabs$useIceSlabUnderFeet(CallbackInfoReturnable<BlockPos> cir) {
		Entity self = (Entity) (Object) this;
		if (!self.isOnGround()) {
			return;
		}

		Box box = self.getBoundingBox();
		int minX = MathHelper.floor(box.minX);
		int maxX = MathHelper.floor(box.maxX - 1.0E-3);
		int minZ = MathHelper.floor(box.minZ);
		int maxZ = MathHelper.floor(box.maxZ - 1.0E-3);
		int y = MathHelper.floor(box.minY - 0.05);

		World world = self.getEntityWorld();
		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				BlockPos pos = new BlockPos(x, y, z);
				if (ModBlocks.isIceSlab(world.getBlockState(pos))) {
					cir.setReturnValue(pos);
					return;
				}
			}
		}
	}
}
