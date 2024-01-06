package com.ninni.spawn.mixin;

import com.ninni.spawn.entity.Seahorse;
import com.ninni.spawn.registry.SpawnEntityType;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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

@Mixin(MobBucketItem.class)
public abstract class MobBucketItemMixin extends BucketItem {
    @Shadow @Final private EntityType<?> type;

    public MobBucketItemMixin(Fluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (this.type == SpawnEntityType.SEAHORSE) {
            CompoundTag compoundTag = itemStack.getTag();
            if (compoundTag != null && compoundTag.contains("BucketVariantTag", 3)) {
                int i = compoundTag.getInt("BucketVariantTag");
                ChatFormatting[] chatFormattings = new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY};
                String string = "color.minecraft." + Seahorse.getBaseColor(i);
                String string2 = "color.minecraft." + Seahorse.getPatternColor(i);

                list.add(Seahorse.getPattern(i).displayName().plainCopy().withStyle(chatFormattings));
                MutableComponent mutableComponent = Component.translatable(string);
                if (!string.equals(string2)) {
                    mutableComponent.append(", ").append(Component.translatable(string2));
                }
                mutableComponent.withStyle(chatFormattings);
                list.add(mutableComponent);
            }
        }
    }
}
