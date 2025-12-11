package com.jam8ee.rushford.event;

import com.jam8ee.rushford.network.PoopMeterSyncPayload;
import com.jam8ee.rushford.poop.IPoopMeter;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ModEvents {

    public static void registerEvents() {
        // 玩家重生时重置憋屎度
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            if (!alive) {
                // 死亡重生，重置憋屎度
                ((IPoopMeter) newPlayer).rushford$setPoopLevel(0);
            } else {
                // 从末地返回等，保留憋屎度
                int oldLevel = ((IPoopMeter) oldPlayer).rushford$getPoopLevel();
                ((IPoopMeter) newPlayer).rushford$setPoopLevel(oldLevel);
            }
            // 同步到客户端
            int level = ((IPoopMeter) newPlayer).rushford$getPoopLevel();
            ServerPlayNetworking.send(newPlayer, new PoopMeterSyncPayload(level));
        });
    }
}
