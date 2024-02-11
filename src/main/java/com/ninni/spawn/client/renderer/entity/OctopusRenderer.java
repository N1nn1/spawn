package com.ninni.spawn.client.renderer.entity;

import com.ninni.spawn.client.model.OctopusModel;
import com.ninni.spawn.client.model.SeaCowModel;
import com.ninni.spawn.client.renderer.entity.feature.SeagrassLayer;
import com.ninni.spawn.entity.Octopus;
import com.ninni.spawn.entity.SeaCow;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(value=EnvType.CLIENT)
public class OctopusRenderer<T extends LivingEntity> extends MobRenderer<Octopus, OctopusModel> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/entity/octopus/octopus.png");

    public OctopusRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new OctopusModel(ctx.bakeLayer(SpawnEntityModelLayers.OCTOPUS)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(Octopus entity) {
        return  TEXTURE;
    }
}
