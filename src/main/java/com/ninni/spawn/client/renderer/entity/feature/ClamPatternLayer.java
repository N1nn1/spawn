package com.ninni.spawn.client.renderer.entity.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ninni.spawn.client.model.ClamModel;
import com.ninni.spawn.entity.Clam;
import com.ninni.spawn.entity.variant.ClamVariant;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(value= EnvType.CLIENT)
public class ClamPatternLayer extends RenderLayer<Clam, ClamModel> {
    private final ClamModel modelWedgeShell;
    private final ClamModel modelScallop;
    private final ClamModel modelGiantClam;

    public ClamPatternLayer(RenderLayerParent<Clam, ClamModel> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.modelWedgeShell = new ClamModel(entityModelSet.bakeLayer(SpawnEntityModelLayers.CLAM_WEDGE_SHELL));
        this.modelScallop = new ClamModel(entityModelSet.bakeLayer(SpawnEntityModelLayers.CLAM_SCALLOP));
        this.modelGiantClam = new ClamModel(entityModelSet.bakeLayer(SpawnEntityModelLayers.CLAM_GIANT_CLAM));
    }

    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Clam clam, float f, float g, float h, float j, float k, float l) {

        if (clam.getPattern() != ClamVariant.Pattern.NO_PATTERN) {

            ClamModel entityModel = switch (clam.getVariant().baseColor().base()) {
                case WEDGE_SHELL -> this.modelWedgeShell;
                case SCALLOP -> this.modelScallop;
                case GIANT_CLAM -> this.modelGiantClam;
            };

            ResourceLocation resourceLocation = new ResourceLocation(MOD_ID, "textures/entity/clam/variant/" + clam.getBaseColor().baseName() + "_" + clam.getPattern().patternName() + ".png");

            float[] fs = clam.getDyeColor().getTextureDiffuseColors();
            coloredCutoutModelCopyLayerRender(this.getParentModel(), entityModel, resourceLocation, poseStack, multiBufferSource, i, clam, f, g, j, k, l, h, fs[0], fs[1], fs[2]);
        }
    }
}
