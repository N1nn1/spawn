package com.ninni.spawn.block;

import com.ninni.spawn.SpawnProperties;
import com.ninni.spawn.block.entity.ClamLauncherBlockEntity;
import com.ninni.spawn.block.entity.PigmentShifterBlockEntity;
import com.ninni.spawn.registry.SpawnBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ClamLauncherBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final IntegerProperty COOLDOWN = SpawnProperties.COOLDOWN;
    protected static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 3, 16);

    public ClamLauncherBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false).setValue(POWERED, false).setValue(COOLDOWN, 0));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ClamLauncherBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {

            if (!blockState.getValue(POWERED) && blockState.getValue(COOLDOWN) == 0) {
                this.open(blockState, level, blockPos);
                return InteractionResult.sidedSuccess(true);
            }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        boolean hasSignal = level.hasNeighborSignal(blockPos) || level.hasNeighborSignal(blockPos.above());

        if (hasSignal && !blockState.getValue(POWERED) && blockState.getValue(COOLDOWN) == 0) {
            this.open(blockState, level, blockPos);
        }

    }

    public void open(BlockState blockState, Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.5f, 1);
        level.setBlockAndUpdate(pos, blockState.setValue(POWERED, true).setValue(COOLDOWN, 20));
        level.scheduleTick(new BlockPos(pos), this, 10);

        if (blockState.getValue(WATERLOGGED) && level instanceof  ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, 15,0.05,0.5,0.05,0.2);
        }

    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(POWERED, false));
        serverLevel.playSound(null, blockPos, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.5f, 1);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        super.entityInside(blockState, level, blockPos, entity);
        if (blockState.getValue(POWERED)) {
            level.playSound(null, blockPos, SoundEvents.SLIME_JUMP, SoundSource.BLOCKS, 1, 1);
            bounce(entity, blockState);
        }
    }

    private void bounce(Entity entity, BlockState state) {
        Vec3 vec3 = entity.getDeltaMovement();
        double d = entity instanceof LivingEntity ? 1.0 : 0.4;
        double s = entity instanceof LivingEntity ? 1.0 : 0.2;
        double e = entity instanceof LivingEntity livingEntity && livingEntity.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA) ? 1.75 : 1.0;
        double g = entity instanceof LivingEntity livingEntity && livingEntity.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA) ? 3.75 : 1.0;
        if (entity instanceof LivingEntity livingEntity && livingEntity.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA) && livingEntity.isInWaterOrBubble()) {
            g = 2;
            e = 3;
        }

        double x = ((vec3.x * 0.2f + 2) * s) * g;
        double y = ((vec3.y * 0.2f + 1.5) * d) * e;
        double z = ((vec3.z * 0.2f + 2) * s) * g;

        Vec3 vec31 = new Vec3(vec3.x, y, vec3.z);

        if (state.getValue(FACING) == Direction.NORTH) vec31 = new Vec3(vec3.x, y, z);
        else if (state.getValue(FACING) == Direction.EAST) vec31 = new Vec3(-x, y, vec3.z);
        else if (state.getValue(FACING) == Direction.WEST) vec31 = new Vec3(x, y, vec3.z);
        else if (state.getValue(FACING) == Direction.SOUTH) vec31 = new Vec3(vec3.x, y, -z);

        entity.setDeltaMovement(vec31);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return ClamLauncherBlock.createTickerHelper(blockEntityType, SpawnBlockEntityTypes.CLAM_LAUNCHER, level.isClientSide ? ClamLauncherBlockEntity::clientTick : ClamLauncherBlockEntity::serverTick);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        if (blockState.getValue(WATERLOGGED).booleanValue()) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(blockState);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (blockState.getValue(WATERLOGGED).booleanValue()) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, POWERED, COOLDOWN);
    }
}
