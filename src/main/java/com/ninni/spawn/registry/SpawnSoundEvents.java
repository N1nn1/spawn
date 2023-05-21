package com.ninni.spawn.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;

import static com.ninni.spawn.Spawn.MOD_ID;

public interface SpawnSoundEvents {

    SoundEvent ENTITY_FISH_AMBIENT = register("entity.fish.ambient");
    SoundEvent ENTITY_FISH_FLOP = register("entity.fish.flop");
    SoundEvent ENTITY_FISH_SWIM = register("entity.fish.swim");
    SoundEvent ENTITY_FISH_HURT = register("entity.fish.hurt");
    SoundEvent ENTITY_FISH_DEATH = register("entity.fish.death");

    SoundEvent ENTITY_ANGLER_FISH_DEFLATE = register("entity.angler_fish.deflate");
    SoundEvent ENTITY_ANGLER_FISH_EFFECT_GIVE = register("entity.angler_fish.effect.give");
    SoundEvent ENTITY_ANGLER_FISH_EFFECT_DENY = register("entity.angler_fish.effect.deny");

    SoundEvent ENTITY_SEAHORSE_GROWL = register("entity.seahorse.growl");
    SoundEvent ENTITY_SEAHORSE_CLICK = register("entity.seahorse.click");
    SoundEvent ENTITY_SEAHORSE_HURT = register("entity.seahorse.hurt");
    SoundEvent ENTITY_SEAHORSE_DEATH = register("entity.seahorse.death");

    SoundEvent ENTITY_SNAIL_SLIDE = register("entity.snail.slide");
    SoundEvent ENTITY_SNAIL_HURT_HIDDEN = register("entity.snail.hurt_hidden");
    SoundEvent ENTITY_SNAIL_HURT = register("entity.snail.hurt");
    SoundEvent ENTITY_SNAIL_EAT = register("entity.snail.eat");
    SoundEvent ENTITY_SNAIL_DEATH = register("entity.snail.death");
    SoundEvent ENTITY_SNAIL_DEATH_HIDDEN = register("entity.snail.death_hidden");
    SoundEvent ENTITY_SNAIL_SHELL_GROW = register("entity.snail.shell_grow");
    //TODO sounds
    SoundEvent ENTITY_SNAIL_LAY_EGGS = register("entity.snail.lay_eggs");
    SoundEvent BLOCK_SNAIL_EGGS_HATCH = register("block.snail_eggs.hatch");

    SoundType MUCUS = register("mucus", 1, 1);
    SoundType SNAIL_SHELL = register("snail_shell", 1, 1);

	private static SoundType register(String name, float volume, float pitch) {
        return new SoundType(volume, pitch, register("block." + name + ".break"), register("block." + name + ".step"), register("block." + name + ".place"), register("block." + name + ".hit"), register("block." + name + ".fall"));
    }

    static SoundEvent register(String name) {
        ResourceLocation id = new ResourceLocation(MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
}