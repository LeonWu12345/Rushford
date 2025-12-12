package com.jam8ee.rushford.poop;

import com.jam8ee.rushford.block.entity.ToiletBlockEntity;
import com.jam8ee.rushford.entity.ToiletEntity;
import com.jam8ee.rushford.item.ModItems;
import com.jam8ee.rushford.network.PoopMeterSyncPayload;
import com.jam8ee.rushford.sound.ModSounds;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
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

        // 只有坐在马桶上才能让屎进入马桶物品栏
        Entity vehicle = player.getVehicle();
        if (vehicle instanceof ToiletEntity toiletEntity) {
            BlockPos toiletPos = toiletEntity.getToiletPos();
            if (toiletPos != null && world.getBlockEntity(toiletPos) instanceof ToiletBlockEntity toilet) {
                ItemStack poopStack = new ItemStack(ModItems.POOP);
                if (toilet.addPoop(poopStack)) {
                    playPoopSound(world, player);
                    return;
                }
                // 马桶满了，掉落到地上
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