package com.ninni.spawn.block;


import com.ninni.spawn.entity.Snail;
import com.ninni.spawn.registry.SpawnEntityType;
import com.ninni.spawn.registry.SpawnItems;
import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("deprecation")
public class SnailEggsBlock extends MultifaceBlock {
    private final MultifaceSpreader spreader = new MultifaceSpreader(this);


    public SnailEggsBlock(Block.Properties settings) {
        super(settings);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return !blockPlaceContext.getItemInHand().is(SpawnItems.SNAIL_EGGS) || super.canBeReplaced(blockState, blockPlaceContext);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        level.scheduleTick(blockPos, this, getHatchTime(level.getRandom()));
        super.onPlace(blockState, level, blockPos, blockState2, bl);
    }

    private static int getHatchTime(RandomSource random) {
        return random.nextInt(6000, 12000);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        world.destroyBlock(pos, false);
        world.playSound(null, pos, SpawnSoundEvents.SNAIL_EGGS_HATCH, SoundSource.BLOCKS, 1.0f, 1.0f);
        this.spawnSnails(world, pos, random);
    }

    private void spawnSnails(ServerLevel world, BlockPos pos, RandomSource random) {
        int i = random.nextInt(2, 4);
        for (int j = 1; j <= i; ++j) {
            Snail snail = SpawnEntityType.SNAIL.create(world);
            assert snail != null;
            double d = (double)pos.getX() + this.getSpawnOffset(random);
            double e = (double)pos.getZ() + this.getSpawnOffset(random);
            int k = random.nextInt(1, 361);
            snail.moveTo(d, pos.getY(), e, k, 0.0f);
            snail.setPersistenceRequired();
            snail.setBaby(true);
            world.addFreshEntity(snail);
        }
    }

    private double getSpawnOffset(RandomSource random) {
        double d = 0.4F;
        return Mth.clamp(random.nextDouble(), d, 1.0 - d);
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return this.spreader;
    }
}
