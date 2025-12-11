package com.jam8ee.rushford.mixin;

import com.jam8ee.rushford.Rushford;
import com.jam8ee.rushford.block.ModBlocks;
import com.jam8ee.rushford.block.PoopPortalLighter;
import net.minecraft.block.BlockState;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelMixin {

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void onUseOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        BlockPos clickedPos = context.getBlockPos();
        BlockPos firePos = clickedPos.offset(context.getSide());

        Rushford.LOGGER.info("Flint and steel used at: " + clickedPos + ", fire pos: " + firePos);
        Rushford.LOGGER.info("Clicked block: " + world.getBlockState(clickedPos).getBlock());

        if (world.isClient()) return;

        // 检查点击的位置旁边是否可以形成传送门
        if (world.getBlockState(firePos).isAir()) {
            Rushford.LOGGER.info("Trying to light poop portal at: " + firePos);

            if (PoopPortalLighter.tryLightPortal(world, firePos)) {
                Rushford.LOGGER.info("Poop portal lit successfully!");
                cir.setReturnValue(ActionResult.SUCCESS);
            } else {
                Rushford.LOGGER.info("Failed to light poop portal");
            }
        }
    }
}