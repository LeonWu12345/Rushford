package com.jam8ee.rushford.mixin;

import com.jam8ee.rushford.item.ModItems;
import com.jam8ee.rushford.network.PoopMeterSyncPayload;
import com.jam8ee.rushford.poop.IPoopMeter;
import com.jam8ee.rushford.sound.ModSounds;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
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
        if (player.getWorld().isClient()) return;

        // 初始化
        if (previousFoodLevel == -1) {
            previousFoodLevel = foodLevel;
            return;
        }

        // 饱食度下降时，增加憋屎度
        if (foodLevel < previousFoodLevel) {
            int decrease = previousFoodLevel - foodLevel;
            IPoopMeter poopMeter = (IPoopMeter) player;
            poopMeter.rushford$addPoopLevel(decrease);

            // 同步到客户端
            syncPoopLevel(player);

            // 如果憋屎度满了，拉屎
            if (poopMeter.rushford$getPoopLevel() >= 20) {
                doPoop(player);
                poopMeter.rushford$setPoopLevel(0);
                syncPoopLevel(player);
            }
        }

        previousFoodLevel = foodLevel;
    }

    @Unique
    private void syncPoopLevel(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            int level = ((IPoopMeter) player).rushford$getPoopLevel();
            ServerPlayNetworking.send(serverPlayer, new PoopMeterSyncPayload(level));
        }
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
        itemEntity.setPickupDelay(40);

        world.spawnEntity(itemEntity);
    }
}