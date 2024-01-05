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

        SurfaceRules.RuleSource ruleSource = SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, CaveSurface.FLOOR), ALGAL_SAND);
        SurfaceRules.RuleSource ruleSource2 = SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.waterBlockCheck(0, 0)), ruleSource);

        //SurfaceRules.RuleSource ruleSource3 = SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(-6, false, CaveSurface.FLOOR), SANDSTONE);
        //SurfaceRules.RuleSource ruleSource4 = SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.waterBlockCheck(0, 0)), ruleSource3);

        //SurfaceRules.RuleSource ruleSource3 = SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(2, true, CaveSurface.FLOOR), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SANDSTONE), SAND));
        //SurfaceRules.RuleSource ruleSource4 = SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.waterStartCheck(0, 0)), ruleSource3);

        //SurfaceRules.RuleSource ruleSource8 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SANDSTONE), SAND);
        //SurfaceRules.RuleSource ruleSource5 = SurfaceRules.ifTrue(SurfaceRules.VERY_DEEP_UNDER_FLOOR, SANDSTONE);

        //SurfaceRules.RuleSource ruleSource3 = SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(4, true, CaveSurface.FLOOR), SANDSTONE);
        //SurfaceRules.RuleSource ruleSource4 = SurfaceRules.ifTrue(SurfaceRules.waterStartCheck(-6, -1), ruleSource5);

        SurfaceRules.RuleSource ruleSource6 = SurfaceRules.sequence(ruleSource2);

        SurfaceRules.RuleSource seagrassMeadow = SurfaceRules.ifTrue(SurfaceRules.isBiome(SpawnBiomes.SEAGRASS_MEADOW), ruleSource6);

        return SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, seagrassMeadow));
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}