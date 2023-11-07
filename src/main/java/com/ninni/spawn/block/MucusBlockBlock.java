package com.ninni.spawn.block;


import com.ninni.spawn.SpawnProperties;
import com.ninni.spawn.SpawnTags;
import com.ninni.spawn.registry.SpawnCriteriaTriggers;
import com.ninni.spawn.registry.SpawnEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class MucusBlockBlock extends Block {
    private static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;
    public static final BooleanProperty SOLID = SpawnProperties.SOLID;
    protected static final VoxelShape SHAPE = Block.box(1, 1, 1, 15, 10, 15);

    public MucusBlockBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(SOLID, false).setValue(DISTANCE, 7));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return updateDistanceFromWater(this.defaultBlockState(), ctx.getLevel(), ctx.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        int i;
        if ((i = getDistanceFromWater(blockState2) + 1) != 1 || blockState.getValue(DISTANCE) != i) {
            levelAccessor.scheduleTick(blockPos, this, 2);
        }
        return blockState;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        serverLevel.setBlock(blockPos, updateDistanceFromWater(blockState, serverLevel, blockPos), Block.UPDATE_ALL);
    }

    private static BlockState updateDistanceFromWater(BlockState state, Level world, BlockPos pos) {
        int i = 7;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            mutable.setWithOffset(pos, direction);
            i = Math.min(i, getDistanceFromWater(world.getBlockState(mutable)) + 1);
            if (i == 1) break;
        }
        world.playSound(null, pos, world.getBlockState(pos).getBlock().getSoundType(state).getBreakSound(), SoundSource.BLOCKS, 0.15F, 1.5F);
        return state.setValue(DISTANCE, i).setValue(SOLID, i < 7);
    }

    public static int getDistanceFromWater(BlockState state) {
        if (state.is(SpawnTags.MUCUS_SOLIDIFIER)) {
            return 0;
        }
        if (state.is(SpawnTags.MUCUS_SOLIDIFICATION_SPREADER)) {
            return state.getValue(DISTANCE);
        }
        return 7;
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (!(entity instanceof LivingEntity) || entity.getType() == SpawnEntityType.SNAIL.get() || entity.getType() == EntityType.BEE) {
            return;
        }
        if (entity instanceof ServerPlayer serverPlayer) SpawnCriteriaTriggers.GOT_STUCK_IN_MUCUS.trigger(serverPlayer);
        entity.makeStuckInBlock(blockState, new Vec3(0.5f, 0.5, 0.5f));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return blockState.getValue(SOLID) ? Shapes.block() : SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SOLID, DISTANCE);
    }
}
