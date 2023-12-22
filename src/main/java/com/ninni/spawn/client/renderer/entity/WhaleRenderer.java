package com.ninni.spawn.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ninni.spawn.client.model.WhaleModel;
import com.ninni.spawn.entity.Whale;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(value=EnvType.CLIENT)
public class WhaleRenderer<T extends LivingEntity> extends MobRenderer<Whale, WhaleModel> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/entity/whale/whale.png");

    public WhaleRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new WhaleModel(ctx.bakeLayer(SpawnEntityModelLayers.WHALE)), 0.8F);
    }

    @Override
    protected void scale(Whale livingEntity, PoseStack poseStack, float f) {
        super.scale(livingEntity, poseStack, f);
        poseStack.scale(3f, 3f, 3f);
    }

    @Override
    public ResourceLocation getTextureLocation(Whale entity) {
        return  TEXTURE;
    }
}
