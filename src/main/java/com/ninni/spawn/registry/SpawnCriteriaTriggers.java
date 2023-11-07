package com.ninni.spawn.registry;

import com.ninni.spawn.advancements.SpawnCriterionTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class SpawnCriteriaTriggers {

    public static final SpawnCriterionTrigger INTERACT_WITH_ANGLER_FISH = CriteriaTriggers.register(new SpawnCriterionTrigger("interact_with_angler_fish"));
    public static final SpawnCriterionTrigger HATCH_ANT = CriteriaTriggers.register(new SpawnCriterionTrigger("hatch_ant"));
    public static final SpawnCriterionTrigger OPEN_HAMSTER_INVENTORY = CriteriaTriggers.register(new SpawnCriterionTrigger("open_hamster_inventory"));
    public static final SpawnCriterionTrigger GOT_STUCK_IN_MUCUS = CriteriaTriggers.register(new SpawnCriterionTrigger("got_stuck_in_mucus"));
    public static final SpawnCriterionTrigger WENT_THROUGH_GHOSTLY_MUCUS = CriteriaTriggers.register(new SpawnCriterionTrigger("went_through_ghostly_mucus"));

}