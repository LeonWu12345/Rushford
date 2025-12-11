package com.jam8ee.rushford.block;

import com.jam8ee.rushford.Rushford;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

public class ModBlocks {

    public static final Block POOP_BLOCK = registerBlock("poop_block",
            new Block(AbstractBlock.Settings.create()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.SLIME)
            ));

    public static final Block TOILET = registerBlock("toilet",
            new ToiletBlock(AbstractBlock.Settings.create()
                    .strength(1.5f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
            ));

    public static final Block POOP_PORTAL = Registry.register(
            Registries.BLOCK,
            Identifier.of(Rushford.MOD_ID, "poop_portal"),
            new PoopPortalBlock(AbstractBlock.Settings.copy(Blocks.NETHER_PORTAL)
                    .noCollision()
                    .ticksRandomly()
                    .strength(-1.0f)
                    .sounds(BlockSoundGroup.GLASS)
                    .luminance(state -> 11)
            ));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Rushford.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, Identifier.of(Rushford.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        Rushford.LOGGER.info("Registering blocks for " + Rushford.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> {
            content.add(POOP_BLOCK);
        });
    }
}