package com.afts.content;

import com.afts.effect.AFTSEffects;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_engine.internals.target.SpellTarget;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;

import static com.afts.AFTSMod.MOD_ID;

public class Abilities {

    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable SpellTooltip.DescriptionMutator mutator) { }
    public static final List<Entry> entries = new ArrayList<>();
    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    private static Spell createModifierAlikePassiveSpell() {
        var spell = SpellBuilder.createSpellPassive();
        spell.range = 0;
        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_activation = false;
        return spell;
    }

    /// ACTIVES
    public static final Entry vorpal_leap = add(vorpal_leap());
    private static Entry vorpal_leap() {
        var id = Identifier.of(MOD_ID, "vorpal_leap");
        var title = "Vorpal Leap";
        var description = "Teleport through space {teleport_distance} blocks forwards.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;
        spell.tier = 1;

        SpellBuilder.Casting.instant(spell);

        spell.release = new Spell.Release();
        spell.release.animation = "spell_engine:one_handed_area_release";
        spell.release.sound = new Sound(Identifier.of("minecraft", "entity.enderman.teleport"));


        var teleport = new Spell.Impact();
        teleport.action = new Spell.Impact.Action();
        teleport.action.type = Spell.Impact.Action.Type.TELEPORT;
        teleport.action.teleport = new Spell.Impact.Action.Teleport();
        teleport.action.teleport.mode = Spell.Impact.Action.Teleport.Mode.FORWARD;
        teleport.action.teleport.forward = new Spell.Impact.Action.Teleport.Forward();
        teleport.action.teleport.forward.distance = 10F;
        teleport.action.teleport.depart_particles = new ParticleBatch[] {
                new ParticleBatch(
                        "minecraft:portal",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        40, 0.1F, 0.3F)
                        .preSpawnTravel(1)
        };
        teleport.particles = new ParticleBatch[] {
                new ParticleBatch(
                        "minecraft:portal",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        40, 0.1F, 0.3F)
                        .invert()
                        .preSpawnTravel(4)
        };
        spell.impacts = List.of(teleport);

        SpellBuilder.Cost.exhaust(spell, 0.4F);
        SpellBuilder.Cost.cooldown(spell, 10);

        return new Entry(id, spell, title, description, null);
    }

    public static Entry dragons_wrath = add(dragons_wrath());
    private static Entry dragons_wrath() {
        var id = Identifier.of(MOD_ID, "dragons_wrath");
        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.tier = 4;
        spell.range = 12;
        var title = "Dragon's Wrath";
        var description = "Furiously blow infernal fire in front of you, leaving a deadly area for {cloud_duration} seconds dealing {damage}.";

        SpellBuilder.Casting.channel(spell, 5, 4);
        spell.active.cast.animation = "afts:vd_flamethrower";
        spell.active.cast.sound = new Sound();
        spell.active.cast.start_sound = new Sound();
        spell.active.cast.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.flame.id().toString(),
                        ParticleBatch.Shape.CONE, ParticleBatch.Origin.LAUNCH_POINT,
                        ParticleBatch.Rotation.LOOK, 8, 1, 1, 30)
        };

        spell.release = new Spell.Release();
        spell.release.sound = new Sound();

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        spell.target.area.angle_degrees = 40;

        var damage = SpellBuilder.Impacts.damage(0.9F, 0.9F);
        damage.particles = new ParticleBatch[] {
                new ParticleBatch("lava",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        3, 0.5F, 3F)
        };
        damage.sound = new Sound();

        spell.impacts = List.of(damage);

        SpellBuilder.Cost.exhaust(spell, 0.2F);
        SpellBuilder.Cost.cooldown(spell, 10);
        spell.cost.cooldown.proportional = true;

        return new Entry(id, spell, title, description, null);
    }
    //TO DO
    //with chestplate: leap 5~ blocks upwards, slam upwards to 6 blocks (depending on velocity), dealing 7 damage on impact and applying slowness
    //with chestplate and sword (modifier on sword):  leap 6~ blocks upwards, slam upwards to 7 blocks (depending on velocity), dealing 11 damage on impact and applying slowness 2
    public static final Entry void_slam = add(void_slam());
    private static Entry void_slam() {
        var id = Identifier.of(MOD_ID, "void_slam");
        var title = "Void Slam";
        var effect = AFTSEffects.VOID_SLAM_JUMP;
        var description = "Flying high and when landing dealing damage and inflicting weakness to targets.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 5;
        spell.tier = 4;

        spell.target.type = Spell.Target.Type.CASTER;

        var custom = new Spell.Impact();
        custom.action = new Spell.Impact.Action();
        custom.action.custom = new Spell.Impact.Action.Custom();
        custom.action.type = Spell.Impact.Action.Type.CUSTOM;
        custom.action.custom.intent = SpellTarget.Intent.HELPFUL;
        custom.action.custom.handler = "afts:caster_jump";

        var buff = SpellBuilder.Impacts.effectSet(effect.id.toString(),10,0);
        buff.action.status_effect.refresh_duration = false;

        spell.impacts = List.of(buff, custom);

        SpellBuilder.Cost.exhaust(spell, 0.4F);
        SpellBuilder.Cost.cooldown(spell, 20);
        return new Entry(id, spell, title, description, null);
    }
    public static final Entry void_slam_landing_impact = add(void_slam_landing_impact());
    private static Entry void_slam_landing_impact() {
        var id = Identifier.of(MOD_ID, "void_slam_landing_impact");
        var title = "Void Slam Landing";
        var description = "Dealing {damage} damage";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 6;
        spell.tier = 4;

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.vertical_range_multiplier = 0.5F;

        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.BURST
                        ).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.GROUND,
                        50, 0.8F, 1.5F)
                        .color(Color.ARCANE.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.GROUND,
                        50,  0.8F, 1.5F)
        };
        spell.release.particles_scaled_with_ranged = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.area_effect_293.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.GROUND,
                        1, 0, 0)
                        .scale(4.05F)
                        .color(Color.ARCANE.alpha(0.6F).toRGBA())
        };

        var damage = SpellBuilder.Impacts.damage(0.5F, 0.8F);
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10, 0.2F, 0.4F),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.BURST
                        ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        30, 0.2F, 0.5F)
                        .color(Color.ARCANE.toRGBA()),
        };
        var debuff = SpellBuilder.Impacts.effectSet("slowness",5,0);
        debuff.action.status_effect.refresh_duration = false;
        var custom = new Spell.Impact();
        custom.action = new Spell.Impact.Action();
        custom.action.custom = new Spell.Impact.Action.Custom();
        custom.action.type = Spell.Impact.Action.Type.CUSTOM;
        custom.action.custom.intent = SpellTarget.Intent.HARMFUL;
        custom.action.custom.handler = "afts:void_slam";

        spell.impacts = List.of(damage,debuff,custom);

        SpellBuilder.Cost.exhaust(spell, 0.4F);
        SpellBuilder.Cost.cooldown(spell, 20);
        return new Entry(id, spell, title, description, null);
    }

    ///MODIFIER
    public static final Entry space_rupture = add(space_rupture());
    private static Entry space_rupture() {
        var id = Identifier.of(MOD_ID, "space_rupture");
        var title = "Space Rupture";
        var description = "Dealing {damage} damage to targets around you after Vorpal Leaping.";

        var spell = createModifierAlikePassiveSpell();
        spell.school = SpellSchools.ARCANE;
        spell.range = 2.5F;

        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_PURPLE.asString();
        spell.tooltip.description.show_in_compact = true;

        var trigger = SpellBuilder.Triggers.specificSpellCast("afts:vorpal_leap");
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPELL,
                                SpellEngineParticles.MagicParticles.Motion.BURST
                        ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        30, 0.2F, 0.7F)
                        .color(Color.ARCANE.toRGBA()),
        };
        spell.release.particles_scaled_with_ranged = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.area_effect_293.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.GROUND,
                        1, 0, 0)
                        .scale(0.8F)
                        .color(Color.ARCANE.toRGBA())
        };

        var stashEffect = AFTSEffects.SPACE_RUPTURE;
        var stashTrigger = SpellBuilder.Triggers.effectTick(stashEffect.id.toString());
        SpellBuilder.Deliver.stash(spell, stashEffect.id.toString(), 0.75F, List.of(stashTrigger));
        spell.deliver.stash_effect.consume = 0;

        var impact = SpellBuilder.Impacts.damage(0.5F, 0F);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.BURST
                        ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10, 0.2F, 0.5F)
                        .color(Color.ARCANE.toRGBA()),
        };
        spell.impacts = List.of(impact);
        var areaImpact = new Spell.AreaImpact();
        areaImpact.radius = 2F;
        areaImpact.force_indirect = true;
        areaImpact.sound = new Sound();
        spell.area_impact = areaImpact;

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry improved_dragons_wrath = add(improved_dragons_wrath());
    private static Entry improved_dragons_wrath() {
        var id = Identifier.of(MOD_ID, "improved_dragons_wrath");
        var title = "Improved Dragon's Wrath";
        var description = "Dragons Wrath leaves fire puddles behind for {cloud_duration} sec.";

        var spell = createModifierAlikePassiveSpell();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;

        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_PURPLE.asString();
        spell.tooltip.description.show_in_compact = true;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellHit("afts:dragons_wrath");
        spell.passive.triggers = List.of(trigger);

        spell.deliver.type = net.spell_engine.api.spell.Spell.Delivery.Type.CLOUD;
        spell.deliver.delay = 5;
        Spell.Delivery.Cloud cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 2.0F;
        cloud.volume.area.vertical_range_multiplier = 0.3F;
        cloud.volume.sound = new Sound(SpellEngineSounds.GENERIC_FIRE_IMPACT_2.id().toString());
        cloud.impact_tick_interval = 8;
        cloud.time_to_live_seconds = 4;
        cloud.spawn.sound = new Sound(SpellEngineSounds.GENERIC_FIRE_IGNITE.id().toString());
        cloud.client_data = new Spell.Delivery.Cloud.ClientData();
        cloud.client_data.light_level = 15;
        cloud.client_data.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.flame_ground.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        3.0F, 0.0F, 0.0F),
                new ParticleBatch(SpellEngineParticles.flame_medium_a.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        2.0F, 0.02F, 0.1F),
                new ParticleBatch(SpellEngineParticles.flame_medium_b.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        1.0F, 0.02F, 0.1F),
                new ParticleBatch(SpellEngineParticles.flame_spark.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        3.0F, 0.03F, 0.2F)
        };
        spell.deliver.clouds = List.of(cloud);

        var damage = SpellBuilder.Impacts.damage(0.5F, 0F);
        damage.sound = new Sound(SpellEngineSounds.GENERIC_FIRE_IMPACT_1.id().toString());
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.flame.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        20.0F, 0.05F, 0.15F),
                new ParticleBatch(SpellEngineParticles.flame_medium_a.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        20.0F, 0.05F, 0.15F)
        };
        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, 1);
        return new Entry(id, spell, title, description, null);
    }
    public static final Entry improved_void_slam = add(improved_void_slam());
    private static Entry improved_void_slam() {
        var id = Identifier.of(MOD_ID, "improved_void_slam");
        var title = "Improved Void Slam";
        var description = "With Void Slam jump {range_add_1} block higher and knock up targets {range_add_1} block higher. " +
                "The ground landing impact deals {power_multiplier} more damage and inflicts {effect_amplifier_add} higher amplifier of slowness.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;

        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_PURPLE.asString();
        spell.tooltip.description.show_in_compact = true;

        var effectAmplifierAdd = new Spell.Modifier();
        effectAmplifierAdd.spell_pattern = "afts:void_slam_landing_impact";
        effectAmplifierAdd.effect_amplifier_add = 1;
        var rangeAdd = new Spell.Modifier();
        rangeAdd.spell_pattern = "afts:void_slam_landing_impact";
        rangeAdd.range_add = 1;
        var rangeAdd2 = new Spell.Modifier();
        rangeAdd2.spell_pattern = "afts:void_slam";
        rangeAdd2.range_add = 1;
        var powerModifier = new Spell.Modifier();
        powerModifier.spell_pattern =  "afts:void_slam_landing_impact";
        powerModifier.power_modifier = new Spell.Impact.Modifier();
        powerModifier.power_modifier.power_multiplier = 0.3F;

        spell.modifiers = List.of(effectAmplifierAdd,rangeAdd, rangeAdd2);

        return new Entry(id, spell, title, description, null);
    }

    /*
    public static final Entry dragons_wrath = add(dragons_wrath());
    private static Entry dragons_wrath() {
        var id = Identifier.of(MOD_ID, "dragons_wrath");
        var title = "Dragon's Wrath";
        var description = "Furiously blow infernal fire in front of you, leaving a deadly area for {cloud_duration} seconds dealing {damage}.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;
        spell.tier = 4;

        spell.deliver.type = Spell.Delivery.Type.CLOUD;

        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 1.0F;
        cloud.volume.area.vertical_range_multiplier = 0.5F;
        cloud.volume.sound = new Sound("");
        cloud.delay_ticks = 0;
        cloud.impact_tick_interval = 15;
        cloud.time_to_live_seconds = 5;
        cloud.spawn = new Spell.Delivery.Cloud.Spawn();
        cloud.spawn.sound = new Sound("");
        cloud.spawn.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.flame.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        15, 0.1F, 0.5F)
        };
        cloud.client_data = new Spell.Delivery.Cloud.ClientData();
        cloud.client_data.light_level = 15;
        cloud.client_data.particles = new ParticleBatch[] {
                new ParticleBatch(SpellEngineParticles.flame_ground.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        2, 0, 0),
                new ParticleBatch(SpellEngineParticles.flame_medium_a.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        3, 0.02F, 0.3F),
                new ParticleBatch(SpellEngineParticles.flame_medium_b.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        3, 0.01F, 0.35F),
                new ParticleBatch(SpellEngineParticles.flame_spark.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        4, 0.05F, 0.3F),
                new ParticleBatch("campfire_cosy_smoke",
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        0.1F, 0.05F, 0.1F),
        };

        cloud.placement = SpellBuilder.Deliver.placementByLook(1.5f, 0, 0);
        cloud.additional_placements = List.of(
                SpellBuilder.Deliver.placementByLook(3f, 0, 1),
                SpellBuilder.Deliver.placementByLook(4.5f, 0, 2),
                SpellBuilder.Deliver.placementByLook(6f, 0, 3),
                SpellBuilder.Deliver.placementByLook(7.5f, 0, 4),
                SpellBuilder.Deliver.placementByLook(9.0f, 0, 5)
        );

        spell.deliver.clouds = List.of(cloud);

        var damage = SpellBuilder.Impacts.damage(0.2F, 0.0F);
        damage.particles = new ParticleBatch[] {
                new ParticleBatch("smoke",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.01F, 0.1F),
                new ParticleBatch("flame",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10, 0.01F, 0.1F)
        };

        SpellBuilder.Cost.exhaust(spell, 0.4F);
        SpellBuilder.Cost.cooldown(spell, 15);
        return new Entry(id, spell, title, description, null);
    }
     */

}
