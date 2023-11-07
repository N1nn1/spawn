package com.ninni.spawn.registry;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

import static com.ninni.spawn.Spawn.MOD_ID;

public class SpawnEntityModelLayers {

    public static final ModelLayerLocation ANGLER_FISH = main("angler_fish");
    public static final ModelLayerLocation ANGLER_FISH_DEFLATED = main("angler_fish_deflated");
    public static final ModelLayerLocation TUNA = main("tuna");
    public static final ModelLayerLocation TUNA_EGG = main("tuna_egg");
    public static final ModelLayerLocation SEAHORSE = main("seahorse");
    public static final ModelLayerLocation SNAIL = main("snail");
    public static final ModelLayerLocation HAMSTER = main("hamster");
    public static final ModelLayerLocation HAMSTER_STANDING = main("hamster_standing");
    public static final ModelLayerLocation ANT = main("ant");

    private static ModelLayerLocation register(String id, String name) {
        return new ModelLayerLocation(new ResourceLocation(MOD_ID, id), name);
    }

    private static ModelLayerLocation main(String id) {
        return register(id, "main");
    }
}
