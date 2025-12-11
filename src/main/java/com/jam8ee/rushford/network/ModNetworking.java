package com.jam8ee.rushford.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ModNetworking {

    public static void registerC2SPayloads() {
        // 客户端到服务端的包（如果需要的话）
    }

    public static void registerS2CPayloads() {
        // 服务端到客户端的包
        PayloadTypeRegistry.playS2C().register(PoopMeterSyncPayload.ID, PoopMeterSyncPayload.CODEC);
    }
}