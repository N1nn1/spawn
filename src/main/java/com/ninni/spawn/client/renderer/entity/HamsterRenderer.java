package com.ninni.spawn.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ninni.spawn.client.model.HamsterModel;
import com.ninni.spawn.entity.Hamster;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(value=EnvType.CLIENT)
public class HamsterRenderer<E extends Hamster> extends MobRenderer<E, HamsterModel<E>> {
    public static final ResourceLocation HAMSTER_GOLDEN_LOCATION = new ResourceLocation(MOD_ID, "textures/entity/hamster/golden.png");
    public static final ResourceLocation HAMSTER_ROBOROWSKI_LOCATION = new ResourceLocation(MOD_ID, "textures/entity/hamster/roborowski.png");
    public static final ResourceLocation HAMSTER_RUSSIAN_LOCATION = new ResourceLocation(MOD_ID, "textures/entity/hamster/russian.png");
    public static final ResourceLocation HAMSTER_TURKISH_LOCATION = new ResourceLocation(MOD_ID, "textures/entity/hamster/turkish.png");
    private final HamsterModel<E> normalModel, standingModel;

    public HamsterRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, null, 0.4F);

        this.model = this.normalModel = createModel(ctx, SpawnEntityModelLayers.HAMSTER);
        this.standingModel = createModel(ctx, SpawnEntityModelLayers.HAMSTER_STANDING);
    }

    @Override
    public void render(E mob, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        this.model = mob.isStanding() ? this.standingModel : this.normalModel;
        super.render(mob, f, g, poseStack, multiBufferSource, i);
    }

    @Override
    public ResourceLocation getTextureLocation(Hamster entity) {
        return switch (entity.getVariant()) {
            case GOLDEN -> HAMSTER_GOLDEN_LOCATION;
            case ROBOROWSKI -> HAMSTER_ROBOROWSKI_LOCATION;
            case RUSSIAN -> HAMSTER_RUSSIAN_LOCATION;
            case TURKISH -> HAMSTER_TURKISH_LOCATION;
        };
    }

    public HamsterModel<E> createModel(EntityRendererProvider.Context context, ModelLayerLocation layer) {
        return new HamsterModel<>(context.bakeLayer(layer));
    }
}
