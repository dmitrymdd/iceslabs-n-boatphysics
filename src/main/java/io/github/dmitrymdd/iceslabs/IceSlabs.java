package io.github.dmitrymdd.iceslabs;

import net.fabricmc.api.ModInitializer;

public class IceSlabs implements ModInitializer {
	public static final String MOD_ID = "iceslabs";

	@Override
	public void onInitialize() {
		ModBlocks.init();
	}
}
