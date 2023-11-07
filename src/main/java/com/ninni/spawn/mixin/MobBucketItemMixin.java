package com.ninni.spawn.mixin;

import com.ninni.spawn.entity.variant.SeahorseVariant;
import com.ninni.spawn.registry.SpawnEntityType;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Supplier;

@Mixin(MobBucketItem.class)
public class MobBucketItemMixin {

    @Shadow @Final private Supplier<? extends EntityType<?>> entityTypeSupplier;

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo ci) {
        CompoundTag compoundTag;
        if (this.entityTypeSupplier.get() == SpawnEntityType.SEAHORSE.get() && (compoundTag = itemStack.getTag()) != null && compoundTag.contains("BucketVariantTag", 3)) {
            int i = compoundTag.getInt("BucketVariantTag");
            ChatFormatting[] chatFormattings = new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY};
            list.add(Component.translatable("entity.spawn.seahorse.variant." + SeahorseVariant.byId(i).getSerializedName()).withStyle(chatFormattings));
        }
    }
}
