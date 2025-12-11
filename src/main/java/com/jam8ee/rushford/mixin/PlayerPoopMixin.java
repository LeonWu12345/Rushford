package com.jam8ee.rushford.mixin;

import com.jam8ee.rushford.poop.IPoopMeter;
import com.jam8ee.rushford.poop.PoopHelper;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
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
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;

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
            PoopHelper.syncPoopLevel(serverPlayer, poopMeter.rushford$getPoopLevel());

            // 如果憋屎度满了，拉屎
            if (poopMeter.rushford$getPoopLevel() >= 20) {
                PoopHelper.doPoop(serverPlayer);
                poopMeter.rushford$setPoopLevel(0);
                PoopHelper.syncPoopLevel(serverPlayer, 0);
            }
        }

        previousFoodLevel = foodLevel;
    }
}