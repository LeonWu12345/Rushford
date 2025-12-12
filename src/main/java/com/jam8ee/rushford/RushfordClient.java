package com.jam8ee.rushford;

import com.jam8ee.rushford.block.entity.ModScreenHandlers;
import com.jam8ee.rushford.client.ClientPoopData;
import com.jam8ee.rushford.client.PoopMeterHud;
import com.jam8ee.rushford.client.ToiletScreen;
import com.jam8ee.rushford.entity.ModEntities;
import com.jam8ee.rushford.network.PoopMeterSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.entity.EmptyEntityRenderer;

public class RushfordClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // 注册HUD
        PoopMeterHud.register();

        // 注册网络包接收
        ClientPlayNetworking.registerGlobalReceiver(PoopMeterSyncPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                ClientPoopData.setPoopLevel(payload.poopLevel());
            });
        });

        HandledScreens.register(ModScreenHandlers.TOILET_SCREEN_HANDLER, ToiletScreen::new);

        EntityRendererRegistry.register(ModEntities.TOILET_ENTITY, EmptyEntityRenderer::new);

        Rushford.LOGGER.info("Rushford client initialized!");
    }
}
