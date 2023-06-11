package com.ninni.spawn.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

import static com.ninni.spawn.Spawn.MOD_ID;

public class SpawnDecoratedPotPatterns {
    public static final ResourceKey<String> CROWN = register("crown_pottery_pattern");
    @Unique public static final Map<Item, ResourceKey<String>> S$ITEM_TO_POT_TEXTURE = new HashMap<>();

    static {
        S$ITEM_TO_POT_TEXTURE.put(SpawnItems.CROWN_POTTERY_SHERD, CROWN);
        Registry.register(BuiltInRegistries.DECORATED_POT_PATTERNS, CROWN, "crown_pottery_pattern");
    }

    public static ResourceKey<String> register(String string) {
        return ResourceKey.create(Registries.DECORATED_POT_PATTERNS, new ResourceLocation(MOD_ID, string));
    }
}
