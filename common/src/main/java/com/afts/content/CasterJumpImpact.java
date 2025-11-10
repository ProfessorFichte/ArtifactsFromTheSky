package com.afts.content;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.event.SpellHandlers;
import net.spell_engine.internals.SpellHelper;
import net.spell_power.api.SpellPower;

public class CasterJumpImpact implements SpellHandlers.CustomImpact {

    @Override
    public SpellHandlers.ImpactResult onSpellImpact(
            RegistryEntry<Spell> spell,
            SpellPower.Result powerResult,
            LivingEntity caster,
            Entity target,
            SpellHelper.ImpactContext context
    ) {
        float range = spell.value().range / 5.0F;
        Vec3d velocity = caster.getVelocity();
        caster.setVelocity(velocity.x, 0.0, velocity.z);
        caster.addVelocity(0.0, range, 0.0);
        caster.velocityModified = true;
        if (!caster.getWorld().isClient) {
            float dashStrength = spell.value().range / 4;
            Vec3d look = caster.getRotationVector().normalize();
            double yVel = caster.getVelocity().y > 0 ? caster.getVelocity().y : 0.25;
            Vec3d dashVelocity = new Vec3d(
                    look.x * dashStrength,
                    yVel,
                    look.z * dashStrength
            );
            caster.setVelocity(dashVelocity);
            caster.velocityModified = true;
            caster.velocityDirty = true;
        }

        return new SpellHandlers.ImpactResult(true, false);
    }
}
