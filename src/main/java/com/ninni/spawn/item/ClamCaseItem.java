package com.ninni.spawn.item;

import com.ninni.spawn.SpawnTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ClamCaseItem extends Item implements DyeableLeatherItem {
    private static final String TAG_ITEMS = "Items";
    private static final int BAR_COLOR = Mth.color(0.4f, 0.4f, 1.0f);
    public static final int MAX_WEIGHT = 16;

    public ClamCaseItem(Item.Properties properties) {
        super(properties);
    }

    public int getColor(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTagElement(TAG_DISPLAY);
        if (compoundTag != null && compoundTag.contains(TAG_COLOR, 99)) {
            return compoundTag.getInt(TAG_COLOR);
        }
        return 0x0077FF;
    }

    public static float getFullnessDisplay(ItemStack itemStack) {
        return (float) ClamCaseItem.getContentWeight(itemStack) / MAX_WEIGHT;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack itemStack, Slot slot, ClickAction clickAction, Player player) {
        if (clickAction != ClickAction.SECONDARY) {
            return false;
        }
        ItemStack itemStack22 = slot.getItem();
        if (itemStack22.isEmpty()) {
            this.playRemoveOneSound(player);
            ClamCaseItem.removeOne(itemStack).ifPresent(itemStack2 -> ClamCaseItem.add(itemStack, slot.safeInsert(itemStack2)));
        } else if (itemStack22.getItem().canFitInsideContainerItems()) {
            int j = ClamCaseItem.add(itemStack, itemStack22);
            if (j > 0) {
                this.playInsertSound(player);
                itemStack22.shrink(j);
            }
        }
        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack itemStack2, ItemStack itemStack22, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction != ClickAction.SECONDARY || !slot.allowModification(player)) {
            return false;
        }
        if (itemStack22.isEmpty()) {
            ClamCaseItem.removeOne(itemStack2).ifPresent(itemStack -> {
                this.playRemoveOneSound(player);
                slotAccess.set(itemStack);
            });
        } else {
            int i = ClamCaseItem.add(itemStack2, itemStack22);
            if (i > 0) {
                this.playInsertSound(player);
                itemStack22.shrink(i);
            }
        }
        return true;
    }

    public static int add(ItemStack itemStack, ItemStack itemStack2) {
        if (itemStack2.is(SpawnTags.CLAM_CASE_BLACKLIST) || itemStack2.isEmpty() || itemStack2.getItem().getMaxStackSize() != 1) {
            return 0;
        } else if (itemStack2.getItem() instanceof MobBucketItem || itemStack2.is(SpawnTags.ADDITIONAL_CLAM_CASE_ITEMS)) {
            CompoundTag compoundTag = itemStack.getOrCreateTag();
            if (!compoundTag.contains(TAG_ITEMS)) {
                compoundTag.put(TAG_ITEMS, new ListTag());
            }

            int i = ClamCaseItem.getContentWeight(itemStack);
            int k = Math.min(itemStack2.getCount(), (MAX_WEIGHT - i));
            if (k == 0) {
                return 0;
            }
            ListTag listTag = compoundTag.getList(TAG_ITEMS, 10);

            ItemStack itemStack4 = itemStack2.copyWithCount(k);
            CompoundTag compoundTag3 = new CompoundTag();
            itemStack4.save(compoundTag3);
            listTag.add(0, compoundTag3);
            return k;
        } else return 0;
    }

    public static int getContentWeight(ItemStack itemStack2) {
        return ClamCaseItem.getContents(itemStack2).mapToInt(ItemStack::getCount).sum();
    }

    private static Optional<ItemStack> removeOne(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (!compoundTag.contains(TAG_ITEMS)) {
            return Optional.empty();
        }
        ListTag listTag = compoundTag.getList(TAG_ITEMS, 10);
        if (listTag.isEmpty()) {
            return Optional.empty();
        }
        CompoundTag compoundTag2 = listTag.getCompound(0);
        ItemStack itemStack2 = ItemStack.of(compoundTag2);
        listTag.remove(0);
        if (listTag.isEmpty()) {
            itemStack.removeTagKey(TAG_ITEMS);
        }
        return Optional.of(itemStack2);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (ClamCaseItem.dropContents(itemStack, player)) {
            this.playDropContentsSound(player);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public boolean isBarVisible(ItemStack itemStack) {
        return ClamCaseItem.getContentWeight(itemStack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return Math.min(1 + 12 * ClamCaseItem.getContentWeight(itemStack) / MAX_WEIGHT, 13);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return BAR_COLOR;
    }

    private static boolean dropContents(ItemStack itemStack, Player player) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (!compoundTag.contains(TAG_ITEMS) || !player.isCrouching()) {
            return false;
        }
        if (player instanceof ServerPlayer) {
            ListTag listTag = compoundTag.getList(TAG_ITEMS, 10);
            for (int i = 0; i < listTag.size(); ++i) {
                CompoundTag compoundTag2 = listTag.getCompound(i);
                ItemStack itemStack2 = ItemStack.of(compoundTag2);
                player.drop(itemStack2, true);
            }
        }
        itemStack.removeTagKey(TAG_ITEMS);
        return true;
    }

    private static Stream<ItemStack> getContents(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null) {
            return Stream.empty();
        }
        ListTag listTag = compoundTag.getList(TAG_ITEMS, 10);
        return listTag.stream().map(CompoundTag.class::cast).map(ItemStack::of);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        NonNullList<ItemStack> nonNullList = NonNullList.create();
        ClamCaseItem.getContents(itemStack).forEach(nonNullList::add);
        return Optional.of(new BundleTooltip(nonNullList, ClamCaseItem.getContentWeight(itemStack)));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("item.spawn.clam_case.fullness", ClamCaseItem.getContentWeight(itemStack), MAX_WEIGHT).withStyle(ChatFormatting.GRAY));
        if (ClamCaseItem.getContents(itemStack).toList().isEmpty()) list.add(Component.translatable("item.spawn.clam_case.desc").withStyle(ChatFormatting.BLUE));
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        ItemUtils.onContainerDestroyed(itemEntity, ClamCaseItem.getContents(itemEntity.getItem()));
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8f, 0.8f + entity.level().getRandom().nextFloat() * 0.4f);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8f, 0.8f + entity.level().getRandom().nextFloat() * 0.4f);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8f, 0.8f + entity.level().getRandom().nextFloat() * 0.4f);
    }
}