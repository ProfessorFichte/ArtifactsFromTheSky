package com.afts.item;

import com.afts.content.SetBonuses;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.spell_engine.api.item.weapon.SpellSwordItem;
import net.spell_engine.api.spell.SpellDataComponents;

public class VoidDevourerItem extends SpellSwordItem {
    public VoidDevourerItem(ToolMaterial material, Item.Settings settings) {
        super(material, applyComponents(settings));
    }

    private static Item.Settings applyComponents(Item.Settings settings) {
        return settings.component(
                SpellDataComponents.EQUIPMENT_SET,
                SetBonuses.dragonSet.id()
        );
    }
}