package com.ninni.spawn.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ninni.spawn.client.model.OctopusModel;
import com.ninni.spawn.entity.Octopus;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(value=EnvType.CLIENT)
public class OctopusRenderer<T extends LivingEntity> extends MobRenderer<Octopus, OctopusModel> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/entity/octopus/octopus.png");
    private final OctopusModel normalModel, lockingModel;

    public OctopusRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, null, 0.3f);

        this.model = this.normalModel = new OctopusModel(ctx.bakeLayer(SpawnEntityModelLayers.OCTOPUS));
        this.lockingModel = new OctopusModel(ctx.bakeLayer(SpawnEntityModelLayers.OCTOPUS_LOCKING));
    }

    @Override
    public void render(Octopus mob, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        this.model = mob.isLocking() ? this.lockingModel : this.normalModel;
        super.render(mob, f, g, poseStack, multiBufferSource, i);
    }

    @Override
    public ResourceLocation getTextureLocation(Octopus entity) {
        return  TEXTURE;
    }

}
