package com.jam8ee.rushford.block;

import com.jam8ee.rushford.Rushford;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PoopPortalShape {

    private final World world;
    private final Direction.Axis axis;
    private final BlockPos lowerCorner;
    private final int width;
    private final int height;

    private PoopPortalShape(World world, BlockPos lowerCorner, Direction.Axis axis, int width, int height) {
        this.world = world;
        this.lowerCorner = lowerCorner;
        this.axis = axis;
        this.width = width;
        this.height = height;
    }

    public static PoopPortalShape findShape(World world, BlockPos pos, Direction.Axis axis) {
        Rushford.LOGGER.info("Finding shape at " + pos + " with axis " + axis);

        // X轴传送门 = 沿X方向延伸 = 面朝南北
        // Z轴传送门 = 沿Z方向延伸 = 面朝东西
        Direction rightDir = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        Direction leftDir = rightDir.getOpposite();

        // 找到最左下角
        BlockPos corner = findLowerLeftCorner(world, pos, leftDir);
        if (corner == null) {
            Rushford.LOGGER.info("Could not find lower left corner");
            return null;
        }

        Rushford.LOGGER.info("Found lower left corner at: " + corner);

        // 计算宽度
        int width = calculateWidth(world, corner, rightDir);
        Rushford.LOGGER.info("Width: " + width);
        if (width < 2 || width > 21) return null;

        // 计算高度
        int height = calculateHeight(world, corner, rightDir, leftDir, width);
        Rushford.LOGGER.info("Height: " + height);
        if (height < 3 || height > 21) return null;

        return new PoopPortalShape(world, corner, axis, width, height);
    }

    private static BlockPos findLowerLeftCorner(World world, BlockPos pos, Direction leftDir) {
        // 向下移动直到碰到框架
        BlockPos current = pos;
        while (isEmpty(world, current.down()) && current.getY() > world.getBottomY()) {
            current = current.down();
        }

        // 检查下方是否是屎块
        if (!isFrame(world, current.down())) {
            return null;
        }

        // 向左移动直到碰到框架
        while (isEmpty(world, current.offset(leftDir))) {
            current = current.offset(leftDir);
        }

        // 检查左边是否是屎块
        if (!isFrame(world, current.offset(leftDir))) {
            return null;
        }

        return current;
    }

    private static int calculateWidth(World world, BlockPos corner, Direction rightDir) {
        int width = 0;
        BlockPos current = corner;

        while (width < 21) {
            if (!isEmpty(world, current)) break;
            if (!isFrame(world, current.down())) break;

            width++;
            current = current.offset(rightDir);
        }

        // 检查右边框架
        if (!isFrame(world, current)) {
            return 0;
        }

        return width;
    }

    private static int calculateHeight(World world, BlockPos corner, Direction rightDir, Direction leftDir, int width) {
        int height = 0;

        while (height < 21) {
            // 检查这一行是否都是空的
            boolean rowEmpty = true;
            for (int w = 0; w < width; w++) {
                BlockPos checkPos = corner.offset(rightDir, w).up(height);
                if (!isEmpty(world, checkPos)) {
                    rowEmpty = false;
                    break;
                }
            }

            if (!rowEmpty) break;

            // 检查两侧框架
            BlockPos leftCheck = corner.offset(leftDir).up(height);
            BlockPos rightCheck = corner.offset(rightDir, width).up(height);
            if (!isFrame(world, leftCheck) || !isFrame(world, rightCheck)) {
                break;
            }

            height++;
        }

        // 检查顶部框架
        for (int w = 0; w < width; w++) {
            BlockPos topPos = corner.offset(rightDir, w).up(height);
            if (!isFrame(world, topPos)) {
                return 0;
            }
        }

        return height;
    }

    private static boolean isEmpty(World world, BlockPos pos) {
        return world.getBlockState(pos).isAir();
    }

    private static boolean isFrame(World world, BlockPos pos) {
        return world.getBlockState(pos).isOf(ModBlocks.POOP_BLOCK);
    }

    public boolean isValid() {
        return width >= 2 && width <= 21 && height >= 3 && height <= 21;
    }

    public void createPortal() {
        Direction rightDir = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;


        BlockState portalState = ModBlocks.POOP_PORTAL.getDefaultState()
                .with(NetherPortalBlock.AXIS, axis);

        Rushford.LOGGER.info("Creating portal with axis=" + axis + ", width=" + width + ", height=" + height);

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                BlockPos portalPos = lowerCorner.offset(rightDir, w).up(h);
                world.setBlockState(portalPos, portalState);
            }
        }
    }
}