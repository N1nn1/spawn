package com.ninni.spawn.block;

import com.ninni.spawn.registry.SpawnCriteriaTriggers;
import com.ninni.spawn.registry.SpawnEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
public class MucusBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    public MucusBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (!(entity instanceof LivingEntity) || entity.getType() == SpawnEntityType.SNAIL || entity.getType() == EntityType.BEE) return;
        if (entity instanceof ServerPlayer serverPlayer) SpawnCriteriaTriggers.GOT_STUCK_IN_MUCUS.trigger(serverPlayer);
        entity.makeStuckInBlock(blockState, new Vec3(0.5f, 0.5, 0.5f));
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (!blockState.canSurvive(levelAccessor, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return levelReader.getBlockState(blockPos.below()).isFaceSturdy(levelReader, blockPos, Direction.UP);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }
}
