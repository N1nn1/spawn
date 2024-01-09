package com.ninni.spawn.client.renderer.entity;

import com.ninni.spawn.client.model.SeaCowModel;
import com.ninni.spawn.client.renderer.entity.feature.BioluminescenceLayer;
import com.ninni.spawn.client.renderer.entity.feature.SeagrassLayer;
import com.ninni.spawn.entity.SeaCow;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(value=EnvType.CLIENT)
public class SeaCowRenderer<T extends LivingEntity> extends MobRenderer<SeaCow, SeaCowModel> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/entity/sea_cow/sea_cow.png");

    public SeaCowRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new SeaCowModel(ctx.bakeLayer(SpawnEntityModelLayers.SEA_COW)), 1);
        this.addLayer(new SeagrassLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(SeaCow entity) {
        return  TEXTURE;
    }
}
