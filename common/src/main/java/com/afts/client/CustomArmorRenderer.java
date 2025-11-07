package com.afts.client;

import mod.azure.azurelibarmor.common.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.common.render.armor.AzArmorRendererConfig;
import net.minecraft.util.Identifier;

import static com.afts.AFTSMod.MOD_ID;

public class CustomArmorRenderer extends AzArmorRenderer {

    public static CustomArmorRenderer dragonArmorRenderer() {
        return new CustomArmorRenderer("dragon_armor", "dragon_armor");
    }
    public CustomArmorRenderer(String modelName, String textureName) {
        super(AzArmorRendererConfig.builder(
                Identifier.of(MOD_ID, "geo/" + modelName + ".geo.json"),
                Identifier.of(MOD_ID, "textures/armor/" + textureName + ".png")
        ).build());
    }
}
