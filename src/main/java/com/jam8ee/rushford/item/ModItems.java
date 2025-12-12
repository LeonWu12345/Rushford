package com.jam8ee.rushford.item;

import com.jam8ee.rushford.Rushford;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item POOP = registerItem("poop", new Item(new Item.Settings()
            .food(new FoodComponent.Builder()
                    .nutrition(0)
                    .saturationModifier(0)
                    .alwaysEdible()
                    .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0), 1.0f)
                    .build()
            )
    ));

    public static final Item POOP_BALL = registerItem("poop_ball", new PoopBallItem(new Item.Settings()
            .maxCount(16)
    ));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Rushford.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Rushford.LOGGER.info("Registering items for " + Rushford.MOD_ID);

        // 添加到材料物品栏
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.add(POOP);
            content.add(POOP_BALL);
        });

        // 堆肥桶 100% 概率
        CompostingChanceRegistry.INSTANCE.add(POOP, 1.0f);
    }
}