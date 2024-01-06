package com.ninni.spawn.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

public class SpawnSurfaceRules {
    private static final SurfaceRules.RuleSource ALGAL_SAND = makeStateRule(SpawnBlocks.ALGAL_SAND);
    private static final SurfaceRules.RuleSource SAND = makeStateRule(Blocks.SAND);
    private static final SurfaceRules.RuleSource SANDSTONE = makeStateRule(Blocks.SANDSTONE);

    public static SurfaceRules.RuleSource makeRules() {

        //TODO why the FUCK does this not work
        SurfaceRules.RuleSource algalSand = SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, CaveSurface.FLOOR), ALGAL_SAND);
        SurfaceRules.RuleSource sand = SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(3, false, CaveSurface.FLOOR), SAND);
        SurfaceRules.RuleSource sandstone = SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(8, false, CaveSurface.FLOOR), SANDSTONE);
        SurfaceRules.RuleSource blocksSequence = SurfaceRules.sequence(algalSand, sand, sandstone);
        SurfaceRules.RuleSource waterOnlylBlockSequence = SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.waterStartCheck(0, 0)), blocksSequence);

        SurfaceRules.RuleSource seagrassMeadow = SurfaceRules.ifTrue(SurfaceRules.isBiome(SpawnBiomes.SEAGRASS_MEADOW), waterOnlylBlockSequence);

        return SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, seagrassMeadow));
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}