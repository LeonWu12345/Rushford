package com.jam8ee.rushford;

import com.jam8ee.rushford.client.ClientPoopData;
import com.jam8ee.rushford.client.PoopMeterHud;
import com.jam8ee.rushford.network.PoopMeterSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

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

        Rushford.LOGGER.info("Rushford client initialized!");
    }
}
