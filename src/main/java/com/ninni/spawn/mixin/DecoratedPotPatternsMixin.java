package com.ninni.spawn.mixin;

import com.ninni.spawn.registry.SpawnDecoratedPotPatterns;
import com.ninni.spawn.registry.SpawnItems;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(DecoratedPotPatterns.class)
public abstract class DecoratedPotPatternsMixin {
    private static final Map<Item, ResourceKey<String>> S$ITEM_TO_POT_TEXTURE = Map.ofEntries(Map.entry(SpawnItems.CROWN_POTTERY_SHERD, SpawnDecoratedPotPatterns.CROWN));

    @Inject(method = "getResourceKey", at = @At("HEAD"), cancellable = true)
    private static void S$getResourceKey(Item item, CallbackInfoReturnable<@Nullable ResourceKey<String>> cir) {
        if (S$ITEM_TO_POT_TEXTURE.containsKey(item)) {
            cir.setReturnValue(S$ITEM_TO_POT_TEXTURE.get(item));
        }
    }

    @Inject(method = "bootstrap", at = @At("HEAD"))
    private static void S$bootstrap(Registry<String> registry, CallbackInfoReturnable<String> cir) {
        Registry.register(registry, SpawnDecoratedPotPatterns.CROWN, "crown_pottery_pattern");
    }
}
