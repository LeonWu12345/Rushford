package com.jam8ee.rushford.entity.ai;

import com.jam8ee.rushford.entity.PoopMonsterEntity;
import com.jam8ee.rushford.entity.ThrownPoopEntity;
import com.jam8ee.rushford.item.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;

import java.util.EnumSet;

public class PoopMonsterAttackGoal extends Goal {

    private final PoopMonsterEntity mob;
    private final double speed;
    private final int attackInterval;
    private final float attackRange;
    private int attackCooldown;
    private int seeTime;

    public PoopMonsterAttackGoal(PoopMonsterEntity mob, double speed, int attackInterval, float attackRange) {
        this.mob = mob;
        this.speed = speed;
        this.attackInterval = attackInterval;
        this.attackRange = attackRange;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        LivingEntity target = this.mob.getTarget();
        return target != null && target.isAlive() && !this.mob.isFleeing();
    }

    @Override
    public boolean shouldContinue() {
        return this.canStart();
    }

    @Override
    public void stop() {
        this.seeTime = 0;
        this.attackCooldown = 0;
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) return;

        double distance = this.mob.squaredDistanceTo(target);
        boolean canSee = this.mob.canSee(target);

        if (canSee) {
            this.seeTime++;
        } else {
            this.seeTime = 0;
        }

        // 如果在攻击范围内且能看到目标，停下来攻击
        if (distance <= (double)(this.attackRange * this.attackRange) && this.seeTime >= 5) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().startMovingTo(target, this.speed);
        }

        this.mob.getLookControl().lookAt(target, 30.0f, 30.0f);

        if (this.attackCooldown > 0) {
            this.attackCooldown--;
        }

        // 攻击
        if (this.attackCooldown <= 0 && distance <= (double)(this.attackRange * this.attackRange) && canSee) {
            this.throwPoop(target);
            this.attackCooldown = this.attackInterval;
        }
    }

    private void throwPoop(LivingEntity target) {
        ThrownPoopEntity poop = new ThrownPoopEntity(this.mob.getWorld(), this.mob);
        poop.setItem(new ItemStack(ModItems.POOP_BALL));

        double dx = target.getX() - this.mob.getX();
        double dy = target.getBodyY(0.3333333333333333) - poop.getY();
        double dz = target.getZ() - this.mob.getZ();
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

        poop.setVelocity(dx, dy + horizontalDistance * 0.2, dz, 1.2f, 8.0f);

        this.mob.playSound(SoundEvents.ENTITY_SNOWBALL_THROW, 1.0f, 1.0f / (this.mob.getRandom().nextFloat() * 0.4f + 0.8f));
        this.mob.getWorld().spawnEntity(poop);
    }
}