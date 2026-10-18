package io.github.dmitrymdd.iceslabs.client;

import io.github.dmitrymdd.iceslabs.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;

public class IceSlabsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Without this the ice slab is drawn in the solid layer and looks opaque.
		// (26.x picks the layer from the texture automatically, so main has no client code.)
		BlockRenderLayerMap.putBlock(ModBlocks.ICE_SLAB, BlockRenderLayer.TRANSLUCENT);
	}
}
