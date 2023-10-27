package com.ninni.spawn.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;


import static com.ninni.spawn.Spawn.MOD_ID;

public class SpawnCriterionTrigger extends SimpleCriterionTrigger<SpawnCriterionTrigger.TriggerInstance> {
    private final ResourceLocation ID;

    public SpawnCriterionTrigger(String id) {
        ID = new ResourceLocation(MOD_ID, id);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, conditions -> true);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected TriggerInstance createInstance(JsonObject jsonObject, ContextAwarePredicate contextAwarePredicate, DeserializationContext deserializationContext) {
        return new TriggerInstance(ID, contextAwarePredicate);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        public TriggerInstance(ResourceLocation resourceLocation, ContextAwarePredicate contextAwarePredicate) {
            super(resourceLocation, contextAwarePredicate);
        }
    }
}