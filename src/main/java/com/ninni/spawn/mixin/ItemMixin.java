package com.ninni.spawn.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin {


    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void S$use(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {

        float f = player.getXRot();
        float g = player.getYRot();
        Vec3 vec3 = player.getEyePosition();
        float h = Mth.cos(-g * ((float)Math.PI / 180) - (float)Math.PI);
        float i = Mth.sin(-g * ((float)Math.PI / 180) - (float)Math.PI);
        float j = -Mth.cos(-f * ((float)Math.PI / 180));
        float k = Mth.sin(-f * ((float)Math.PI / 180));
        float l = i * j;
        float n = h * j;
        Vec3 vec32 = vec3.add((double)l * 5.0, (double) k * 5.0, (double)n * 5.0);

        BlockHitResult blockHitResult = level.clip(new ClipContext(vec3, vec32, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        BlockPos blockPos = blockHitResult.getBlockPos();
        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (level.getBlockState(blockPos).is(Blocks.WATER_CAULDRON) && itemStack.hasTag() && itemStack.getTag().contains("OctoKey")) {
            ItemStack itemStack2 = itemStack.copy();

            CompoundTag tag = itemStack.getOrCreateTag();
            tag.remove("OctoKey");
            itemStack2.setTag(tag);
            player.setItemInHand(interactionHand, itemStack2);
            LayeredCauldronBlock.lowerFillLevel(level.getBlockState(blockPos), level, blockPos);

            cir.setReturnValue(InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide));
        }


    }

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    public void S$appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (itemStack.hasTag() && itemStack.getTag().contains("OctoKey")) {
            list.add(Component.translatable("spawn.item.isKey").withStyle(new ChatFormatting[]{ChatFormatting.GRAY}));
        }
    }

}
