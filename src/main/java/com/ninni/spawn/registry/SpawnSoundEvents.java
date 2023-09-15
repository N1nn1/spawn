package com.ninni.spawn.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;

import static com.ninni.spawn.Spawn.MOD_ID;

public interface SpawnSoundEvents {

    SoundEvent FISH_AMBIENT = register("entity.fish.ambient");
    SoundEvent FISH_FLOP = register("entity.fish.flop");
    SoundEvent FISH_SWIM = register("entity.fish.swim");
    SoundEvent FISH_HURT = register("entity.fish.hurt");
    SoundEvent FISH_DEATH = register("entity.fish.death");
    SoundEvent BIG_FISH_SWIM = register("entity.big_fish.swim");

    SoundEvent ANGLER_FISH_DEFLATE = register("entity.angler_fish.deflate");
    SoundEvent ANGLER_FISH_EFFECT_GIVE = register("entity.angler_fish.effect.give");
    SoundEvent ANGLER_FISH_EFFECT_DENY = register("entity.angler_fish.effect.deny");

    //TODO
    SoundEvent TUNA_EGG_HIT = register("entity.tuna_egg.hit");
    //TODO
    SoundEvent TUNA_EGG_BROKEN = register("entity.tuna_egg.broken");
    //TODO
    SoundEvent BUCKET_EMPTY_TUNA_EGG = register("item.bucket.empty_tuna_egg");
    //TODO
    SoundEvent BUCKET_FILL_TUNA_EGG = register("item.bucket.fill_tuna_egg");

    SoundEvent SEAHORSE_GROWL = register("entity.seahorse.growl");
    SoundEvent SEAHORSE_CLICK = register("entity.seahorse.click");
    SoundEvent SEAHORSE_HURT = register("entity.seahorse.hurt");
    SoundEvent SEAHORSE_DEATH = register("entity.seahorse.death");

    SoundEvent SNAIL_SLIDE = register("entity.snail.slide");
    SoundEvent SNAIL_HURT_HIDDEN = register("entity.snail.hurt_hidden");
    SoundEvent SNAIL_HURT = register("entity.snail.hurt");
    SoundEvent SNAIL_EAT = register("entity.snail.eat");
    SoundEvent SNAIL_DEATH = register("entity.snail.death");
    SoundEvent SNAIL_DEATH_HIDDEN = register("entity.snail.death_hidden");
    SoundEvent SNAIL_SHELL_GROW = register("entity.snail.shell_grow");
    SoundEvent SNAIL_LAY_EGGS = register("entity.snail.lay_eggs");
    SoundEvent SNAIL_EGGS_HATCH = register("block.snail_eggs.hatch");

    SoundEvent HAMSTER_AMBIENT = register("entity.hamster.idle");
    SoundEvent HAMSTER_AMBIENT_CALL = register("entity.hamster.idle_call");
    SoundEvent HAMSTER_STEP = register("entity.hamster.step");
    SoundEvent HAMSTER_HURT = register("entity.hamster.hurt");
    SoundEvent HAMSTER_EAT = register("entity.hamster.eat");
    SoundEvent HAMSTER_DEATH = register("entity.hamster.death");
    //TODO
    SoundEvent SUNFLOWER_SEED_PICKUP = register("block.sunflower.seed_pickup");


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