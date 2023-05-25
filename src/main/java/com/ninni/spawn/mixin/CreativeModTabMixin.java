package com.ninni.spawn.mixin;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeTab.ItemDisplayBuilder.class)
public class CreativeModTabMixin {

    @Inject(at = @At("HEAD"), method = "accept", cancellable = true)
    public void S$accept(ItemStack itemStack, CreativeModeTab.TabVisibility tabVisibility, CallbackInfo ci) {
        if (itemStack.is(Items.SUNFLOWER)) ci.cancel();
    }

}
