package com.ninni.spawn.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ninni.spawn.client.model.ClamModel;
import com.ninni.spawn.client.renderer.entity.feature.ClamPatternLayer;
import com.ninni.spawn.entity.Clam;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(EnvType.CLIENT)
public class ClamRenderer extends MobRenderer<Clam, ClamModel> {
    private final ClamModel modelWedgeShell = this.getModel();
    private final ClamModel modelScallop;
    private final ClamModel modelGiantClam;

    public ClamRenderer(EntityRendererProvider.Context context) {
        super(context, new ClamModel(context.bakeLayer(SpawnEntityModelLayers.CLAM_WEDGE_SHELL)), 0.2F);
        this.modelScallop = new ClamModel(context.bakeLayer(SpawnEntityModelLayers.CLAM_SCALLOP));
        this.modelGiantClam = new ClamModel(context.bakeLayer(SpawnEntityModelLayers.CLAM_GIANT_CLAM));
        this.addLayer(new ClamPatternLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(Clam entity) {
        return new ResourceLocation(MOD_ID,"textures/entity/clam/" + entity.getBaseColor().baseName() + "_" + entity.getBaseColor().colorName() + ".png");
    }

    @Override
    public void render(Clam clam, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        this.model = switch (clam.getVariant().baseColor().base()) {
            case WEDGE_SHELL -> this.modelWedgeShell;
            case SCALLOP -> this.modelScallop;
            case GIANT_CLAM -> this.modelGiantClam;
        };
        super.render(clam, f, g, poseStack, multiBufferSource, i);
    }

}

