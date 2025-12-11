package com.jam8ee.rushford.mixin;

import com.jam8ee.rushford.poop.IPoopMeter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerPoopMeterMixin implements IPoopMeter {

    @Unique
    private int rushford$poopLevel = 0;

    @Unique
    private static final int MAX_POOP_LEVEL = 20;

    @Override
    public int rushford$getPoopLevel() {
        return rushford$poopLevel;
    }

    @Override
    public void rushford$setPoopLevel(int level) {
        this.rushford$poopLevel = Math.max(0, Math.min(MAX_POOP_LEVEL, level));
    }

    @Override
    public void rushford$addPoopLevel(int amount) {
        rushford$setPoopLevel(this.rushford$poopLevel + amount);
    }

    // 保存数据
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void onWriteNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("rushford_poop_level", rushford$poopLevel);
    }

    // 读取数据
    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void onReadNbt(NbtCompound nbt, CallbackInfo ci) {
        rushford$poopLevel = nbt.getInt("rushford_poop_level");
    }
}
