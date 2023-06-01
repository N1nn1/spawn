package com.ninni.spawn.client.renderer.entity.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ninni.spawn.client.model.AntModel;
import com.ninni.spawn.entity.Ant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.resources.ResourceLocation;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(value= EnvType.CLIENT)
public class AbdomenLayer extends RenderLayer<Ant, AntModel<Ant>> {
    private static final ResourceLocation ANT_ABDOMEN_LOCATION = new ResourceLocation(MOD_ID,"textures/entity/ant/ant_abdomen.png");

    public AbdomenLayer(RenderLayerParent<Ant, AntModel<Ant>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Ant ant, float f, float g, float h, float j, float k, float l) {
        if (!ant.isTame() || ant.isInvisible()) return;
        float[] fs = ant.getAbdomenColor().getTextureDiffuseColors();
        WolfCollarLayer.renderColoredCutoutModel(this.getParentModel(), ANT_ABDOMEN_LOCATION, poseStack, multiBufferSource, i, ant, fs[0], fs[1], fs[2]);
    }
}

