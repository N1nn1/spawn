package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.ninni.spawn.Spawn.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpawnSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);

    public static final RegistryObject<SoundEvent> FISH_AMBIENT = register("entity.fish.ambient");
    public static final RegistryObject<SoundEvent> FISH_FLOP = register("entity.fish.flop");
    public static final RegistryObject<SoundEvent> FISH_SWIM = register("entity.fish.swim");
    public static final RegistryObject<SoundEvent> FISH_HURT = register("entity.fish.hurt");
    public static final RegistryObject<SoundEvent> FISH_DEATH = register("entity.fish.death");
    public static final RegistryObject<SoundEvent> BIG_FISH_SWIM = register("entity.big_fish.swim");

    public static final RegistryObject<SoundEvent> ANGLER_FISH_DEFLATE = register("entity.angler_fish.deflate");
    public static final RegistryObject<SoundEvent> ANGLER_FISH_EFFECT_GIVE = register("entity.angler_fish.effect.give");
    public static final RegistryObject<SoundEvent> ANGLER_FISH_EFFECT_DENY = register("entity.angler_fish.effect.deny");

    public static final RegistryObject<SoundEvent> TUNA_EGG_HIT = register("entity.tuna_egg.hit");
    public static final RegistryObject<SoundEvent> TUNA_EGG_BROKEN = register("entity.tuna_egg.broken");
    public static final RegistryObject<SoundEvent> BUCKET_EMPTY_TUNA_EGG = register("item.bucket.empty_tuna_egg");
    public static final RegistryObject<SoundEvent> BUCKET_FILL_TUNA_EGG = register("item.bucket.fill_tuna_egg");

    public static final RegistryObject<SoundEvent> SEAHORSE_GROWL = register("entity.seahorse.growl");
    public static final RegistryObject<SoundEvent> SEAHORSE_CLICK = register("entity.seahorse.click");
    public static final RegistryObject<SoundEvent> SEAHORSE_HURT = register("entity.seahorse.hurt");
    public static final RegistryObject<SoundEvent> SEAHORSE_DEATH = register("entity.seahorse.death");

    public static final RegistryObject<SoundEvent> SNAIL_SLIDE = register("entity.snail.slide");
    public static final RegistryObject<SoundEvent> SNAIL_HURT_HIDDEN = register("entity.snail.hurt_hidden");
    public static final RegistryObject<SoundEvent> SNAIL_HURT = register("entity.snail.hurt");
    public static final RegistryObject<SoundEvent> SNAIL_EAT = register("entity.snail.eat");
    public static final RegistryObject<SoundEvent> SNAIL_DEATH = register("entity.snail.death");
    public static final RegistryObject<SoundEvent> SNAIL_DEATH_HIDDEN = register("entity.snail.death_hidden");
    public static final RegistryObject<SoundEvent> SNAIL_SHELL_GROW = register("entity.snail.shell_grow");
    public static final RegistryObject<SoundEvent> SNAIL_LAY_EGGS = register("entity.snail.lay_eggs");
    public static final RegistryObject<SoundEvent> SNAIL_EGGS_HATCH = register("block.snail_eggs.hatch");

    public static final RegistryObject<SoundEvent> HAMSTER_AMBIENT = register("entity.hamster.idle");
    public static final RegistryObject<SoundEvent> HAMSTER_AMBIENT_CALL = register("entity.hamster.idle_call");
    public static final RegistryObject<SoundEvent> HAMSTER_STEP = register("entity.hamster.step");
    public static final RegistryObject<SoundEvent> HAMSTER_HURT = register("entity.hamster.hurt");
    public static final RegistryObject<SoundEvent> HAMSTER_EAT = register("entity.hamster.eat");
    public static final RegistryObject<SoundEvent> HAMSTER_DEATH = register("entity.hamster.death");

    public static final RegistryObject<SoundEvent> SUNFLOWER_SEED_PICKUP = register("block.sunflower.seed_pickup");

    public static final RegistryObject<SoundEvent> ANT_AMBIENT = register("entity.ant.idle");
    public static final RegistryObject<SoundEvent> ANT_HURT = register("entity.ant.hurt");
    public static final RegistryObject<SoundEvent> ANT_EAT = register("entity.ant.eat");
    public static final RegistryObject<SoundEvent> ANT_DEATH = register("entity.ant.death");
    public static final RegistryObject<SoundEvent> ANT_STEP = register("entity.ant.step");
    public static final RegistryObject<SoundEvent> ANT_COLLECT_RESOURCE = register("entity.ant.collect_resource");

    public static final RegistryObject<SoundEvent> ANT_HATCH = register("entity.ant.hatch");

    public static final RegistryObject<SoundEvent> ANTHILL_ENTER = register("block.anthill.enter");
    public static final RegistryObject<SoundEvent> ANTHILL_EXIT = register("block.anthill.exit");
    public static final RegistryObject<SoundEvent> ANTHILL_WORK = register("block.anthill.work");
    public static final RegistryObject<SoundEvent> ANTHILL_RESOURCE = register("block.anthill.resource");

    public static final RegistryObject<SoundEvent> ROTTEN_WOOD_CRACK = register("block.rotten_wood.crack");

    public static final RegistryObject<SoundEvent> MUSIC_DISC_ROT = register("music_disc.rot");

    public static final RegistryObject<SoundEvent> ROTTEN_WOOD_FENCE_GATE_OPEN = register("block.rotten_wood_fence_gate.open");
    public static final RegistryObject<SoundEvent> ROTTEN_WOOD_FENCE_GATE_CLOSE = register("block.rotten_wood_fence_gate.close");
    public static final RegistryObject<SoundEvent> ROTTEN_WOOD_DOOR_OPEN = register("block.rotten_wood_door.open");
    public static final RegistryObject<SoundEvent> ROTTEN_WOOD_DOOR_CLOSE = register("block.rotten_wood_door.close");
    public static final RegistryObject<SoundEvent> ROTTEN_WOOD_TRAPDOOR_OPEN = register("block.rotten_wood_trapdoor.open");
    public static final RegistryObject<SoundEvent> ROTTEN_WOOD_TRAPDOOR_CLOSE = register("block.rotten_wood_trapdoor.close");

    public static final SoundType MUCUS = register("mucus", 1, 1);
    public static final SoundType ROTTEN_WOOD = register("rotten_wood", 1, 1);
    public static final SoundType CRACKED_ROTTEN_WOOD = register("cracked_rotten_wood", 1, 1);
    public static final SoundType FALLEN_LEAVES = register("fallen_leaves", 1, 1);
    public static final SoundType SNAIL_SHELL = register("snail_shell", 1, 1);

	private static SoundType register(String name, float volume, float pitch) {
        return new ForgeSoundType(volume, pitch, register("block." + name + ".break"), register("block." + name + ".step"), register("block." + name + ".place"), register("block." + name + ".hit"), register("block." + name + ".fall"));
    }

    public static RegistryObject<SoundEvent> register(String name) {
        ResourceLocation id = new ResourceLocation(MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
}