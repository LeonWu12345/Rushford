package com.jam8ee.rushford.item;

import com.jam8ee.rushford.Rushford;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item poop = registerItems("poop", new Item(new Item.Settings()));
    private static Item registerItems(String id, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Rushford.MOD_ID, id), item);
    }

    public static void registerModItems() {
        Rushford.LOGGER.info("REGISTERING ITEMS");
    }
}
