package com.jam8ee.rushford.entity;

import com.jam8ee.rushford.entity.ai.PoopMonsterAttackGoal;
import com.jam8ee.rushford.entity.ai.PoopMonsterFleeGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class PoopMonsterEntity extends HostileEntity {

    private int fleeTimer = 0;
    private boolean isFleeing = false;

    public PoopMonsterEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        // 目标选择
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));

        // 行为
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new PoopMonsterFleeGoal(this, PlayerEntity.class, 16.0f, 1.2, 1.5));
        this.goalSelector.add(2, new PoopMonsterAttackGoal(this, 1.0, 40, 15.0f));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(5, new LookAroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder createPoopMonsterAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient()) {
            if (isFleeing && fleeTimer > 0) {
                fleeTimer--;
                if (fleeTimer <= 0) {
                    isFleeing = false;
                }
            }
        }
    }

    // 当投掷的屎球命中目标时调用
    public void onPoopHit(Entity target) {
        if (target instanceof PlayerEntity) {
            this.startFleeing();
        }
    }

    public void startFleeing() {
        this.isFleeing = true;
        this.fleeTimer = 200; // 10秒 = 200 ticks
    }

    public boolean isFleeing() {
        return isFleeing;
    }

    public int getFleeTimer() {
        return fleeTimer;
    }
}