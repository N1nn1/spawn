package com.ninni.spawn.mixin;

import com.ninni.spawn.entity.Octopus;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stat;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Supplier;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin extends AbstractChestBlock<ChestBlockEntity> implements SimpleWaterloggedBlock {

    @Shadow protected abstract Stat<ResourceLocation> getOpenChestStat();

    protected ChestBlockMixin(Properties properties, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier) {
        super(properties, supplier);
    }

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    private void S$use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {

        List<Octopus> list = level.getEntitiesOfClass(Octopus.class, new AABB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1));

        if (!list.isEmpty()) {
            ItemStack stack = player.getItemInHand(interactionHand);



            if (!stack.isEmpty() && list.get(0).getOwnerUUID() == player.getUUID()) {
                if (((stack.hasTag() && !stack.getTag().contains("OctoKey")) || !stack.hasTag())) {
                    ItemStack stack2 = stack.copy();

                    CompoundTag tag = stack.getOrCreateTag();
                    tag.putUUID("OctoKey", list.get(0).getUUID());
                    if (list.get(0).hasCustomName()) tag.putString("OctoName", list.get(0).getCustomName().getString());
                    else tag.putString("OctoName", Component.translatable("entity.spawn.octopus").getString());
                    stack2.setTag(tag);
                    player.setItemInHand(interactionHand, stack2);
                    player.displayClientMessage(Component.translatable("spawn.container.createdKey", stack2.getHoverName()), true);

                    cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
                }
            }
            else {
                if (list.get(0).getOwnerUUID() == player.getUUID() || (stack.hasTag() && stack.getTag().contains("OctoKey") && stack.getTag().getUUID("OctoKey").equals(list.get(0).getUUID()))) {
                    MenuProvider menuProvider = this.getMenuProvider(blockState, level, blockPos);
                    if (menuProvider != null) {
                        player.openMenu(menuProvider);
                        player.awardStat(this.getOpenChestStat());
                        PiglinAi.angerNearbyPiglins(player, true);
                    }
                } else {
                    player.displayClientMessage(Component.translatable("container.isLocked", ((ChestBlockEntity)level.getBlockEntity(blockPos)).getDisplayName()), true);
                    level.playSound(player,blockPos, SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            }


        }


    }
}
