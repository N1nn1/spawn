package com.ninni.spawn.entity.variant;

import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum HamsterVariant implements StringRepresentable {
    GOLDEN(0, "golden"),
    ROBOROWSKI(1, "roborowski"),
    RUSSIAN(2, "russian"),
    TURKISH(3, "turkish");

    private static final IntFunction<HamsterVariant> BY_ID = ByIdMap.sparse(HamsterVariant::id, HamsterVariant.values(), RUSSIAN);
    final int id;
    private final String name;

    HamsterVariant(int j, String string2) {
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

    public static HamsterVariant byId(int i) {
        return BY_ID.apply(i);
    }
}