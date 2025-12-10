package com.jam8ee.rushford;

import com.jam8ee.rushford.item.ModFuels;
import com.jam8ee.rushford.item.ModItems;
import com.jam8ee.rushford.sound.ModSounds;
import com.jam8ee.rushford.block.ModBlocks;
import com.jam8ee.rushford.item.ModItems;
import net.fabricmc.api.ModInitializer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rushford implements ModInitializer {
	public static final String MOD_ID = "rushford";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModItems.registerModItems();
		ModSounds.registerModSounds();
		ModBlocks.registerModBlocks();
		ModFuels.registerFuels();
		LOGGER.info("Hello Fabric world!");
	}
}