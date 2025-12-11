package com.jam8ee.rushford.world;

import com.jam8ee.rushford.block.ModBlocks;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PoopChunkGenerator extends ChunkGenerator {

    public static final MapCodec<PoopChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(gen -> gen.biomeSource)
            ).apply(instance, PoopChunkGenerator::new)
    );

    public PoopChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carverStep) {
        //不生成洞穴
    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {
    }

    @Override
    public void populateEntities(ChunkRegion region) {
        //不生成实体
    }

    @Override
    public int getWorldHeight() {
        return 128;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
        BlockState poopBlock = ModBlocks.POOP_BLOCK.getDefaultState();
        BlockState lava = Blocks.LAVA.getDefaultState();
        BlockState air = Blocks.AIR.getDefaultState();

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();

        //类似下界的生成
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 5; y++) {
                    mutable.set(startX + x, y, startZ + z);
                    chunk.setBlockState(mutable, poopBlock, false);
                }

                //中间层
                for (int y = 5; y < 120; y++) {
                    mutable.set(startX + x, y, startZ + z);

                    //使用简单的伪随机来决定是否放置方块
                    double noise = Math.sin((startX + x) * 0.1) * Math.cos((startZ + z) * 0.1) * Math.sin(y * 0.15);
                    double noise2 = Math.cos((startX + x) * 0.05 + y * 0.1) * Math.sin((startZ + z) * 0.05);

                    if (y < 32) {
                        //下层更密实
                        if (noise + noise2 > -0.3 || y < 10) {
                            chunk.setBlockState(mutable, poopBlock, false);
                        } else if (y < 31) {
                            chunk.setBlockState(mutable, lava, false);
                        }
                    } else if (y > 100) {
                        //顶层更密实
                        if (noise + noise2 > -0.3 || y > 115) {
                            chunk.setBlockState(mutable, poopBlock, false);
                        }
                    } else {
                        //中间层有更多空腔
                        if (noise + noise2 > 0.2) {
                            chunk.setBlockState(mutable, poopBlock, false);
                        }
                    }
                }

                //顶部封顶层
                for (int y = 120; y < 128; y++) {
                    mutable.set(startX + x, y, startZ + z);
                    chunk.setBlockState(mutable, poopBlock, false);
                }
            }
        }

        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getSeaLevel() {
        return 32;
    }

    @Override
    public int getMinimumY() {
        return 0;
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
        return 64;
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
        BlockState[] states = new BlockState[128];
        for (int i = 0; i < 128; i++) {
            states[i] = ModBlocks.POOP_BLOCK.getDefaultState();
        }
        return new VerticalBlockSample(0, states);
    }

    @Override
    public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {
        text.add("Poop Dimension");
    }
}