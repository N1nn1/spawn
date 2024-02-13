package com.ninni.spawn.mixin;

import com.ninni.spawn.entity.Octopus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Supplier;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin extends AbstractChestBlock<ChestBlockEntity> implements SimpleWaterloggedBlock {

    protected ChestBlockMixin(Properties properties, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier) {
        super(properties, supplier);
    }

    @Inject(at = @At("HEAD"), method = "isChestBlockedAt", cancellable = true)
    private static void S$isChestBlockedAt(LevelAccessor levelAccessor, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (ChestBlockMixin.isOctopusLockingChest(levelAccessor, blockPos)) {
            cir.setReturnValue(ChestBlockMixin.isOctopusLockingChest(levelAccessor, blockPos));
        }

    }


    private static boolean isOctopusLockingChest(LevelAccessor levelAccessor, BlockPos blockPos) {
        List<Octopus> list = levelAccessor.getEntitiesOfClass(Octopus.class, new AABB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1));
        if (!list.isEmpty()) {
            for (Octopus octopus : list) {
                if (!octopus.isLocking() && octopus.getLockingPos() == blockPos) continue;
                return true;
            }
        }
        return false;
    }
}
