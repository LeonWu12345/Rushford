package com.jam8ee.rushford.entity.ai;

import com.jam8ee.rushford.entity.PoopMonsterEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class PoopMonsterFleeGoal extends Goal {

    private final PoopMonsterEntity mob;
    private final Class<? extends LivingEntity> fleeFromType;
    private final float distance;
    private final double slowSpeed;
    private final double fastSpeed;
    private LivingEntity targetEntity;
    private Vec3d fleePath;

    public PoopMonsterFleeGoal(PoopMonsterEntity mob, Class<? extends LivingEntity> fleeFromType, float distance, double slowSpeed, double fastSpeed) {
        this.mob = mob;
        this.fleeFromType = fleeFromType;
        this.distance = distance;
        this.slowSpeed = slowSpeed;
        this.fastSpeed = fastSpeed;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!this.mob.isFleeing()) {
            return false;
        }

        this.targetEntity = this.mob.getWorld().getClosestEntity(
                this.mob.getWorld().getEntitiesByClass(this.fleeFromType, this.mob.getBoundingBox().expand(this.distance, 3.0, this.distance), e -> true),
                net.minecraft.entity.ai.TargetPredicate.DEFAULT,
                this.mob,
                this.mob.getX(),
                this.mob.getY(),
                this.mob.getZ()
        );

        if (this.targetEntity == null) {
            return false;
        }

        Vec3d awayVec = NoPenaltyTargeting.findFrom(this.mob, 16, 7, this.targetEntity.getPos());
        if (awayVec == null) {
            return false;
        }

        if (this.targetEntity.squaredDistanceTo(awayVec.x, awayVec.y, awayVec.z) < this.targetEntity.squaredDistanceTo(this.mob)) {
            return false;
        }

        this.fleePath = awayVec;
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return this.mob.isFleeing() && !this.mob.getNavigation().isIdle();
    }

    @Override
    public void start() {
        this.mob.getNavigation().startMovingTo(this.fleePath.x, this.fleePath.y, this.fleePath.z, this.fastSpeed);
    }

    @Override
    public void stop() {
        this.targetEntity = null;
    }

    @Override
    public void tick() {
        this.mob.getNavigation().setSpeed(this.fastSpeed);
    }
}