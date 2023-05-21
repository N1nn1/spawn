package com.ninni.spawn.entity.variant;

import com.mojang.serialization.Codec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum SeahorseVariant implements StringRepresentable {
    BLACK(0, "black"),
    BLUE(1, "blue"),
    ORANGE(2, "orange"),
    PURPLE(3, "purple"),
    WHITE(4, "white"),
    YELLOW(5, "yellow");

    private static final IntFunction<SeahorseVariant> BY_ID;
    public static final Codec<SeahorseVariant> CODEC;
    final int id;
    private final String name;

    SeahorseVariant(int j, String string2) {
        this.id = j;
        this.name = string2;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public int id() {
        return this.id;
    }

    public static SeahorseVariant byId(int i) {
        return BY_ID.apply(i);
    }

    static {
        BY_ID = ByIdMap.sparse(SeahorseVariant::id, SeahorseVariant.values(), ORANGE);
        CODEC = StringRepresentable.fromEnum(SeahorseVariant::values);
    }
}