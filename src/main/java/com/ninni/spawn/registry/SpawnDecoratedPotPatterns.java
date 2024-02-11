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
    public static final ResourceKey<String> SPADE = register("spade_pottery_pattern");
    public static final ResourceKey<String> SCHOOL = register("school_pottery_pattern");
    public static final ResourceKey<String> SHELL = register("shell_pottery_pattern");
    @Unique public static final Map<Item, ResourceKey<String>> S$ITEM_TO_POT_TEXTURE = new HashMap<>();

    static {
        S$ITEM_TO_POT_TEXTURE.put(SpawnItems.CROWN_POTTERY_SHERD, CROWN);
        S$ITEM_TO_POT_TEXTURE.put(SpawnItems.SPADE_POTTERY_SHERD, SPADE);
        S$ITEM_TO_POT_TEXTURE.put(SpawnItems.SCHOOL_POTTERY_SHERD, SCHOOL);
        S$ITEM_TO_POT_TEXTURE.put(SpawnItems.SHELL_POTTERY_SHERD, SHELL);
        Registry.register(BuiltInRegistries.DECORATED_POT_PATTERNS, CROWN, "crown_pottery_pattern");
        Registry.register(BuiltInRegistries.DECORATED_POT_PATTERNS, SPADE, "spade_pottery_pattern");
        Registry.register(BuiltInRegistries.DECORATED_POT_PATTERNS, SCHOOL, "school_pottery_pattern");
        Registry.register(BuiltInRegistries.DECORATED_POT_PATTERNS, SHELL, "shell_pottery_pattern");
    }

    public static ResourceKey<String> register(String string) {
        return ResourceKey.create(Registries.DECORATED_POT_PATTERNS, new ResourceLocation(MOD_ID, string));
    }
}
