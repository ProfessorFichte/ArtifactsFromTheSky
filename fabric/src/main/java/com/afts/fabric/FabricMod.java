package com.afts.fabric;

import net.fabricmc.api.ModInitializer;

import com.afts.AFTSMod;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        AFTSMod.init();
        AFTSMod.registerItems();
        AFTSMod.registerEffects();
    }
}
