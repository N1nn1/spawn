package com.ninni.spawn.client.renderer.entity;

import com.ninni.spawn.client.model.SeahorseModel;
import com.ninni.spawn.client.model.SpawnEntityModelLayers;
import com.ninni.spawn.entity.Seahorse;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(EnvType.CLIENT)
public class SeahorseRenderer extends MobRenderer<Seahorse, SeahorseModel<Seahorse>> {
    private static final ResourceLocation SEAHORSE_BLACK_LOCATION = new ResourceLocation(MOD_ID, "textures/entity/seahorse/black.png");
    private static final ResourceLocation SEAHORSE_BLUE_LOCATION = new ResourceLocation(MOD_ID, "textures/entity/seahorse/blue.png");
    private static final ResourceLocation SEAHORSE_ORANGE_LOCATION = new ResourceLocation(MOD_ID, "textures/entity/seahorse/orange.png");
    private static final ResourceLocation SEAHORSE_PURPLE_LOCATION = new ResourceLocation(MOD_ID, "textures/entity/seahorse/purple.png");
    private static final ResourceLocation SEAHORSE_WHITE_LOCATION = new ResourceLocation(MOD_ID, "textures/entity/seahorse/white.png");
    private static final ResourceLocation SEAHORSE_YELLOW_LOCATION = new ResourceLocation(MOD_ID, "textures/entity/seahorse/yellow.png");

    public SeahorseRenderer(EntityRendererProvider.Context context) {
        super(context, new SeahorseModel<>(context.bakeLayer(SpawnEntityModelLayers.SEAHORSE)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(Seahorse entity) {
        return switch (entity.getVariant()) {
            case BLACK -> SEAHORSE_BLACK_LOCATION;
            case BLUE -> SEAHORSE_BLUE_LOCATION;
            case ORANGE -> SEAHORSE_ORANGE_LOCATION;
            case PURPLE -> SEAHORSE_PURPLE_LOCATION;
            case WHITE -> SEAHORSE_WHITE_LOCATION;
            case YELLOW -> SEAHORSE_YELLOW_LOCATION;
        };
    }
}

