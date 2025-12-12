package com.jam8ee.rushford.entity;

import com.jam8ee.rushford.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class ThrownPoopEntity extends ThrownItemEntity {

    public ThrownPoopEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public ThrownPoopEntity(World world, LivingEntity owner) {
        super(ModEntities.THROWN_POOP, owner, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.POOP_BALL;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        Entity hitEntity = entityHitResult.getEntity();

        if (hitEntity instanceof LivingEntity target) {
            // 造成 1 点伤害
            target.damage(this.getDamageSources().thrown(this, this.getOwner()), 1.0f);

            // 给予恶心效果 5 秒 (100 ticks)
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));

            // 给予失明效果 4 秒 (80 ticks)
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 80, 0));

            // 通知屎怪命中了
            Entity owner = this.getOwner();
            if (owner instanceof PoopMonsterEntity poopMonster) {
                poopMonster.onPoopHit(target);
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.getWorld().isClient()) {
            // 生成粒子效果
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(
                        new ItemStackParticleEffect(ParticleTypes.ITEM, this.getStack()),
                        this.getX(), this.getY(), this.getZ(),
                        8, 0.1, 0.1, 0.1, 0.05
                );
            }
            this.discard();
        }
    }
}