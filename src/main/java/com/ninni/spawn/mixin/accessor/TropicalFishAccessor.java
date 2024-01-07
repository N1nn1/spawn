package com.ninni.spawn.mixin.accessor;

import net.minecraft.world.entity.animal.TropicalFish;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TropicalFish.class)
public interface TropicalFishAccessor {
    @Invoker
    void callSetPackedVariant(int i);

    @Invoker
    int callGetPackedVariant();
}
