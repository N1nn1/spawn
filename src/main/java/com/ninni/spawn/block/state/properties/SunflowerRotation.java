package com.ninni.spawn.block.state.properties;

import net.minecraft.util.StringRepresentable;

public enum SunflowerRotation implements StringRepresentable {
    MORNING("morning"),
    DAY("day"),
    EVENING("evening"),
    NIGHT("night");

    private final String name;

    SunflowerRotation(String string2) {
        this.name = string2;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}

