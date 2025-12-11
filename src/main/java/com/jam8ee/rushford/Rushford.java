package com.jam8ee.rushford;

import com.jam8ee.rushford.effect.ModEffects;
import com.jam8ee.rushford.event.ModEvents;
import com.jam8ee.rushford.item.ModFuels;
import com.jam8ee.rushford.item.ModItems;
import com.jam8ee.rushford.network.ModNetworking;
import com.jam8ee.rushford.network.PoopMeterSyncPayload;
import com.jam8ee.rushford.sound.ModSounds;
import com.jam8ee.rushford.block.ModBlocks;
import com.jam8ee.rushford.item.ModItems;
import net.fabricmc.api.ModInitializer;


import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rushford implements ModInitializer {
	public static final String MOD_ID = "rushford";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		PayloadTypeRegistry.playS2C().register(PoopMeterSyncPayload.ID, PoopMeterSyncPayload.CODEC);

		ModItems.registerModItems();
		ModSounds.registerModSounds();
		ModBlocks.registerModBlocks();
		ModFuels.registerFuels();
		ModEvents.registerEvents();
		ModEffects.registerModEffects();
//		ModNetworking.registerS2CPayloads();

		LOGGER.info("Hello Fabric world!");
	}
}