package com.afts.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.WeaponConfig;
import net.spell_engine.api.item.Equipment;
import net.spell_engine.api.item.armor.Armor;
import net.spell_engine.api.item.weapon.SpellSwordItem;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

import static com.afts.AFTSMod.MOD_ID;

public class Weapons {
    public static final ArrayList<Weapon.Entry> entries = new ArrayList<>();

    private static Weapon.Entry entry(String name, Weapon.CustomMaterial material, Weapon.Factory factory, WeaponConfig defaults, Equipment.WeaponType category) {
        var entry = new Weapon.Entry(MOD_ID, name, material, factory, defaults, category);
        if (entry.isRequiredModInstalled()) {
            entries.add(entry);
        }
        return entry;
    }

    private static Supplier<Ingredient> ingredient(String idString, boolean requirement, Item fallback) {
        var id = Identifier.of(idString);
        if (requirement) {
            return () -> {
                return Ingredient.ofItems(fallback);
            };
        } else {
            return () -> {
                var item = Registries.ITEM.get(id);
                var ingredient = item != null ? item : fallback;
                return Ingredient.ofItems(ingredient);
            };
        }
    }

    private static Armor.ItemSettingsTweaker commonSettings(Identifier equipmentSetId) {
        return Armor.ItemSettingsTweaker.standard(itemSettings -> {
            itemSettings
                    .component(SpellDataComponents.EQUIPMENT_SET, equipmentSetId)
                    .component(DataComponentTypes.RARITY, Rarity.RARE);
        });
    }

    private static final float sword_attack_speed = -2.4F;
    private static Weapon.Entry sword(String name, Weapon.CustomMaterial material, float damage) {
        return entry(name, material, SpellSwordItem::new, new WeaponConfig(damage, sword_attack_speed), Equipment.WeaponType.SWORD);
    }
    private static Weapon.Entry void_devourer(String name, Weapon.CustomMaterial material, float damage) {
        return entry(name, material, VoidDevourerItem::new, new WeaponConfig(damage, sword_attack_speed), Equipment.WeaponType.SWORD);
    }

    public static final Weapon.Entry fotv = sword("fotv",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.ENDER_PEARL)), 7.0F)
            .attribute(AttributeModifier.bonus(SpellSchools.ARCANE.id, 2.0F))
            .translatedName("Fragment of the Void");
    public static final Weapon.Entry void_devourer = void_devourer("void_devourer",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.DRAGON_BREATH)), 7.5F)
            .attribute(AttributeModifier.bonus(SpellSchools.ARCANE.id, 3.5F))
            .translatedName("Void Devourer");

    static {
        entries.forEach(entry -> entry.rarity = Rarity.RARE);
    }

    /// REGISTRY
    public static void register(Map<String, WeaponConfig> configs) {
        Weapon.register(configs, entries, Group.KEY);
    }
}
