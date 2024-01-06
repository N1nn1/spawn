package com.ninni.spawn.client.renderer.entity.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ninni.spawn.client.model.SeahorseModel;
import com.ninni.spawn.entity.Seahorse;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

import static com.ninni.spawn.Spawn.MOD_ID;

public class SeahorsePatternLayer extends RenderLayer<Seahorse, SeahorseModel<Seahorse>> {
    private static final ResourceLocation RIDGED = new ResourceLocation(MOD_ID, "textures/entity/fish/seahorse_a_pattern_1.png");
    private static final ResourceLocation SHARP = new ResourceLocation(MOD_ID, "textures/entity/fish/seahorse_a_pattern_2.png");
    private static final ResourceLocation HOODED = new ResourceLocation(MOD_ID, "textures/entity/fish/seahorse_a_pattern_3.png");
    private static final ResourceLocation SPOTTED = new ResourceLocation(MOD_ID, "textures/entity/fish/seahorse_a_pattern_4.png");
    private static final ResourceLocation BANDED = new ResourceLocation(MOD_ID, "textures/entity/fish/seahorse_b_pattern_1.png");
    private static final ResourceLocation WAVY = new ResourceLocation(MOD_ID, "textures/entity/fish/seahorse_b_pattern_2.png");
    private static final ResourceLocation RIBBED = new ResourceLocation(MOD_ID, "textures/entity/fish/seahorse_b_pattern_3.png");
    private static final ResourceLocation CURLED = new ResourceLocation(MOD_ID, "textures/entity/fish/seahorse_b_pattern_4.png");
    private final SeahorseModel<Seahorse> modelA;
    private final SeahorseModel<Seahorse> modelB;

    public SeahorsePatternLayer(RenderLayerParent<Seahorse, SeahorseModel<Seahorse>> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.modelA = new SeahorseModel<>(entityModelSet.bakeLayer(SpawnEntityModelLayers.SEAHORSE_SMALL));
        this.modelB = new SeahorseModel<>(entityModelSet.bakeLayer(SpawnEntityModelLayers.SEAHORSE_LARGE));
    }

    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Seahorse Seahorse, float f, float g, float h, float j, float k, float l) {
        Seahorse.Pattern pattern = Seahorse.getVariant();
        SeahorseModel<Seahorse> entityModel = switch (pattern.base()) {
            case SMALL -> this.modelA;
            case LARGE -> this.modelB;
        };

        ResourceLocation resourceLocation = switch (pattern) {
            case RIDGED -> RIDGED;
            case SHARP -> SHARP;
            case HOODED -> HOODED;
            case SPOTTED -> SPOTTED;
            case BANDED -> BANDED;
            case WAVY -> WAVY;
            case RIBBED -> RIBBED;
            case CURLED -> CURLED;
        };

        float[] fs = Seahorse.getPatternColor().getTextureDiffuseColors();
        coloredCutoutModelCopyLayerRender(this.getParentModel(), entityModel, resourceLocation, poseStack, multiBufferSource, i, Seahorse, f, g, j, k, l, h, fs[0], fs[1], fs[2]);
    }
}
