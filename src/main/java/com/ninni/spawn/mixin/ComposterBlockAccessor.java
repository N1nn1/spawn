package com.ninni.spawn.mixin;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ComposterBlock.class)
public interface ComposterBlockAccessor {
    @Invoker
    static void callAdd(float p_51921_, ItemLike p_51922_) {
        throw new UnsupportedOperationException();
    }
}
