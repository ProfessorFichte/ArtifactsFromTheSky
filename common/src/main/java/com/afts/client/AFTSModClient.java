package com.afts.client;

import com.afts.item.ArmorSets;
import mod.azure.azurelibarmor.common.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.common.render.armor.AzArmorRendererRegistry;
import net.spell_engine.api.item.armor.Armor;

import java.util.function.Supplier;

public class AFTSModClient {
    public static void  init(){
        registerArmorRenderer(ArmorSets.dragonArmorSet.armorSet(), CustomArmorRenderer::dragonArmorRenderer);
    }
    private static void registerArmorRenderer(Armor.Set set, Supplier<AzArmorRenderer> armorRendererSupplier) {
        AzArmorRendererRegistry.register(armorRendererSupplier, set.head, set.chest, set.legs, set.feet);
    }
}
