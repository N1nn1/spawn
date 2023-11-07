package com.ninni.spawn.block;

import com.google.common.collect.Lists;
import com.ninni.spawn.Spawn;
import com.ninni.spawn.SpawnProperties;
import com.ninni.spawn.block.entity.AnthillBlockEntity;
import com.ninni.spawn.entity.Ant;
import com.ninni.spawn.registry.SpawnBlockEntityTypes;
import com.ninni.spawn.registry.SpawnBlocks;
import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("deprecation")
public class AnthillBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty RESOURCE_LEVEL = SpawnProperties.RESOURCE_LEVEL;

    public AnthillBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(((this.stateDefinition.any())).setValue(FACING, Direction.NORTH).setValue(RESOURCE_LEVEL, 0));
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);
        if (!level.isClientSide && blockEntity instanceof AnthillBlockEntity anthillBlockEntity) {
            anthillBlockEntity.angerAnts(player, blockState, AnthillBlockEntity.AntState.EMERGENCY);
            level.updateNeighbourForOutputSignal(blockPos, this);
            this.angerNearbyAnts(level, blockPos);
        }
    }

    private void angerNearbyAnts(Level world, BlockPos pos) {
        List<Ant> antList = world.getEntitiesOfClass(Ant.class, new AABB(pos).inflate(8.0, 6.0, 8.0));
        if (!antList.isEmpty()) {
            List<Player> playerList = world.getEntitiesOfClass(Player.class, new AABB(pos).inflate(8.0, 6.0, 8.0));
            for (Ant ant : antList) {
                if (ant.getTarget() != null) continue;
                ant.setTarget(playerList.get(world.random.nextInt(playerList.size())));
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AnthillBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : AnthillBlock.createTickerHelper(blockEntityType, SpawnBlockEntityTypes.ANTHILL.get(), AnthillBlockEntity::serverTick);
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState blockState, Player player) {
        BlockEntity blockEntity;
        if (!world.isClientSide() && player.isCreative() && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && (blockEntity = world.getBlockEntity(pos)) instanceof AnthillBlockEntity) {
            AnthillBlockEntity blockEntity1 = (AnthillBlockEntity)blockEntity;
            ItemStack itemStack = new ItemStack(this);
            boolean bl = !blockEntity1.hasNoAnts();
            if (bl) {
                CompoundTag nbtCompound = new CompoundTag();
                nbtCompound.put("Ants", blockEntity1.getAnts());
                BlockItem.setBlockEntityData(itemStack, SpawnBlockEntityTypes.ANTHILL.get(), nbtCompound);
                nbtCompound = new CompoundTag();
                itemStack.addTagElement("BlockStateTag", nbtCompound);
                ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                itemEntity.setDefaultPickUpDelay();
                world.addFreshEntity(itemEntity);
            }
        }
        super.playerWillDestroy(world, pos, blockState, player);
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return blockState.getValue(RESOURCE_LEVEL) == 3;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource randomSource) {
        int range = 1;
        List<BlockPos> list = Lists.newArrayList();
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                BlockPos blockPos = new BlockPos(pos.getX() + x, pos.getY() - range, pos.getZ() + z);
                BlockState belowState = world.getBlockState(blockPos);
                if (belowState.is(BlockTags.OVERWORLD_NATURAL_LOGS) || belowState.is(BlockTags.DIRT) && !belowState.is(SpawnBlocks.ANT_MOUND.get())) {
                    list.add(blockPos);
                }
            }
        }
        if (!list.isEmpty()) {
            BlockPos blockPos = list.get(randomSource.nextInt(list.size()));
            BlockState placeState = null;
            if (world.getBlockState(blockPos).is(BlockTags.OVERWORLD_NATURAL_LOGS)) {
                placeState = SpawnBlocks.ROTTEN_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, world.getBlockState(blockPos).getValue(RotatedPillarBlock.AXIS));
            } else if (world.getBlockState(blockPos).is(BlockTags.DIRT) && !world.getBlockState(blockPos).is(SpawnBlocks.ANT_MOUND.get())) {
                placeState = SpawnBlocks.ANT_MOUND.get().defaultBlockState();
            }
            world.setBlock(blockPos, placeState, 2);
            if (world.getBlockEntity(blockPos) instanceof BrushableBlockEntity brushableBlockEntity) {
                brushableBlockEntity.setLootTable(new ResourceLocation(Spawn.MOD_ID, "archaeology/anthill"), blockPos.asLong());
            }
            world.playSound(null, pos, SpawnSoundEvents.ANTHILL_RESOURCE.get(), SoundSource.BLOCKS);
            world.setBlock(pos, state.setValue(RESOURCE_LEVEL, 0), 2);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity;
        Entity entity = builder.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if ((entity instanceof PrimedTnt || entity instanceof Creeper || entity instanceof WitherSkull || entity instanceof WitherBoss || entity instanceof MinecartTNT) && (blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY)) instanceof AnthillBlockEntity) {
            AnthillBlockEntity blockEntity1 = (AnthillBlockEntity)blockEntity;
            blockEntity1.angerAnts(null, state, AnthillBlockEntity.AntState.EMERGENCY);
        }
        return super.getDrops(state, builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, RESOURCE_LEVEL);
    }
}
