package com.ninni.spawn.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ninni.spawn.client.model.AnglerFishModel;
import com.ninni.spawn.client.model.SpawnEntityModelLayers;
import com.ninni.spawn.client.renderer.entity.feature.BioluminescenceLayer;
import com.ninni.spawn.entity.AnglerFish;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Predicate;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(EnvType.CLIENT)
public class AnglerFishRenderer<E extends AnglerFish> extends MobRenderer<E, AnglerFishModel<E>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/entity/angler_fish/angler_fish.png");
    public static final ResourceLocation TEXTURE_DEFLATED = new ResourceLocation(MOD_ID, "textures/entity/angler_fish/angler_fish_deflated.png");
    private final AnglerFishModel<E> normalModel, deflatedModel;

    public AnglerFishRenderer(EntityRendererProvider.Context context) {
        super(context, null, 0.3F);
        this.addLayer(new BioluminescenceLayer<>(this, Predicate.not(E::isDeflated)));

        this.model = this.normalModel = createModel(context, SpawnEntityModelLayers.ANGLER_FISH);
        this.deflatedModel = createModel(context, SpawnEntityModelLayers.ANGLER_FISH_DEFLATED);
    }

    @Override
    public void render(E mob, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        this.model = mob.isDeflated() ? this.deflatedModel : this.normalModel;
        super.render(mob, f, g, poseStack, multiBufferSource, i);
    }

    @Override
    public ResourceLocation getTextureLocation(E entity) {
        return entity.isDeflated() ? TEXTURE_DEFLATED : TEXTURE;
    }

    public AnglerFishModel<E> createModel(EntityRendererProvider.Context context, ModelLayerLocation layer) {
        return new AnglerFishModel<>(context.bakeLayer(layer));
    }
}
