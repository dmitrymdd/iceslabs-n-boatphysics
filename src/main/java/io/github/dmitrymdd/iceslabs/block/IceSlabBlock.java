package io.github.dmitrymdd.iceslabs.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.util.math.Direction;

/**
 * Translucent ice slab. Like vanilla ice, it hides faces that are pressed against other ice,
 * so stacked or adjacent slabs do not show inner faces through each other.
 */
public class IceSlabBlock extends SlabBlock {

	public IceSlabBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		if (stateFrom.isOf(Blocks.ICE)) {
			return true;
		}
		if (stateFrom.isOf(this)) {
			SlabType mine = state.get(TYPE);
			SlabType other = stateFrom.get(TYPE);
			if (other == SlabType.DOUBLE) {
				return true;
			}
			return switch (direction) {
				case UP -> other == SlabType.BOTTOM;  // its bottom face touches our top
				case DOWN -> other == SlabType.TOP;   // its top half touches our bottom
				default -> other == mine;             // side faces only match same-height halves
			};
		}
		return super.isSideInvisible(state, stateFrom, direction);
	}
}
