package com.ninni.spawn;

import com.ninni.spawn.block.state.properties.SunflowerRotation;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public interface SpawnProperties {
    BooleanProperty SOLID = BooleanProperty.create("solid");
    BooleanProperty TEETH = BooleanProperty.create("teeth");
    BooleanProperty SEEDS = BooleanProperty.create("seeds");
    EnumProperty<SunflowerRotation> SUNFLOWER_ROTATION = EnumProperty.create("sunflower_rotation", SunflowerRotation.class);
    IntegerProperty LEAVES = IntegerProperty.create("leaves", 1, 4);
    IntegerProperty RESOURCE_LEVEL = IntegerProperty.create("resource_level", 0, 3);
    IntegerProperty COOLDOWN = IntegerProperty.create("cooldown", 0, 20);
}