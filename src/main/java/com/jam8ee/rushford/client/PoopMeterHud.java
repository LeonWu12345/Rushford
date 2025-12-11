package com.jam8ee.rushford.client;

import com.jam8ee.rushford.Rushford;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;

public class PoopMeterHud implements HudRenderCallback {

    private static final Identifier POOP_EMPTY = Identifier.of(Rushford.MOD_ID, "textures/gui/poop_empty.png");
    private static final Identifier POOP_FULL = Identifier.of(Rushford.MOD_ID, "textures/gui/poop_full.png");

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) return;
        if (client.options.hudHidden) return;

        if(client.interactionManager != null) {
            GameMode gameMode = client.interactionManager.getCurrentGameMode();
            if (gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR) {
                return;
            }
        }

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int poopLevel = ClientPoopData.getPoopLevel();
        int maxPoop = 20;

        // 在饥饿条上方显示
        int x = screenWidth / 2 + 10;
        int y = screenHeight - 49 - 2;  // 饥饿条上方

        // 每个图标代表2点憋屎度，共10个图标
        for (int i = 0; i < 10; i++) {
            int iconX = x + (9 - i) * 8;  // 从右向左排列
            int iconY = y;

            // EMPTY
            drawContext.drawTexture(POOP_EMPTY, iconX, iconY, 0, 0, 9, 9, 9, 9);

            // FULL
            int poopForThisIcon = poopLevel - (i * 2);
            if (poopForThisIcon >= 2) {
                // 完全填满
                drawContext.drawTexture(POOP_FULL, iconX, iconY, 0, 0, 9, 9, 9, 9);
            } else if (poopForThisIcon == 1) {
                // 半满
                drawContext.drawTexture(POOP_FULL, iconX, iconY, 0, 0, 5, 9, 9, 9);
            }
        }
    }

    public static void register() {
        HudRenderCallback.EVENT.register(new PoopMeterHud());
    }
}
