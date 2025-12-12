package com.jam8ee.rushford;

import com.jam8ee.rushford.block.entity.ModScreenHandlers;
import com.jam8ee.rushford.client.ClientPoopData;
import com.jam8ee.rushford.client.PoopMeterHud;
import com.jam8ee.rushford.client.PoopMonsterRenderer;
import com.jam8ee.rushford.client.ToiletScreen;
import com.jam8ee.rushford.entity.ModEntities;
import com.jam8ee.rushford.network.PoopMeterSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class RushfordClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // 注册网络包接收处理器
        ClientPlayNetworking.registerGlobalReceiver(PoopMeterSyncPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                ClientPoopData.setPoopLevel(payload.poopLevel());
            });
        });

        // 注册HUD
        PoopMeterHud.register();

        // 注册马桶界面
        HandledScreens.register(ModScreenHandlers.TOILET_SCREEN_HANDLER, ToiletScreen::new);

        // 注册马桶座位实体渲染器（不可见的实体）
        EntityRendererRegistry.register(ModEntities.TOILET_ENTITY, EmptyEntityRenderer::new);

        // 注册投掷屎实体渲染器（显示为飞行的物品）
        EntityRendererRegistry.register(ModEntities.THROWN_POOP, FlyingItemEntityRenderer::new);

        // 注册屎怪渲染器
        EntityRendererRegistry.register(ModEntities.POOP_MONSTER, PoopMonsterRenderer::new);

        Rushford.LOGGER.info("Rushford client initialized!");
    }
}