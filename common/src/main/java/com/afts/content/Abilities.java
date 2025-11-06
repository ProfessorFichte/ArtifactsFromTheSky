package com.afts.content;

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

    /// ACTIVES
    public static final Entry fotv = add(fotv());
    private static Entry fotv() {
        var id = Identifier.of(MOD_ID, "fotv");
        var title = "Fragment of the Void";
        var description = "Teleport through space {teleport_distance} blocks forwards.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;
        spell.tier = 1;

        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_PURPLE.asString();
        spell.tooltip.description.show_in_compact = true;

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

    public static final Entry charged_space_rupture = add(charged_space_rupture());
    private static Entry charged_space_rupture() {
        var id = Identifier.of(MOD_ID, "charged_space_rupture");
        var title = "Charged Space Rupture";
        var description = "";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;
        spell.tier = 4;

        SpellBuilder.Cost.exhaust(spell, 0.4F);
        SpellBuilder.Cost.cooldown(spell, 20);
        return new Entry(id, spell, title, description, null);
    }
    public static final Entry void_rush = add(void_rush());
    private static Entry void_rush() {
        var id = Identifier.of(MOD_ID, "void_rush");
        var title = "Void Rush";
        var description = "";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;
        spell.tier = 4;

        SpellBuilder.Cost.exhaust(spell, 0.4F);
        SpellBuilder.Cost.cooldown(spell, 20);
        return new Entry(id, spell, title, description, null);
    }

    ///MODIFIER
    public static final Entry space_rupture = add(space_rupture());
    private static Entry space_rupture() {
        var id = Identifier.of(MOD_ID, "space_rupture");
        var title = "Space Rupture";
        var description = "";

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;
        spell.range = 2.5F;
        spell.tier = 4;

        var damage = SpellBuilder.Impacts.damage(0.5F, 0.8F);
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.FROST,
                                SpellEngineParticles.MagicParticles.Motion.BURST
                        ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        30, 0.2F, 0.7F)
                        .color(Color.ARCANE.toRGBA())
        };
        spell.impacts = List.of(damage);

        var area_impact = new Spell.AreaImpact();
        area_impact.execute_action_type = Spell.Impact.Action.Type.DAMAGE;
        area_impact.radius = 2.0F;
        area_impact.area = new Spell.Target.Area();
        area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        area_impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        "firework",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.15F, 0.15F
                )
        };

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry improved_dragons_wrath = add(improved_dragons_wrath());
    private static Entry improved_dragons_wrath() {
        var id = Identifier.of(MOD_ID, "improved_dragons_wrath");
        var title = "Improved Dragon's Wrath";
        var description = "";

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;
        spell.range = 2.5F;
        spell.tier = 4;

        var damage = SpellBuilder.Impacts.damage(0.5F, 0.8F);
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.FROST,
                                SpellEngineParticles.MagicParticles.Motion.BURST
                        ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        30, 0.2F, 0.7F)
                        .color(Color.ARCANE.toRGBA())
        };
        spell.impacts = List.of(damage);

        var area_impact = new Spell.AreaImpact();
        area_impact.execute_action_type = Spell.Impact.Action.Type.DAMAGE;
        area_impact.radius = 2.0F;
        area_impact.area = new Spell.Target.Area();
        area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        area_impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        "firework",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.15F, 0.15F
                )
        };

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
