package com.afts.effect;

import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.spell_engine.api.config.ConfigFile;
import net.spell_engine.api.config.EffectConfig;
import net.spell_engine.api.effect.Effects;
import net.spell_engine.api.effect.Synchronized;
import net.spell_engine.api.effect.TickingStatusEffect;

import java.util.ArrayList;
import java.util.List;

import static com.afts.AFTSMod.MOD_ID;

public class AFTSEffects {
    public static final List<Effects.Entry> entries = new ArrayList<>();
    private static Effects.Entry add(Effects.Entry entry) {
        entries.add(entry);
        return entry;
    }
    public static Effects.Entry SPACE_RUPTURE = add(new Effects.Entry(Identifier.of(MOD_ID, "space_rupture"),
            "Space Rupture",
            "Damaging nearby enemies.",
            new TickingStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff).interval(3),
            new EffectConfig(
                    List.of()
            )
    ));
    public static Effects.Entry VOID_SLAM_JUMP = add(new Effects.Entry(Identifier.of(MOD_ID, "void_slam_jump"),
            "Void Slam Jump",
            "Jumps up and negates fall damage.",
            new VoidSlamJumpEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of()
            )
    ));
    public static Effects.Entry VOID_SLAM = add(new Effects.Entry(Identifier.of(MOD_ID, "void_slam"),
            "Void Slam",
            "Triggers spell impact after landing on the Ground.",
            new VoidSlamEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of()
            )
    ));

    public static void register(ConfigFile.Effects config) {
        for (var entry : entries) {
            Synchronized.configure(entry.effect, true);
        }
        Effects.register(entries, config.effects);

    }

}
