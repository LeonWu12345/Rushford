package com.jam8ee.rushford.mixin;

import com.jam8ee.rushford.item.ModItems;
import com.jam8ee.rushford.sound.ModSounds;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class PlayerPoopMixin {

    @Shadow
    private int foodLevel;

    @Unique
    private int previousFoodLevel = -1;

    @Inject(method = "update", at = @At("HEAD"))
    private void onUpdate(PlayerEntity player, CallbackInfo ci) {
        // 初始化上一次的饱食度
        if (previousFoodLevel == -1) {
            previousFoodLevel = foodLevel;
        }

        // 检测：从未满 -> 满（20）
        if (previousFoodLevel < 20 && foodLevel >= 20) {
            if (!player.getWorld().isClient()) {
                doPoop(player);
            }
        }

        previousFoodLevel = foodLevel;
    }

    @Unique
    private void doPoop(PlayerEntity player) {
        ServerWorld world = (ServerWorld) player.getWorld();

        // 播放音效
        world.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                ModSounds.POOP,
                SoundCategory.PLAYERS,
                1.0F, 1.0F
        );

        // 在玩家身后掉落屎
        Vec3d lookVec = player.getRotationVector();
        double dropX = player.getX() - lookVec.x * 0.5;
        double dropY = player.getY() + 0.5;
        double dropZ = player.getZ() - lookVec.z * 0.5;

        ItemStack poopStack = new ItemStack(ModItems.poop);
        ItemEntity itemEntity = new ItemEntity(world, dropX, dropY, dropZ, poopStack);
        itemEntity.setVelocity(-lookVec.x * 0.1, 0.2, -lookVec.z * 0.1);
        itemEntity.setPickupDelay(40); // 2秒后才能捡

        world.spawnEntity(itemEntity);
    }
}