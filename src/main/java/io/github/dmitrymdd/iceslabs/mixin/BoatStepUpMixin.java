package io.github.dmitrymdd.iceslabs.mixin;

import io.github.dmitrymdd.iceslabs.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
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
 * maxUpStep() is queried by Entity.move() for the current tick's movement, which for boats is
 * getDeltaMovement(), so the sweep matches the collision exactly. Slabs behind the boat or under it
 * do not count, so a boat riding on ice slabs cannot step onto other blocks.
 */
@Mixin(Entity.class)
public abstract class BoatStepUpMixin {

	@Unique
	private static final float ICESLABS_STEP_HEIGHT = 0.6f;

	@Inject(method = "maxUpStep", at = @At("HEAD"), cancellable = true)
	private void iceslabs$climbIceSlabs(CallbackInfoReturnable<Float> cir) {
		if (!((Object) this instanceof AbstractBoat boat)) {
			return;
		}

		Vec3 motion = boat.getDeltaMovement();
		if (motion.horizontalDistanceSqr() < 1.0E-6) {
			return;
		}

		AABB box = boat.getBoundingBox();
		AABB swept = box.expandTowards(motion.x, 0.0, motion.z).inflate(0.05, 0.0, 0.05);
		double bottom = box.minY;

		int minX = Mth.floor(swept.minX);
		int maxX = Mth.floor(swept.maxX);
		int minZ = Mth.floor(swept.minZ);
		int maxZ = Mth.floor(swept.maxZ);
		int minY = Mth.floor(bottom + 1.0E-3);
		int maxY = Mth.floor(bottom + ICESLABS_STEP_HEIGHT);

		Level level = boat.level();
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				for (int z = minZ; z <= maxZ; z++) {
					BlockState state = level.getBlockState(pos.set(x, y, z));
					if (!ModBlocks.isIceSlab(state)) {
						continue;
					}
					double top = y + (state.getValue(SlabBlock.TYPE) == SlabType.BOTTOM ? 0.5 : 1.0);
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
