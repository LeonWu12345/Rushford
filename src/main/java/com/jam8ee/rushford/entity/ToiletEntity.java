package com.jam8ee.rushford.entity;

import com.jam8ee.rushford.block.ModBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.entity.EntityDimensions;

public class ToiletEntity extends Entity {

    private BlockPos toiletPos;

    public ToiletEntity(EntityType<?> type, World world) {
        super(type, world);
        this.noClip = true;
    }

    public ToiletEntity(World world, BlockPos pos) {
        this(ModEntities.TOILET_ENTITY, world);
        this.toiletPos = pos;
        this.setPosition(pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5);
    }

    @Override
    public void tick() {
        super.tick();

        // 检查马桶方块是否还存在
        if (toiletPos != null && !getWorld().getBlockState(toiletPos).isOf(ModBlocks.TOILET)) {
            this.discard();
            return;
        }

        // 如果没有乘客，移除实体
        if (!this.hasPassengers()) {
            this.discard();
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        // 乘客下来后移除实体
        if (!this.getWorld().isClient()) {
            this.discard();
        }
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        // 下马时站在马桶旁边
        return new Vec3d(this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5);
    }

    @Override
    protected Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        return new Vec3d(0, 0.2, 0);
    }

    @Override
    public boolean canAddPassenger(Entity passenger) {
        return this.getPassengerList().isEmpty();
    }

    @Override
    protected void initDataTracker(net.minecraft.entity.data.DataTracker.Builder builder) {
        // 不需要额外数据
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("ToiletX")) {
            toiletPos = new BlockPos(nbt.getInt("ToiletX"), nbt.getInt("ToiletY"), nbt.getInt("ToiletZ"));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (toiletPos != null) {
            nbt.putInt("ToiletX", toiletPos.getX());
            nbt.putInt("ToiletY", toiletPos.getY());
            nbt.putInt("ToiletZ", toiletPos.getZ());
        }
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean canHit() {
        return false;
    }

    public BlockPos getToiletPos() {
        return toiletPos;
    }
}
