package com.jam8ee.rushford.block;

import com.jam8ee.rushford.world.ModDimensions;
import com.jam8ee.rushford.world.PoopTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PoopPortalBlock extends NetherPortalBlock {

    public PoopPortalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClient()) return;
        if (entity.hasPortalCooldown()) return;
        if (!(entity instanceof ServerPlayerEntity player)) return;

        ServerWorld serverWorld = (ServerWorld) world;
        ServerWorld targetWorld;

        // 判断当前在哪个维度，决定传送目标
        if (world.getRegistryKey() == ModDimensions.POOP_DIMENSION_KEY) {
            // 在屎维度，传送到主世界
            targetWorld = serverWorld.getServer().getWorld(World.OVERWORLD);
        } else {
            // 在其他维度，传送到屎维度
            targetWorld = serverWorld.getServer().getWorld(ModDimensions.POOP_DIMENSION_KEY);
        }

        if (targetWorld != null) {
            player.setPortalCooldown(100); // 5秒冷却
            PoopTeleporter.teleport(player, targetWorld, pos);
        }
    }
}
