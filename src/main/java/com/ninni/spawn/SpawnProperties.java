package com.ninni.spawn;

import com.ninni.spawn.block.state.properties.SunflowerRotation;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public interface SpawnProperties {
    BooleanProperty SOLID = BooleanProperty.create("solid");
    EnumProperty<SunflowerRotation> SUNFLOWER_ROTATION = EnumProperty.create("sunflower_rotation", SunflowerRotation.class);
}