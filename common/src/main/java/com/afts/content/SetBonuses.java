package com.afts.content;

import com.afts.item.ArmorSets;
import com.afts.item.Weapons;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.set.EquipmentSet;
import net.spell_engine.api.spell.container.SpellContainerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.afts.AFTSMod.MOD_ID;

public class SetBonuses {
    private static final String NAMESPACE = MOD_ID;
    public record Entry(Identifier id, String title, Supplier<List<Identifier>> itemSupplier, List<EquipmentSet.Bonus> bonuses) { }
    public static final List<Entry> all = new ArrayList<>();
    private static Entry add(Entry entry) {
        all.add(entry);
        return entry;
    }

    private static AttributeModifiersComponent attribute(RegistryEntry<EntityAttribute> attribute, double value, EntityAttributeModifier.Operation operation, Identifier id) {
        return new AttributeModifiersComponent(
                List.of(
                        new AttributeModifiersComponent.Entry(
                                attribute,
                                new EntityAttributeModifier(
                                        id,
                                        value,
                                        operation
                                ),
                                AttributeModifierSlot.MAINHAND)
                ),
                true
        );
    }
    static Supplier<List<Identifier>> dragon = () -> {
        List<Identifier> items = new ArrayList<>(ArmorSets.dragonArmorSet.armorSet().pieceIds());
        items.add(Weapons.void_devourer.id());
        return items;
    };
    public static Entry dragonSet = add(dragonSet());
    private static Entry dragonSet() {
        var id = Identifier.of(NAMESPACE, "dragon_set");
        return new Entry(id,
                "DRAGON SET BONUS",
                dragon,
                List.of(
                        /*EquipmentSet.Bonus.withAttributes(1, attribute(
                                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                0.1F,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                                id)
                        ),
                         */
                        EquipmentSet.Bonus.withSpells(2, SpellContainerHelper.createForModifier(Abilities.space_rupture.id())),
                        EquipmentSet.Bonus.withSpells(4, SpellContainerHelper.createForModifier(Abilities.void_slam.id())),
                        EquipmentSet.Bonus.withSpells(5, SpellContainerHelper.createForModifier(Abilities.improved_dragons_wrath.id()))
                )
        );
    }
}
