package io.github.dmitrymdd.iceslabs.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

/**
 * Translucent ice slab. Like vanilla ice, it hides faces that are pressed against other ice,
 * so stacked or adjacent slabs do not show inner faces through each other.
 */
public class IceSlabBlock extends SlabBlock {

	public IceSlabBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public boolean skipRendering(BlockState state, BlockState adjacent, Direction direction) {
		if (adjacent.is(Blocks.ICE)) {
			return true;
		}
		if (adjacent.is(this)) {
			SlabType mine = state.getValue(TYPE);
			SlabType other = adjacent.getValue(TYPE);
			if (other == SlabType.DOUBLE) {
				return true;
			}
			return switch (direction) {
				case UP -> other == SlabType.BOTTOM;  // its bottom face touches our top
				case DOWN -> other == SlabType.TOP;   // its top half touches our bottom
				default -> other == mine;             // side faces only match same-height halves
			};
		}
		return super.skipRendering(state, adjacent, direction);
	}
}
