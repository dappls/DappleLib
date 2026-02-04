package net.dappls.dapplelib.client;

import net.dappls.dapplelib.client.autoscreen.ScreenRegistrar;
import net.fabricmc.api.ClientModInitializer;

public class DapplelibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register screens with the client
        ScreenRegistrar.registerScreens();
    }
}
