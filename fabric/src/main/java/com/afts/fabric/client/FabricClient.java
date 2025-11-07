package com.afts.fabric.client;

import com.afts.client.AFTSModClient;
import net.fabricmc.api.ClientModInitializer;

public final class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AFTSModClient.init();
    }
}
