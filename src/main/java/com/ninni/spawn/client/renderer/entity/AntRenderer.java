package com.ninni.spawn.client.renderer.entity;

import com.ninni.spawn.client.model.AntModel;
import com.ninni.spawn.client.renderer.entity.feature.AbdomenLayer;
import com.ninni.spawn.entity.Ant;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(value=EnvType.CLIENT)
public class AntRenderer extends MobRenderer<Ant, AntModel<Ant>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/entity/ant/ant.png");

    public AntRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new AntModel<>(ctx.bakeLayer(SpawnEntityModelLayers.ANT)), 0.3F);
        this.addLayer(new AbdomenLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Ant entity) {
        return  TEXTURE;
    }
}
