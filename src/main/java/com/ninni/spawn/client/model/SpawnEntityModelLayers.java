package com.ninni.spawn.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(EnvType.CLIENT)
public interface SpawnEntityModelLayers {

    ModelLayerLocation ANGLER_FISH = main("angler_fish", AnglerFishModel::getLayerDefinition);
    ModelLayerLocation ANGLER_FISH_DEFLATED = main("angler_fish_deflated", AnglerFishModel::getDeflatedLayerDefinition);
    ModelLayerLocation SEAHORSE = main("seahorse", SeahorseModel::getLayerDefinition);
    ModelLayerLocation SNAIL = main("snail", SnailModel::getLayerDefinition);

    private static ModelLayerLocation register(String id, String name, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
        ModelLayerLocation layer = new ModelLayerLocation(new ResourceLocation(MOD_ID, id), name);
        EntityModelLayerRegistry.registerModelLayer(layer, provider);
        return layer;
    }

    private static ModelLayerLocation main(String id, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
        return register(id, "main", provider);
    }
}
