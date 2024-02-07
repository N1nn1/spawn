package com.ninni.spawn.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.PufferfishRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pufferfish;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PufferfishRenderer.class)
@Environment(value= EnvType.CLIENT)
public class PufferfishRendererMixin {
    @Unique
    private static final ResourceLocation NOON_LOCATION = new ResourceLocation("textures/entity/fish/noon.png");

    @Inject(at = @At("HEAD"), method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Pufferfish;)Lnet/minecraft/resources/ResourceLocation;", cancellable = true)
    private void S$getTextureLocation(Pufferfish pufferfish, CallbackInfoReturnable<ResourceLocation> cir) {
        if (pufferfish.getName().getString().equalsIgnoreCase("noon") || pufferfish.getName().getString().equalsIgnoreCase("stinkyfish")) cir.setReturnValue(NOON_LOCATION);
    }

}
