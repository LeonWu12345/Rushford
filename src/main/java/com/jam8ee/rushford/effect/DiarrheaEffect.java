package com.jam8ee.rushford.effect;

import com.jam8ee.rushford.poop.IPoopMeter;
import com.jam8ee.rushford.poop.PoopHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;

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
                PoopHelper.doPoop(player);
                poopMeter.rushford$setPoopLevel(0);
            }

            // 同步到客户端
            PoopHelper.syncPoopLevel(player, poopMeter.rushford$getPoopLevel());
        }
        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        // 每3秒触发一次 (60 ticks)
        return duration % 60 == 0;
    }
}