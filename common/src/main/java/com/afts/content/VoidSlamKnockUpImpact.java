package com.afts.content;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.event.SpellHandlers;
import net.spell_engine.internals.SpellHelper;
import net.spell_power.api.SpellPower;

public class VoidSlamKnockUpImpact implements SpellHandlers.CustomImpact {

    @Override
    public SpellHandlers.ImpactResult onSpellImpact(
            RegistryEntry<Spell> spell,
            SpellPower.Result powerResult,
            LivingEntity caster,
            Entity target,
            SpellHelper.ImpactContext context
    ) {
        if (!(target instanceof LivingEntity)) return null;
        float range = spell.value().range / 5.0F;
        Vec3d velocity = target.getVelocity();
        target.setVelocity(velocity.x, 0.0, velocity.z);
        target.addVelocity(0.0, range, 0.0);
        target.velocityModified = true;

        return new SpellHandlers.ImpactResult(true, false);
    }
}
