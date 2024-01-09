package com.ninni.spawn.client.renderer.entity.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ninni.spawn.client.model.SeaCowModel;
import com.ninni.spawn.entity.SeaCow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(value= EnvType.CLIENT)
public class SeagrassLayer extends RenderLayer<SeaCow, SeaCowModel> {
    private static final ResourceLocation SEAGRASS_LOCATION = new ResourceLocation(MOD_ID,"textures/entity/sea_cow/sea_cow_seagrass.png");

    public SeagrassLayer(RenderLayerParent<SeaCow, SeaCowModel> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, SeaCow entity, float f, float g, float h, float j, float k, float l) {
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityTranslucent(SEAGRASS_LOCATION));

        this.getParentModel().renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, (float) entity.getAlgaeAmount() / entity.maxAlgaeAmount);
    }

}

