package com.jam8ee.rushford.effect;

import com.jam8ee.rushford.item.ModItems;
import com.jam8ee.rushford.network.PoopMeterSyncPayload;
import com.jam8ee.rushford.poop.IPoopMeter;
import com.jam8ee.rushford.sound.ModSounds;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;

public class DiarrheaEffect extends StatusEffect {

    public DiarrheaEffect() {
        super(StatusEffectCategory.HARMFUL, 0x8B4513); // 棕色
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof ServerPlayerEntity player) {
            IPoopMeter poopMeter = (IPoopMeter) player;
            poopMeter.rushford$addPoopLevel(1);

            // 检测是否需要拉屎
            if (poopMeter.rushford$getPoopLevel() >= 20) {
                doPoop(player);
                poopMeter.rushford$setPoopLevel(0);
            }

            // 同步到客户端
            int level = poopMeter.rushford$getPoopLevel();
            ServerPlayNetworking.send(player, new PoopMeterSyncPayload(level));
        }
        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        // 每3秒触发一次 (60 ticks)
        return duration % 60 == 0;
    }

    private void doPoop(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();

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

        ItemStack poopStack = new ItemStack(ModItems.POOP);
        ItemEntity itemEntity = new ItemEntity(world, dropX, dropY, dropZ, poopStack);
        itemEntity.setVelocity(-lookVec.x * 0.1, 0.2, -lookVec.z * 0.1);
        itemEntity.setPickupDelay(40);

        world.spawnEntity(itemEntity);
    }
}