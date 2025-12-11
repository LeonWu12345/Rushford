package com.jam8ee.rushford.block;

import com.jam8ee.rushford.Rushford;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PoopPortalLighter {

    public static boolean tryLightPortal(World world, BlockPos pos) {
        Rushford.LOGGER.info("PoopPortalLighter.tryLightPortal called at: " + pos);

        // 尝试 X 轴方向
        PoopPortalShape shapeX = PoopPortalShape.findShape(world, pos, Direction.Axis.X);
        if (shapeX != null && shapeX.isValid()) {
            Rushford.LOGGER.info("Found valid X-axis portal shape!");
            shapeX.createPortal();
            return true;
        }

        // 尝试 Z 轴方向
        PoopPortalShape shapeZ = PoopPortalShape.findShape(world, pos, Direction.Axis.Z);
        if (shapeZ != null && shapeZ.isValid()) {
            Rushford.LOGGER.info("Found valid Z-axis portal shape!");
            shapeZ.createPortal();
            return true;
        }

        Rushford.LOGGER.info("No valid portal shape found");
        return false;
    }
}