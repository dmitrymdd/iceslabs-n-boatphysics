package io.github.dmitrymdd.iceslabs;

import io.github.dmitrymdd.iceslabs.block.IceSlabBlock;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;

public final class ModBlocks {

	// Strength, friction and sound mirror the vanilla full blocks.
	public static final Block ICE_SLAB = register("ice_slab", IceSlabBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.ICE)
					.strength(0.5f)
					.friction(0.98f)
					.sound(SoundType.GLASS)
					.noOcclusion());

	public static final Block PACKED_ICE_SLAB = register("packed_ice_slab", SlabBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.ICE)
					.strength(0.5f)
					.friction(0.98f)
					.sound(SoundType.GLASS));

	public static final Block BLUE_ICE_SLAB = register("blue_ice_slab", SlabBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.ICE)
					.strength(2.8f)
					.friction(0.989f)
					.sound(SoundType.GLASS));

	private ModBlocks() {
	}

	/** Called from the mod initializer; loading this class registers the blocks. */
	public static void init() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(output -> {
			output.accept(ICE_SLAB.asItem());
			output.accept(PACKED_ICE_SLAB.asItem());
			output.accept(BLUE_ICE_SLAB.asItem());
		});
	}

	public static boolean isIceSlab(BlockState state) {
		Block block = state.getBlock();
		return block == ICE_SLAB || block == PACKED_ICE_SLAB || block == BLUE_ICE_SLAB;
	}

	private static Block register(String name, Function<BlockBehaviour.Properties, Block> factory,
								  BlockBehaviour.Properties properties) {
		Identifier id = Identifier.fromNamespaceAndPath(IceSlabs.MOD_ID, name);

		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
		Block block = Registry.register(BuiltInRegistries.BLOCK, blockKey, factory.apply(properties.setId(blockKey)));

		// useBlockDescriptionPrefix(): the item uses the block.iceslabs.* name, so no item.* lang keys are needed
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
		Registry.register(BuiltInRegistries.ITEM, itemKey,
				new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));

		return block;
	}
}
