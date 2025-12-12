package com.jam8ee.rushford.client;

import com.jam8ee.rushford.Rushford;
import com.jam8ee.rushford.entity.PoopMonsterEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class PoopMonsterRenderer extends MobEntityRenderer<PoopMonsterEntity, BipedEntityModel<PoopMonsterEntity>> {

    private static final Identifier TEXTURE = Identifier.of(Rushford.MOD_ID, "textures/entity/poop_monster.png");

    public PoopMonsterRenderer(EntityRendererFactory.Context context) {
        super(context, new BipedEntityModel<>(context.getPart(EntityModelLayers.ZOMBIE)), 0.5f);
    }

    @Override
    public Identifier getTexture(PoopMonsterEntity entity) {
        return TEXTURE;
    }
}