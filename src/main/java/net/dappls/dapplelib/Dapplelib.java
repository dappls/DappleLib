package net.dappls.dapplelib;

import net.dappls.dapplelib.autoscreen.ScreenKit;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dapplelib implements ModInitializer {
    public static final String MOD_ID = "dapplelib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Library!");
        ScreenKit.scan("net.dappls", MOD_ID);

    }
}
