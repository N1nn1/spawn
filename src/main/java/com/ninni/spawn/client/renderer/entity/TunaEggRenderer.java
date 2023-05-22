package com.ninni.spawn.client.renderer.entity;

import com.ninni.spawn.client.model.SpawnEntityModelLayers;
import com.ninni.spawn.client.model.TunaEggModel;
import com.ninni.spawn.entity.TunaEgg;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(value=EnvType.CLIENT)
public class TunaEggRenderer<T extends LivingEntity> extends MobRenderer<TunaEgg, TunaEggModel> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/entity/tuna/tuna_egg.png");

    public TunaEggRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new TunaEggModel(ctx.bakeLayer(SpawnEntityModelLayers.TUNA_EGG)), 0.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(TunaEgg entity) {
        return  TEXTURE;
    }
}
