package com.afts.datagen;

import com.afts.content.Abilities;
import com.afts.content.SetBonuses;
import com.afts.item.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntryList;
import net.spell_engine.api.datagen.SpellGenerator;
import net.spell_engine.api.item.set.EquipmentSet;
import net.spell_engine.api.item.set.EquipmentSetRegistry;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;

import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

public class AFTSDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(LangGenerator::new);
        pack.addProvider(SpellGen::new);
        pack.addProvider(ModelProvider::new);
        pack.addProvider(ItemTagGenerator::new);
        pack.addProvider(AFTSRecipes::new);
        pack.addProvider(EquipmentSetGenerator::new);
    }

    public static class LangGenerator extends FabricLanguageProvider {
        protected LangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, "en_us", registryLookup);
        }

        @Override
        public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, FabricLanguageProvider.TranslationBuilder translationBuilder) {
            translationBuilder.add(Group.translationKey, "Artifacts from the Sky");
            Weapons.entries.forEach(entry ->
                    translationBuilder.add(entry.item().getTranslationKey(), entry.translatedName())
            );
            Abilities.entries.forEach(entry -> {
                var id = entry.id();
                translationBuilder.add("spell." + id.getNamespace() + "." + id.getPath() + ".name" , entry.title());
                translationBuilder.add("spell." + id.getNamespace() + "." + id.getPath() + ".description" , entry.description());
            });
            SmithingTemplates.ENTRIES.forEach(entry -> {
                translationBuilder.add(entry.item().get().getTranslationKey(), entry.translations().itemName());
                translationBuilder.add(entry.upgradeTranslationKey(), entry.translations().upgradeName());
                translationBuilder.add(entry.baseSlotDescriptionTranslationKey(), entry.translations().baseSlotDescription());
                translationBuilder.add(entry.additionsSlotDescriptionTranslationKey(), entry.translations().additionsSlotDescription());
                translationBuilder.add(entry.appliesToTranslationKey(), entry.translations().appliesTo());
                translationBuilder.add(entry.ingredientsTranslationKey(), entry.translations().ingredients());
            });
            MaterialItems.ENTRIES.forEach(entry -> {
                var id = entry.id();
                translationBuilder.add("item." + id.getNamespace() + "." + id.getPath(), entry.translation());
            });
            SetBonuses.all.forEach(entry -> {
                translationBuilder.add(EquipmentSet.translationKey(entry.id()), entry.title());
            });
            ArmorSets.entries.forEach(entry -> {
                var translations = new LinkedHashMap<String, String>();
                translations.put(((Item)entry.armorSet().head).getTranslationKey(), entry.armorSet().headTranslation);
                translations.put(((Item)entry.armorSet().chest).getTranslationKey(), entry.armorSet().chestTranslation);
                translations.put(((Item)entry.armorSet().legs).getTranslationKey(), entry.armorSet().legsTranslation);
                translations.put(((Item)entry.armorSet().feet).getTranslationKey(), entry.armorSet().feetTranslation);
                for (var armorEntry: translations.entrySet()) {
                    translationBuilder.add(armorEntry.getKey(), armorEntry.getValue());
                }
            });
        }
    }
    public static class ItemTagGenerator extends RPGSeriesDataGen.ItemTagGenerator {
        public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            var armorTagOptions = new RPGSeriesDataGen.ItemTagGenerator.ArmorOptions(false, false);
            generateArmorTags(
                    ArmorSets.entries.stream().filter(entry -> entry.name().contains("dragon")).toList(),
                    RPGSeriesItemTags.ArmorMetaType.MELEE,
                    armorTagOptions
            );
        }
    }
    public static class SpellGen extends SpellGenerator {
        public SpellGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateSpells(Builder builder) {
            for (var entry: Abilities.entries) {
                builder.add(entry.id(), entry.spell());
            }
        }
    }
    public static class ModelProvider extends FabricModelProvider {
        public ModelProvider(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {
            Weapons.entries.forEach(entry -> {
                itemModelGenerator.register(entry.item(), Models.HANDHELD);
            });
            MaterialItems.ENTRIES.forEach(entry -> {
                itemModelGenerator.register(entry.item(), Models.GENERATED);
            });
            SmithingTemplates.ENTRIES.forEach(entry -> {
                itemModelGenerator.register(entry.item().get(), Models.GENERATED);
            });
            ArmorSets.entries.forEach(entry -> {
                for (var piece: entry.armorSet().pieces()) {
                    itemModelGenerator.register((Item) piece, Models.GENERATED);
                }
            });
        }
    }

    public static class EquipmentSetGenerator extends FabricDynamicRegistryProvider {

        public EquipmentSetGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }
        @Override
        protected void configure(RegistryWrapper.WrapperLookup registries, FabricDynamicRegistryProvider.Entries entries) {
            RegistryEntryLookup<Item> itemLookup = registries.createRegistryLookup().getOrThrow(RegistryKeys.ITEM);
            for (var set: SetBonuses.all) {
                var items = RegistryEntryList.of(
                        set.itemSupplier().get().stream()
                                .map(id -> itemLookup.getOrThrow(RegistryKey.of(RegistryKeys.ITEM, id)))
                                .toList()
                );
                entries.add(
                        RegistryKey.of(EquipmentSetRegistry.KEY, set.id()),
                        new EquipmentSet.Definition(
                                set.id().getPath(),
                                items,
                                set.bonuses()
                        )
                );
            }
        }
        @Override
        public String getName() {
            return "Equipment Set Generator";
        }
    }
}
