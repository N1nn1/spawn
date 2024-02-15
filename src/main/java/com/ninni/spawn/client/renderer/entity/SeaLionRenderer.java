package com.ninni.spawn.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ninni.spawn.client.model.OctopusModel;
import com.ninni.spawn.client.model.SeaLionModel;
import com.ninni.spawn.entity.Octopus;
import com.ninni.spawn.entity.SeaLion;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(value=EnvType.CLIENT)
public class SeaLionRenderer extends MobRenderer<SeaLion, SeaLionModel> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/entity/sea_lion/sea_lion.png");
    private final SeaLionModel normalModel, swimmingModel;

    public SeaLionRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, null, 0.6f);

        this.model = this.normalModel = new SeaLionModel(ctx.bakeLayer(SpawnEntityModelLayers.SEA_LION));
        this.swimmingModel = new SeaLionModel(ctx.bakeLayer(SpawnEntityModelLayers.SEA_LION_SWIMMING));
    }

    @Override
    public void render(SeaLion mob, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {

        this.model = (mob.isInWaterOrBubble() || mob.isSlacking()) ? this.swimmingModel : this.normalModel;

        super.render(mob, f, g, poseStack, multiBufferSource, i);
    }

    @Override
    public ResourceLocation getTextureLocation(SeaLion entity) {
        return  TEXTURE;
    }

}
