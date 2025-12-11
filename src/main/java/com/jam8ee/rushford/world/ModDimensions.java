package com.jam8ee.rushford.world;

import com.jam8ee.rushford.Rushford;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class ModDimensions {

    public static final RegistryKey<World> POOP_DIMENSION_KEY = RegistryKey.of(
            RegistryKeys.WORLD,
            Identifier.of(Rushford.MOD_ID, "poop_dimension")
    );

    public static final RegistryKey<DimensionType> POOP_DIMENSION_TYPE_KEY = RegistryKey.of(
            RegistryKeys.DIMENSION_TYPE,
            Identifier.of(Rushford.MOD_ID, "poop_dimension_type")
    );

    public static void register() {
        // 注册自定义区块生成器
        Registry.register(
                Registries.CHUNK_GENERATOR,
                Identifier.of(Rushford.MOD_ID, "poop_chunk_generator"),
                PoopChunkGenerator.CODEC
        );

        Rushford.LOGGER.info("Registering dimensions for " + Rushford.MOD_ID);
    }
}
