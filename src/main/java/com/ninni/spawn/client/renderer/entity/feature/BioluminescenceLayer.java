package com.ninni.spawn.client.renderer.entity.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ninni.spawn.client.model.AnglerFishModel;
import com.ninni.spawn.entity.AnglerFish;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Predicate;

import static com.ninni.spawn.Spawn.MOD_ID;

@OnlyIn(Dist.CLIENT)
public class BioluminescenceLayer<T extends AnglerFish> extends EyesLayer<T, AnglerFishModel<T>> {
    private final Predicate<T> predicate;
    private static final RenderType BIOLUMINESCENCE = RenderType.entityTranslucentEmissive(new ResourceLocation(MOD_ID, "textures/entity/angler_fish/angler_fish_bioluminescence.png"));

    public BioluminescenceLayer(RenderLayerParent<T, AnglerFishModel<T>> renderLayerParent, Predicate<T> predicate) {
        super(renderLayerParent);
        this.predicate = predicate;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T entity, float f, float g, float h, float j, float k, float l) {
        if (!this.predicate.test(entity)) return;
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(this.renderType());
        ((Model)this.getParentModel()).renderToBuffer(poseStack, vertexConsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public RenderType renderType() {
        return BIOLUMINESCENCE;
    }
}

