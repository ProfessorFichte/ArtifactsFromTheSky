package com.afts.item;

import com.afts.content.SetBonuses;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.spell_engine.api.config.ArmorSetConfig;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.item.Equipment;
import net.spell_engine.api.item.armor.Armor;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static com.afts.AFTSMod.MOD_ID;

public class ArmorSets {
    public static final ArrayList<Armor.Entry> entries = new ArrayList<>();
    private static Armor.Entry create(RegistryEntry<ArmorMaterial> material, Identifier id, int durability, int tier,
                                      Armor.Set.ItemFactory factory, ArmorSetConfig defaults, Armor.ItemSettingsTweaker settings) {
        var entry = Armor.Entry.create(
                material,
                id,
                durability,
                factory,
                defaults,
                Equipment.LootProperties.of(tier),
                settings
        );
        entries.add(entry);
        return entry;
    }

    public static RegistryEntry<ArmorMaterial> material(
            String name, int protectionHead, int protectionChest, int protectionLegs, int protectionFeet,
            int enchantability, RegistryEntry<SoundEvent> equipSound, Supplier<Ingredient> repairIngredient) {

        var material = new ArmorMaterial(
                Map.of(
                        ArmorItem.Type.HELMET, protectionHead,
                        ArmorItem.Type.CHESTPLATE, protectionChest,
                        ArmorItem.Type.LEGGINGS, protectionLegs,
                        ArmorItem.Type.BOOTS, protectionFeet),
                enchantability, equipSound, repairIngredient,
                List.of(new ArmorMaterial.Layer(Identifier.of(MOD_ID, name))),
                0,0
        );
        return Registry.registerReference(Registries.ARMOR_MATERIAL, Identifier.of(MOD_ID, name), material);
    }
    private static final Supplier<Ingredient> DRAGON_INGREDIENTS = () -> Ingredient.ofItems(
            Items.DRAGON_BREATH, Items.AMETHYST_SHARD
    );

    private static Armor.ItemSettingsTweaker commonSettings(Identifier equipmentSetId) {
        return Armor.ItemSettingsTweaker.standard(itemSettings -> {
            itemSettings
                    .component(SpellDataComponents.EQUIPMENT_SET, equipmentSetId)
                    .component(DataComponentTypes.RARITY, Rarity.RARE);
        });
    }

    public static Identifier dragon_set_passive = Identifier.of(MOD_ID, "dragon_set");


    private static final float dragonKnockbackRes = 0.5F;
    private static final float dragonAttackDmg = 0.05F;
    private static final float dragonSpellPower = 0.025F;
    private static final float dragonManaRegen = 1.0F;


    public static RegistryEntry<ArmorMaterial> material_dragon = material(
            "dragon",
            3, 7, 4, 4,
            18,
            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, DRAGON_INGREDIENTS);


    private static final Identifier ATTACK_DAMAGE_ID = Identifier.ofVanilla("generic.attack_damage");
    private static final Identifier KNOCKBACK_ID = Identifier.ofVanilla("generic.knockback_resistance");
    private static final Identifier MANA_REGEN_ID = Identifier.of("manaattributes:generic.mana_regeneration");

    private static AttributeModifier damageMultiplier(float value) {
        return new AttributeModifier(
                ATTACK_DAMAGE_ID.toString(),
                value,
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }
    private static AttributeModifier knockbackResAdd(float value) {
        return new AttributeModifier(
                KNOCKBACK_ID.toString(),
                value,
                EntityAttributeModifier.Operation.ADD_VALUE);
    }
    private static AttributeModifier manaRegMultiplier(float value) {
        return new AttributeModifier(
                MANA_REGEN_ID.toString(),
                value,
                EntityAttributeModifier.Operation.ADD_VALUE);
    }

    public static final Armor.Entry dragonArmorSet = create(
            material_dragon,
            Identifier.of(MOD_ID, "dragon"),
            40,
            10,
            Armor.CustomItem::new,
            ArmorSetConfig.with(
                    new ArmorSetConfig.Piece(material_dragon.value().getProtection(ArmorItem.Type.HELMET))
                            .add(damageMultiplier(dragonAttackDmg))
                            .add(knockbackResAdd(dragonKnockbackRes))
                            .add(AttributeModifier.multiply(SpellSchools.GENERIC.id, dragonSpellPower))
                            .add(manaRegMultiplier(dragonManaRegen)),
                    new ArmorSetConfig.Piece(material_dragon.value().getProtection(ArmorItem.Type.CHESTPLATE))
                            .add(damageMultiplier(dragonAttackDmg))
                            .add(knockbackResAdd(dragonKnockbackRes))
                            .add(AttributeModifier.multiply(SpellSchools.GENERIC.id, dragonSpellPower))
                            .add(manaRegMultiplier(dragonManaRegen)),
                    new ArmorSetConfig.Piece(material_dragon.value().getProtection(ArmorItem.Type.LEGGINGS))
                            .add(damageMultiplier(dragonAttackDmg))
                            .add(knockbackResAdd(dragonKnockbackRes))
                            .add(AttributeModifier.multiply(SpellSchools.GENERIC.id, dragonSpellPower))
                            .add(manaRegMultiplier(dragonManaRegen)),
                    new ArmorSetConfig.Piece(material_dragon.value().getProtection(ArmorItem.Type.BOOTS))
                            .add(damageMultiplier(dragonAttackDmg))
                            .add(knockbackResAdd(dragonKnockbackRes))
                            .add(AttributeModifier.multiply(SpellSchools.GENERIC.id, dragonSpellPower))
                            .add(manaRegMultiplier(dragonManaRegen))
            ),
            commonSettings(SetBonuses.dragonSet.id()) )
            .translatedName("", "", "", "");

    public static void register(Map<String, ArmorSetConfig> configs) {
        Armor.register(configs, entries, Group.KEY);
    }

}
