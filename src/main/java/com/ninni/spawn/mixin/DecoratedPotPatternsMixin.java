package com.ninni.spawn.mixin;

import com.ninni.spawn.registry.SpawnDecoratedPotPatterns;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DecoratedPotPatterns.class)
public abstract class DecoratedPotPatternsMixin {

    @Inject(method = "getResourceKey", at = @At("HEAD"), cancellable = true)
    private static void S$getResourceKey(Item item, CallbackInfoReturnable<@Nullable ResourceKey<String>> cir) {
        if (SpawnDecoratedPotPatterns.S$ITEM_TO_POT_TEXTURE.containsKey(item)) {
            cir.setReturnValue(SpawnDecoratedPotPatterns.S$ITEM_TO_POT_TEXTURE.get(item));
        }
    }
}
