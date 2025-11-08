package com.afts;

import com.afts.config.Default;
import com.afts.content.CustomSpellImpacts;
import com.afts.effect.AFTSEffects;
import com.afts.item.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.spell_engine.api.config.ConfigFile;
import net.tiny_config.ConfigManager;

public final class AFTSMod {
    public static final String MOD_ID = "afts";

    public static ConfigManager<ConfigFile.Equipment> itemConfig = new ConfigManager<>
            ("equipment", Default.itemConfig)
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();
    public static ConfigManager<ConfigFile.Effects> effectConfig = new ConfigManager<>
            ("effects", new ConfigFile.Effects())
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();

    public static void init() {
        itemConfig.refresh();
        effectConfig.refresh();
        CustomSpellImpacts.registerCustomImpacts();
    }
    public static void registerItems() {
        Group.AFTS = FabricItemGroup.builder()
                .icon(() -> new ItemStack(Weapons.void_devourer.item().asItem()))
                .displayName(Text.translatable("itemGroup.afts.generic"))
                .build();
        MaterialItems.register();
        SmithingTemplates.register();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.AFTS);
        Weapons.register(itemConfig.value.weapons);
        ArmorSets.register(itemConfig.value.armor_sets);
        itemConfig.save();

    }
    public static void registerEffects() {
        AFTSEffects.register(effectConfig.value);
        effectConfig.save();
    }
}
