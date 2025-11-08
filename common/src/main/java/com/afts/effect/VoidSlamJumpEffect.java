package com.afts.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.Vec3d;

public class VoidSlamJumpEffect extends StatusEffect {
    protected VoidSlamJumpEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.fallDistance > 0) {
            entity.fallDistance = 0;
        }
        if(!entity.hasStatusEffect(AFTSEffects.VOID_SLAM.entry) && !entity.isOnGround()){
            entity.addStatusEffect(new StatusEffectInstance(AFTSEffects.VOID_SLAM.entry,300,0,false,false,false));
        }
        return true;
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        Vec3d velocity = entity.getVelocity();
        entity.setVelocity(velocity.x, 0.0, velocity.z);
        entity.addVelocity(0.0, 1.5F, 0.0);
        entity.velocityModified = true;
    }
}
