package com.ninni.spawn.item;

import com.ninni.spawn.entity.Octopus;
import com.ninni.spawn.registry.SpawnEntityType;
import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class CapturedOctopusItem extends Item {

    public CapturedOctopusItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        BlockHitResult blockHitResult = BucketItem.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        ItemStack itemStack = player.getItemInHand(interactionHand);
        Direction direction = blockHitResult.getDirection();
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockPos blockPos3 = blockPos.relative(direction);

        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            this.spawn(player, interactionHand, level, itemStack, blockPos3);


            level.gameEvent(player, GameEvent.ENTITY_PLACE, blockPos);
            player.awardStat(Stats.ITEM_USED.get(this));

            return InteractionResultHolder.success(itemStack);
        } else {
            CompoundTag compoundTag = itemStack.getTag();
            if (compoundTag != null && compoundTag.contains("Item") && compoundTag.getInt("Item") == 10) {
                player.getCooldowns().addCooldown(this, 20 * 3);
                player.startUsingItem(interactionHand);
                level.playSound(player, player, SpawnSoundEvents.GOAT_HORN_OCTOPUS, SoundSource.RECORDS, 3.0f, 1.0f);
                player.awardStat(Stats.ITEM_USED.get(this));
                level.gameEvent(GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of(player));
                return InteractionResultHolder.consume(itemStack);
            }
        }

        return super.use(level, player, interactionHand);
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null && compoundTag.contains("Item") && compoundTag.getInt("Item") == 10) {
            return 20 * 3;
        }
        return 0;
    }

    private void spawn(Player player, InteractionHand interactionHand, Level level, ItemStack itemStack, BlockPos blockPos) {

        if (level instanceof ServerLevel serverLevel) {
            Octopus entity = SpawnEntityType.OCTOPUS.spawn(serverLevel, itemStack, null, blockPos, MobSpawnType.BUCKET, true, false);
            entity.loadDataFromItem(itemStack.getOrCreateTag());
            entity.setFromBucket(true);
        }

        if (itemStack.hasTag() && itemStack.getTag().contains("Item")) {
            player.setItemInHand(interactionHand, Octopus.getItem(itemStack.getTag().getInt("Item")).getDefaultInstance());
        } else {
            if (!player.isCreative()) itemStack.shrink(1);
        }

    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        return compoundTag != null && compoundTag.contains("Item") && compoundTag.getInt("Item") == 10 ? UseAnim.TOOT_HORN : UseAnim.NONE;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        ChatFormatting[] chatFormattings = new ChatFormatting[]{ChatFormatting.GRAY};
        CompoundTag compoundTag = itemStack.getTag();

        if (compoundTag != null && compoundTag.contains("Item")) {
            list.add(Component.literal(Octopus.getItem(itemStack.getTag().getInt("Item")).getName(itemStack).getString()).withStyle(chatFormattings));
        } else {
            list.add(Component.translatable("item.spawn.captured_octopus.no_container").withStyle(chatFormattings));
        }
    }
}