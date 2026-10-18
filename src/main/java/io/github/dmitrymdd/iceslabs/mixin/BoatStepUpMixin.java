package io.github.dmitrymdd.iceslabs.mixin;

import io.github.dmitrymdd.iceslabs.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Lets boats climb onto ice slabs.
 * <p>
 * Boats normally have a step height of 0. We raise it only when an ice slab lies inside the area
 * the boat sweeps during this tick AND the slab's top is a climbable step above the boat's bottom.
 * getStepHeight() is queried by Entity.move() for the current tick's movement, which for boats is
 * getVelocity(), so the sweep matches the collision exactly. Slabs behind the boat or under it
 * do not count, so a boat riding on ice slabs cannot step onto other blocks.
 */
@Mixin(Entity.class)
public abstract class BoatStepUpMixin {

	@Unique
	private static final float ICESLABS_STEP_HEIGHT = 0.6f;

	@Inject(method = "getStepHeight", at = @At("HEAD"), cancellable = true)
	private void iceslabs$climbIceSlabs(CallbackInfoReturnable<Float> cir) {
		if (!((Object) this instanceof AbstractBoatEntity boat)) {
			return;
		}

		Vec3d motion = boat.getVelocity();
		if (motion.horizontalLengthSquared() < 1.0E-6) {
			return;
		}

		Box box = boat.getBoundingBox();
		Box swept = box.stretch(motion.x, 0.0, motion.z).expand(0.05, 0.0, 0.05);
		double bottom = box.minY;

		int minX = MathHelper.floor(swept.minX);
		int maxX = MathHelper.floor(swept.maxX);
		int minZ = MathHelper.floor(swept.minZ);
		int maxZ = MathHelper.floor(swept.maxZ);
		int minY = MathHelper.floor(bottom + 1.0E-3);
		int maxY = MathHelper.floor(bottom + ICESLABS_STEP_HEIGHT);

		World world = boat.getEntityWorld();
		BlockPos.Mutable pos = new BlockPos.Mutable();

		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				for (int z = minZ; z <= maxZ; z++) {
					BlockState state = world.getBlockState(pos.set(x, y, z));
					if (!ModBlocks.isIceSlab(state)) {
						continue;
					}
					double top = y + (state.get(SlabBlock.TYPE) == SlabType.BOTTOM ? 0.5 : 1.0);
					double rise = top - bottom;
					if (rise > 1.0E-3 && rise <= ICESLABS_STEP_HEIGHT) {
						cir.setReturnValue(ICESLABS_STEP_HEIGHT);
						return;
					}
				}
			}
		}
	}
}
