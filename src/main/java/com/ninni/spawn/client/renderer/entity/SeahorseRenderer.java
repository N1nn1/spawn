package com.ninni.spawn.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ninni.spawn.client.model.SeahorseModel;
import com.ninni.spawn.client.renderer.entity.feature.SeahorsePatternLayer;
import com.ninni.spawn.entity.common.DeepLurker;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import com.ninni.spawn.entity.Seahorse;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.TropicalFishPatternLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.TropicalFish;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(EnvType.CLIENT)
public class SeahorseRenderer extends MobRenderer<Seahorse, SeahorseModel<Seahorse>> {
    private final SeahorseModel<Seahorse> modelA = this.getModel();
    private final SeahorseModel<Seahorse> modelB;
    private static final ResourceLocation MODEL_A_TEXTURE = new ResourceLocation(MOD_ID,"textures/entity/fish/seahorse_a.png");
    private static final ResourceLocation MODEL_B_TEXTURE = new ResourceLocation(MOD_ID,"textures/entity/fish/seahorse_b.png");

    public SeahorseRenderer(EntityRendererProvider.Context context) {
        super(context, new SeahorseModel<>(context.bakeLayer(SpawnEntityModelLayers.SEAHORSE_SMALL)), 0.2F);
        this.modelB = new SeahorseModel<>(context.bakeLayer(SpawnEntityModelLayers.SEAHORSE_LARGE));
        this.addLayer(new SeahorsePatternLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(Seahorse entity) {
        return switch (entity.getVariant().base()) {
            case SMALL -> MODEL_A_TEXTURE;
            case LARGE -> MODEL_B_TEXTURE;
        };
    }

    @Override
    public void render(Seahorse seahorse, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {

        SeahorseModel<Seahorse> model = switch (seahorse.getVariant().base()) {
            case SMALL -> this.modelA;
            case LARGE -> this.modelB;
        };

        this.model = model;

        //if (model == this.modelB) {
        //    poseStack.scale(1.25f, 1.25f, 1.25f);
        //} else poseStack.scale(0.75f, 0.75f, 0.75f);

        float[] fs = seahorse.getBaseColor().getTextureDiffuseColors();
        model.setColor(fs[0], fs[1], fs[2]);

        super.render(seahorse, f, g, poseStack, multiBufferSource, i);

        model.setColor(1.0F, 1.0F, 1.0F);
    }

}

