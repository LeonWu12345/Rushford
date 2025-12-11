package com.jam8ee.rushford.block.entity;

import com.jam8ee.rushford.Rushford;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {

    public static final ScreenHandlerType<ToiletScreenHandler> TOILET_SCREEN_HANDLER = Registry.register(
            Registries.SCREEN_HANDLER,
            Identifier.of(Rushford.MOD_ID, "toilet"),
            new ScreenHandlerType<>(ToiletScreenHandler::new, FeatureFlags.VANILLA_FEATURES)
    );

    public static void registerScreenHandlers() {
        Rushford.LOGGER.info("Registering screen handlers for " + Rushford.MOD_ID);
    }
}