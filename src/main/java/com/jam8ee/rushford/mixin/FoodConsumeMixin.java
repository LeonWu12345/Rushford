package com.jam8ee.rushford.mixin;

import com.jam8ee.rushford.effect.ModEffects;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class FoodConsumeMixin {

    @Inject(method = "eatFood", at = @At("HEAD"))
    private void onEatFood(World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!(entity instanceof PlayerEntity player)) return;
        if (world.isClient()) return;

        Item item = stack.getItem();
        int durationTicks = getDiarrheaDuration(item);

        if (durationTicks > 0) {
            player.addStatusEffect(new StatusEffectInstance(
                    ModEffects.DIARRHEA,
                    durationTicks,
                    0,      // amplifier
                    false,  // ambient
                    true,   // showParticles
                    true    // showIcon
            ));
        }
    }

    private int getDiarrheaDuration(Item item) {
        // 返回 ticks (20 ticks = 1秒)
        if (item == Items.ROTTEN_FLESH) {
            return 90 * 20;   // 腐肉 90秒
        } else if (item == Items.POISONOUS_POTATO) {
            return 100 * 20;  // 毒马铃薯 100秒
        } else if (item == Items.CHICKEN) {
            return 120 * 20;  // 生鸡肉 120秒
        } else if (item == Items.PUFFERFISH) {
            return 180 * 20;  // 河豚 180秒
        }
        return 0;
    }
}