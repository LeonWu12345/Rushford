package com.jam8ee.rushford.item;

import com.jam8ee.rushford.block.ModBlocks;
import net.fabricmc.fabric.api.registry.FuelRegistry;

public class ModFuels {

    public static void registerFuels() {
        // 1物品 = 200 ticks, 40物品 = 8000 ticks
        FuelRegistry.INSTANCE.add(ModBlocks.POOP_BLOCK.asItem(), 8000);
    }
}