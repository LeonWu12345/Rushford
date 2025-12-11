package com.jam8ee.rushford.block.entity;

import com.jam8ee.rushford.Rushford;
import com.jam8ee.rushford.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static final BlockEntityType<ToiletBlockEntity> TOILET_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Rushford.MOD_ID, "toilet_block_entity"),
            BlockEntityType.Builder.create(ToiletBlockEntity::new, ModBlocks.TOILET).build()
    );

    public static void registerBlockEntities() {
        Rushford.LOGGER.info("Registering block entities for " + Rushford.MOD_ID);
    }
}
