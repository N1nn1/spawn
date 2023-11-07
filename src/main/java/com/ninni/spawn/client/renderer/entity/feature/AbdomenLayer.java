package com.ninni.spawn.client.renderer.entity.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ninni.spawn.client.model.AntModel;
import com.ninni.spawn.entity.Ant;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.ninni.spawn.Spawn.MOD_ID;

@OnlyIn(Dist.CLIENT)
public class AbdomenLayer extends RenderLayer<Ant, AntModel<Ant>> {
    private static final ResourceLocation ANT_ABDOMEN_LOCATION = new ResourceLocation(MOD_ID,"textures/entity/ant/ant_abdomen.png");

    public AbdomenLayer(RenderLayerParent<Ant, AntModel<Ant>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Ant ant, float f, float g, float h, float j, float k, float l) {
        float u;
        float t;
        float s;
        if (ant.isInvisible() || !ant.isTame()) return;

        if (ant.hasCustomName() && "jeb_".equals(ant.getName().getString())) {
            int m = 25;
            int n = ant.tickCount / 25 + ant.getId();
            int o = DyeColor.values().length;
            int p = n % o;
            int q = (n + 1) % o;
            float r = ((float)(ant.tickCount % 25) + h) / 25.0f;
            float[] fs = Ant.getColorArray(DyeColor.byId(p));
            float[] gs = Ant.getColorArray(DyeColor.byId(q));
            s = fs[0] * (1.0f - r) + gs[0] * r;
            t = fs[1] * (1.0f - r) + gs[1] * r;
            u = fs[2] * (1.0f - r) + gs[2] * r;
        } else {
            float[] hs = Ant.getColorArray(ant.getAbdomenColor());
            s = hs[0];
            t = hs[1];
            u = hs[2];
        }
        renderColoredCutoutModel(this.getParentModel(), ANT_ABDOMEN_LOCATION, poseStack, multiBufferSource, i, ant, s, t, u);
    }
}

