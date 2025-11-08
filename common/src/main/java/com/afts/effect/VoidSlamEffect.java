package com.afts.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchools;

import static com.afts.AFTSMod.MOD_ID;

public class VoidSlamEffect extends StatusEffect {
    protected VoidSlamEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    public boolean applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if(livingEntity.isOnGround()){
            livingEntity.removeStatusEffect(AFTSEffects.VOID_SLAM.entry);
            livingEntity.removeStatusEffect(AFTSEffects.VOID_SLAM_JUMP.entry);
            if(livingEntity instanceof PlayerEntity playerEntity && !playerEntity.getWorld().isClient()){
                RegistryEntry<Spell> helper_impact = SpellRegistry.from(playerEntity.getWorld()).getEntry(Identifier.of(MOD_ID, "void_slam_landing_impact")).get();
                ParticleHelper.sendBatches(playerEntity, helper_impact.value().release.particles);
                ParticleHelper.sendBatches(playerEntity, helper_impact.value().release.particles_scaled_with_ranged);
                for(Entity target : TargetHelper.targetsFromArea(playerEntity, helper_impact.value().range,helper_impact.value().target.area, target ->{ return target != playerEntity;})) {
                    SpellHelper.performImpacts(playerEntity.getWorld(), playerEntity, target, playerEntity, helper_impact,
                            helper_impact.value().impacts, new SpellHelper.ImpactContext().power(SpellPower.getSpellPower(SpellSchools.ARCANE, playerEntity)).position(playerEntity.getPos()));
                    ParticleHelper.sendBatches(target, helper_impact.value().impacts.get(0).particles);
                }
            }

        }
        return true;
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
