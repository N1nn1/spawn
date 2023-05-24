package com.ninni.spawn.client.renderer.entity;

import com.ninni.spawn.client.model.SnailModel;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import com.ninni.spawn.entity.Snail;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(value=EnvType.CLIENT)
public class SnailRenderer<T extends LivingEntity> extends MobRenderer<Snail, SnailModel> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/entity/snail/snail.png");

    public SnailRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new SnailModel(ctx.bakeLayer(SpawnEntityModelLayers.SNAIL)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(Snail entity) {
        return  TEXTURE;
    }
}
