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
                    .nutrition(0)//不恢复饱食度
                    .saturationModifier(0)//不恢复饱和度
                    .alwaysEdible()//随时可吃
                    .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 300, 0), 1.0f)//恶心15秒
                    .build()
            )
    ));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Rushford.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Rushford.LOGGER.info("Registering items for " + Rushford.MOD_ID);

        //添加到材料物品栏
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.add(POOP);
        });

        //堆肥桶 100% 概率
        CompostingChanceRegistry.INSTANCE.add(POOP, 1.0f);
    }
}
