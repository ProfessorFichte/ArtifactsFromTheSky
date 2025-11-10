package com.afts.content;

import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.event.SpellHandlers;

import static com.afts.AFTSMod.MOD_ID;

public class CustomSpellImpacts {

    public static void registerCustomImpacts(){
        SpellHandlers.registerCustomImpact(
                Identifier.of(MOD_ID, "void_slam"),
                new VoidSlamKnockUpImpact()
        );
        SpellHandlers.registerCustomImpact(
                Identifier.of(MOD_ID, "caster_jump"),
                new CasterJumpImpact()
        );
    }
}
