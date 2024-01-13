package com.ninni.spawn.entity.variant;

import com.ninni.spawn.entity.Clam;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.item.DyeColor;

import java.util.function.IntFunction;

public class ClamVariant {

    public record Variant(BaseColor baseColor, Pattern pattern, DyeColor dyeColor) {

        public Variant(BaseColor baseColor, Pattern pattern, DyeColor dyeColor) {
            this.baseColor = baseColor;
            this.pattern = pattern;
            this.dyeColor = dyeColor;
        }

        public int getPackedId() {
            return Clam.packVariant(this.baseColor, this.pattern, this.dyeColor);
        }


        public BaseColor baseColor() {
            return this.baseColor;
        }

        public Pattern pattern() {
            return this.pattern;
        }

        public DyeColor dyeColor() {
            return this.dyeColor;
        }
    }

    public enum Pattern {
        NO_PATTERN(0, "no_pattern"),
        STRIPED(1, "striped"),
        DOTTED(2, "dotted"),
        TILED(3, "tiled"),
        RIDGED(4, "ridged");

        private static final IntFunction<Pattern> BY_ID = ByIdMap.sparse(Pattern::getId, values(), NO_PATTERN);
        final int id;
        private final String name;

        Pattern(int j, String name) {
            this.id = j;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String patternName() {
            return name;
        }

        public static Pattern byId(int i) {
            return BY_ID.apply(i);
        }


        public Component patternDisplayName() {
            return Component.translatable("entity.spawn.clam.pattern." + this.name);
        }
    }

    public enum BaseColor {
        WBLUE(0, Base.WEDGE_SHELL, "blue"),
        WBROWN(1, Base.WEDGE_SHELL, "brown"),
        WPURPLE(2, Base.WEDGE_SHELL, "purple"),
        WYELLOW(3, Base.WEDGE_SHELL, "yellow"),
        SPURPLE(4, Base.SCALLOP, "purple"),
        SRED(5, Base.SCALLOP, "red"),
        SWHITE(6, Base.SCALLOP, "white"),
        SYELLOW(7, Base.SCALLOP, "yellow"),
        GCYAN(8, Base.GIANT_CLAM, "cyan"),
        GGREEN(9, Base.GIANT_CLAM, "green"),
        GORANGE(10, Base.GIANT_CLAM, "orange"),
        GPURPLE(11, Base.GIANT_CLAM, "purple");

        private static final IntFunction<BaseColor> BY_ID = ByIdMap.sparse(BaseColor::getId, values(), WBLUE);
        final int id;
        final Base base;
        private final String name;

        BaseColor(int j, Base base, String name) {
            this.id = j;
            this.name = name;
            this.base = base;
        }

        public int getId() {
            return id;
        }

        public Base base() {
            return base;
        }

        public String baseName() {
            return base.name;
        }

        public String colorName() {
            return name;
        }

        public static BaseColor byId(int i) {
            return BY_ID.apply(i);
        }

        public Component colorDisplayName() {
            return Component.translatable("entity.spawn.clam.color." + this.name);
        }

        public Component baseDisplayName() {
            return Component.translatable("entity.spawn.clam.base." + this.base.name);
        }
    }

    public enum Base {
        WEDGE_SHELL(0, "wedge_shell"),
        SCALLOP(1, "scallop"),
        GIANT_CLAM(2, "giant_clam");

        final int id;
        private final String name;

        Base(int j, String name) {
            this.id = j;
            this.name = name;
        }
    }
}
