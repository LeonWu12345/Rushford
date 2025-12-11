package com.jam8ee.rushford.world;

import com.jam8ee.rushford.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.border.WorldBorder;

public class PoopTeleporter {

    public static void teleport(ServerPlayerEntity player, ServerWorld targetWorld, BlockPos portalPos) {
        // 计算目标位置（和下界一样的8倍坐标缩放）
        double scale = targetWorld.getRegistryKey() == ModDimensions.POOP_DIMENSION_KEY ? 0.125 : 8.0;

        double targetX = portalPos.getX() * scale;
        double targetZ = portalPos.getZ() * scale;

        // 确保在世界边界内
        WorldBorder border = targetWorld.getWorldBorder();
        targetX = Math.min(Math.max(targetX, border.getBoundWest() + 16), border.getBoundEast() - 16);
        targetZ = Math.min(Math.max(targetZ, border.getBoundNorth() + 16), border.getBoundSouth() - 16);

        BlockPos targetPos = new BlockPos((int) targetX, portalPos.getY(), (int) targetZ);

        // 查找或创建传送门
        BlockPos foundPortal = findOrCreatePortal(targetWorld, targetPos);

        // 传送玩家
        TeleportTarget target = new TeleportTarget(
                targetWorld,
                foundPortal.toCenterPos().add(0, 0.5, 0),
                player.getVelocity(),
                player.getYaw(),
                player.getPitch(),
                TeleportTarget.NO_OP
        );

        player.teleportTo(target);
    }

    private static BlockPos findOrCreatePortal(ServerWorld world, BlockPos targetPos) {
        // 在目标位置附近搜索现有传送门
        int searchRadius = 128;
        BlockPos foundPortal = null;
        double closestDistance = Double.MAX_VALUE;

        for (int x = -searchRadius; x <= searchRadius; x += 8) {
            for (int z = -searchRadius; z <= searchRadius; z += 8) {
                for (int y = world.getBottomY(); y < world.getTopY(); y++) {
                    BlockPos checkPos = targetPos.add(x, y - targetPos.getY(), z);
                    if (world.getBlockState(checkPos).isOf(ModBlocks.POOP_PORTAL)) {
                        double distance = checkPos.getSquaredDistance(targetPos);
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            foundPortal = checkPos;
                        }
                    }
                }
            }
        }

        if (foundPortal != null) {
            return foundPortal;
        }

        // 没找到，创建新传送门
        return createPortal(world, targetPos);
    }

    private static BlockPos createPortal(ServerWorld world, BlockPos pos) {
        // 找一个安全的Y位置
        int y = 64;
        for (int checkY = 64; checkY < 128; checkY++) {
            BlockPos checkPos = new BlockPos(pos.getX(), checkY, pos.getZ());
            if (world.getBlockState(checkPos).isAir() && world.getBlockState(checkPos.up()).isAir()) {
                y = checkY;
                break;
            }
        }

        BlockPos portalBase = new BlockPos(pos.getX(), y, pos.getZ());

        // 创建传送门框架（和下界传送门一样的结构）
        BlockState frame = ModBlocks.POOP_BLOCK.getDefaultState();
        BlockState portal = ModBlocks.POOP_PORTAL.getDefaultState();

        // 底部
        for (int x = -1; x <= 2; x++) {
            world.setBlockState(portalBase.add(x, -1, 0), frame);
        }

        // 顶部
        for (int x = -1; x <= 2; x++) {
            world.setBlockState(portalBase.add(x, 3, 0), frame);
        }

        // 左右两侧
        for (int yOffset = 0; yOffset < 3; yOffset++) {
            world.setBlockState(portalBase.add(-1, yOffset, 0), frame);
            world.setBlockState(portalBase.add(2, yOffset, 0), frame);
        }

        // 传送门方块
        for (int x = 0; x < 2; x++) {
            for (int yOffset = 0; yOffset < 3; yOffset++) {
                world.setBlockState(portalBase.add(x, yOffset, 0), portal);
            }
        }

        return portalBase;
    }
}