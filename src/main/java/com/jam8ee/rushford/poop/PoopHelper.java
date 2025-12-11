package com.jam8ee.rushford.poop;

import com.jam8ee.rushford.block.ModBlocks;
import com.jam8ee.rushford.block.entity.ToiletBlockEntity;
import com.jam8ee.rushford.item.ModItems;
import com.jam8ee.rushford.network.PoopMeterSyncPayload;
import com.jam8ee.rushford.sound.ModSounds;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PoopHelper {

    public static void doPoop(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        BlockPos playerPos = player.getBlockPos();

        // 检查玩家是否坐在马桶上
        BlockState belowState = world.getBlockState(playerPos);
        if (belowState.isOf(ModBlocks.TOILET)) {
            // 坐在马桶上，屎进入马桶物品栏
            if (world.getBlockEntity(playerPos) instanceof ToiletBlockEntity toilet) {
                ItemStack poopStack = new ItemStack(ModItems.POOP);
                if (toilet.addPoop(poopStack)) {
                    playPoopSound(world, player);
                    return;
                }
            }
        }

        // 检查玩家下方一格是否是马桶
        BlockPos belowPos = playerPos.down();
        BlockState belowOneState = world.getBlockState(belowPos);
        if (belowOneState.isOf(ModBlocks.TOILET)) {
            if (world.getBlockEntity(belowPos) instanceof ToiletBlockEntity toilet) {
                ItemStack poopStack = new ItemStack(ModItems.POOP);
                if (toilet.addPoop(poopStack)) {
                    playPoopSound(world, player);
                    return;
                }
            }
        }

        // 普通拉屎，掉落物品
        playPoopSound(world, player);
        dropPoop(world, player);
    }

    private static void playPoopSound(ServerWorld world, ServerPlayerEntity player) {
        world.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                ModSounds.POOP,
                SoundCategory.PLAYERS,
                1.0F, 1.0F
        );
    }

    private static void dropPoop(ServerWorld world, ServerPlayerEntity player) {
        Vec3d lookVec = player.getRotationVector();
        double dropX = player.getX() - lookVec.x * 0.5;
        double dropY = player.getY() + 0.5;
        double dropZ = player.getZ() - lookVec.z * 0.5;

        ItemStack poopStack = new ItemStack(ModItems.POOP);
        ItemEntity itemEntity = new ItemEntity(world, dropX, dropY, dropZ, poopStack);
        itemEntity.setVelocity(-lookVec.x * 0.1, 0.2, -lookVec.z * 0.1);
        itemEntity.setPickupDelay(40);

        world.spawnEntity(itemEntity);
    }

    public static void syncPoopLevel(ServerPlayerEntity player, int level) {
        ServerPlayNetworking.send(player, new PoopMeterSyncPayload(level));
    }
}