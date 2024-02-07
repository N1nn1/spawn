package com.ninni.spawn.registry;

import com.ninni.spawn.client.inventory.PigmentShifterMenu;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import static com.ninni.spawn.Spawn.MOD_ID;

public class SpawnMenuTypes {

    public static final MenuType<PigmentShifterMenu> FISH_CUSTOMIZER_MENU = register("fish_customizer", PigmentShifterMenu::new);

    private static <T extends AbstractContainerMenu> MenuType<T> register(String string, MenuType.MenuSupplier<T> menuSupplier) {
        return Registry.register(BuiltInRegistries.MENU, new ResourceLocation(MOD_ID, string), new MenuType<T>(menuSupplier, FeatureFlags.VANILLA_SET));
    }
}
