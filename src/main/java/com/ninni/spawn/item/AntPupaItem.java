package com.ninni.spawn.item;

import com.ninni.spawn.entity.Ant;
import com.ninni.spawn.registry.SpawnEntityType;
import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class AntPupaItem extends Item {
    public AntPupaItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Direction direction = useOnContext.getClickedFace();
        if (direction == Direction.DOWN) return InteractionResult.FAIL;

        Level level = useOnContext.getLevel();
        BlockPlaceContext blockPlaceContext = new BlockPlaceContext(useOnContext);
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        ItemStack itemStack = useOnContext.getItemInHand();
        Vec3 vec3 = Vec3.atBottomCenterOf(blockPos);
        AABB aABB = SpawnEntityType.ANT.getDimensions().makeBoundingBox(vec3.x(), vec3.y(), vec3.z());
        if (!level.noCollision(null, aABB) || !level.getEntities(null, aABB).isEmpty()) return InteractionResult.FAIL;

        if (level instanceof ServerLevel serverLevel) {
            Consumer consumer = EntityType.createDefaultStackConfig(serverLevel, itemStack, useOnContext.getPlayer());
            Ant ant = SpawnEntityType.ANT.create(serverLevel, itemStack.getTag(), consumer, blockPos, MobSpawnType.SPAWN_EGG, true, true);
            if (ant == null) return InteractionResult.FAIL;

            ant.setTame(true);
            ant.setOwnerUUID(useOnContext.getPlayer().getUUID());
            ant.setBaby(true);

            float f = (float) Mth.floor((Mth.wrapDegrees(useOnContext.getRotation() - 180.0f) + 22.5f) / 45.0f) * 45.0f;
            ant.moveTo(ant.getX(), ant.getY(), ant.getZ(), f, 0.0f);
            serverLevel.addFreshEntityWithPassengers(ant);
            level.playSound(null, ant.getX(), ant.getY(), ant.getZ(), SpawnSoundEvents.ANT_HATCH, SoundSource.NEUTRAL, 0.75f, 0.8f);

            ant.gameEvent(GameEvent.ENTITY_PLACE, useOnContext.getPlayer());
        }
        itemStack.shrink(1);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}

