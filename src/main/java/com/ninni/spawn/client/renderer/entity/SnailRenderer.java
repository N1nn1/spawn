package com.ninni.spawn.client.renderer.entity;

import com.ninni.spawn.client.model.SnailModel;
import com.ninni.spawn.entity.Snail;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.ninni.spawn.Spawn.MOD_ID;

@OnlyIn(Dist.CLIENT)
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
