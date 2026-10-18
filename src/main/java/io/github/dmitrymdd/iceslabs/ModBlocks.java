package io.github.dmitrymdd.iceslabs;

import io.github.dmitrymdd.iceslabs.block.IceSlabBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public final class ModBlocks {

	// Strength, slipperiness and sound mirror the vanilla full blocks.
	// MapColor.PALE_PURPLE is the ice map colour in Yarn (MapColor.ICE in Mojang mappings).
	public static final Block ICE_SLAB = register("ice_slab", IceSlabBlock::new,
			AbstractBlock.Settings.create()
					.mapColor(MapColor.PALE_PURPLE)
					.strength(0.5f)
					.slipperiness(0.98f)
					.sounds(BlockSoundGroup.GLASS)
					.nonOpaque());

	public static final Block PACKED_ICE_SLAB = register("packed_ice_slab", SlabBlock::new,
			AbstractBlock.Settings.create()
					.mapColor(MapColor.PALE_PURPLE)
					.strength(0.5f)
					.slipperiness(0.98f)
					.sounds(BlockSoundGroup.GLASS));

	public static final Block BLUE_ICE_SLAB = register("blue_ice_slab", SlabBlock::new,
			AbstractBlock.Settings.create()
					.mapColor(MapColor.PALE_PURPLE)
					.strength(2.8f)
					.slipperiness(0.989f)
					.sounds(BlockSoundGroup.GLASS));

	private ModBlocks() {
	}

	/** Called from the mod initializer; loading this class registers the blocks. */
	public static void init() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
			entries.add(ICE_SLAB);
			entries.add(PACKED_ICE_SLAB);
			entries.add(BLUE_ICE_SLAB);
		});
	}

	public static boolean isIceSlab(BlockState state) {
		Block block = state.getBlock();
		return block == ICE_SLAB || block == PACKED_ICE_SLAB || block == BLUE_ICE_SLAB;
	}

	private static Block register(String name, Function<AbstractBlock.Settings, Block> factory,
								  AbstractBlock.Settings settings) {
		Identifier id = Identifier.of(IceSlabs.MOD_ID, name);

		RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
		Block block = Registry.register(Registries.BLOCK, blockKey, factory.apply(settings.registryKey(blockKey)));

		// useBlockPrefixedTranslationKey(): the item uses the block.iceslabs.* name, so no item.* lang keys are needed
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
		Registry.register(Registries.ITEM, itemKey,
				new BlockItem(block, new Item.Settings().registryKey(itemKey).useBlockPrefixedTranslationKey()));

		return block;
	}
}
