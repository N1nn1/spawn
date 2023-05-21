package com.ninni.spawn.item;


import com.ninni.spawn.registry.SpawnItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EscargotItem extends Item {
    public EscargotItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        ItemStack itemStack = super.finishUsingItem(stack, world, user);
        if (user instanceof Player && ((Player)user).getAbilities().instabuild) return itemStack;
        return new ItemStack(SpawnItems.SNAIL_SHELL);
    }
}

