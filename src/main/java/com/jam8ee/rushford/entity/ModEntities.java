package com.jam8ee.rushford.entity;

import com.jam8ee.rushford.Rushford;
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
                    .dimensions(0.001f, 0.001f) // 极小的碰撞箱
                    .build()
    );

    public static void registerEntities() {
        Rushford.LOGGER.info("Registering entities for " + Rushford.MOD_ID);
    }
}