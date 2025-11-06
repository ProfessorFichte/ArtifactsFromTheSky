package com.afts.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.function.Function;

import static com.afts.AFTSMod.MOD_ID;

public class MaterialItems {
    public static class Container { Item item; }

    public record Entry(Identifier id, String translation, Function<Item.Settings, Item> factory,
                        Item.Settings settings, Container container) {
        public Entry(Identifier id, String translation, Function<Item.Settings, Item> factory, Item.Settings settings) {
            this(id, translation,factory, settings, new Container());
        }
        public Item item() { return container.item; }
    }

    public static final ArrayList<Entry> ENTRIES = new ArrayList<>();
    public static Entry add(Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    public static final Entry voids_eye = add(new Entry(
            Identifier.of(MOD_ID, "voids_eye"),
            "Voids Eye",
            Item::new,
            new Item.Settings().rarity(Rarity.EPIC).fireproof()
    ));
    public static final Entry null_ingot = add(new Entry(
            Identifier.of(MOD_ID, "null_ingot"),
            "Null Ingot",
            Item::new,
            new Item.Settings().rarity(Rarity.EPIC).fireproof()
    ));
    public static final Entry dragon_scales = add(new Entry(
            Identifier.of(MOD_ID, "dragon_scales"),
            "Ender Dragon Scales",
            Item::new,
            new Item.Settings().rarity(Rarity.RARE).fireproof()
    ));


    public static void register() {
        for (Entry e : ENTRIES) {
            Item item = e.factory().apply(e.settings());
            e.container.item = item;
            Registry.register(Registries.ITEM, e.id(), item);
        }
        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register(content -> {
            for (Entry e : ENTRIES) {
                content.add(e.item());
            }
        });
    }
}
