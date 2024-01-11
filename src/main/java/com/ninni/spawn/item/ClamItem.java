package com.ninni.spawn.item;

import com.ninni.spawn.entity.Clam;
import com.ninni.spawn.entity.variant.ClamVariant;
import com.ninni.spawn.registry.SpawnEntityType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClamItem extends Item {

    public ClamItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            BlockPos blockPos2 = blockPos.relative(direction);

            if (level instanceof ServerLevel) {
                this.spawn((ServerLevel)level, itemStack, blockPos2, player);
                InteractionResultHolder.success(itemStack);
            }
        }

        return super.use(level, player, interactionHand);
    }


    private void spawn(ServerLevel serverLevel, ItemStack itemStack, BlockPos blockPos, Player player) {
        Clam entity;
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null && compoundTag.contains("ItemVariantTag", 3)) {
            entity = SpawnEntityType.CLAM.spawn(serverLevel, itemStack, null, blockPos, MobSpawnType.MOB_SUMMONED, true, false);
            Clam.loadFromBucketTag(entity, itemStack.getOrCreateTag());
        } else {
            entity = SpawnEntityType.CLAM.spawn(serverLevel, itemStack, null, blockPos, MobSpawnType.SPAWN_EGG, true, false);
        }
        entity.setFromItem(true);
        serverLevel.gameEvent(player, GameEvent.ENTITY_PLACE, blockPos);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {

        ChatFormatting[] chatFormattings = new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY};
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null && compoundTag.contains("ItemVariantTag", 3)) {
            int i = compoundTag.getInt("ItemVariantTag");

            String string2 = "color.minecraft." + Clam.getDyeColor(i);
            String string = Clam.getPattern(i).patternDisplayName().getString();
            MutableComponent mutableComponent;
            if (!string.equals(string2) && Clam.getPattern(i) != ClamVariant.Pattern.NO_PATTERN) {
                mutableComponent = Component.translatable(string2);
                mutableComponent.append(" ").append(Component.translatable(string));
            } else {
                mutableComponent = Component.translatable(string);
            }
            mutableComponent.withStyle(chatFormattings);

            list.add(mutableComponent);
            list.add(Clam.getBaseColor(i).colorDisplayName().plainCopy().withStyle(chatFormattings).append(" ").append(Clam.getBaseColor(i).baseDisplayName().plainCopy().withStyle(chatFormattings)));

        } else {
            list.add(Component.translatable("entity.spawn.clam.random").withStyle(chatFormattings));

        }


    }
}
