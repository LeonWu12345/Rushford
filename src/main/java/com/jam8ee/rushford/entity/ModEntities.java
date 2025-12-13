package com.jam8ee.rushford.entity;

import com.jam8ee.rushford.Rushford;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<ToiletEntity> TOILET_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Rushford.MOD_ID, "toilet_seat"),
            EntityType.Builder.<ToiletEntity>create(ToiletEntity::new, SpawnGroup.MISC)
                    .dimensions(0.001f, 0.001f)
                    .build()
    );

    public static final EntityType<ThrownPoopEntity> THROWN_POOP = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Rushford.MOD_ID, "thrown_poop"),
            EntityType.Builder.<ThrownPoopEntity>create(ThrownPoopEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25f, 0.25f)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
                    .build()
    );

    public static final EntityType<PoopMonsterEntity> POOP_MONSTER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Rushford.MOD_ID, "poop_monster"),
            EntityType.Builder.<PoopMonsterEntity>create(PoopMonsterEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6f, 1.95f)
                    .maxTrackingRange(8)
                    .build()
    );

    public static void registerEntities() {
        Rushford.LOGGER.info("Registering entities for " + Rushford.MOD_ID);

        // 注册屎怪的属性
        FabricDefaultAttributeRegistry.register(POOP_MONSTER, PoopMonsterEntity.createPoopMonsterAttributes());
    }
}