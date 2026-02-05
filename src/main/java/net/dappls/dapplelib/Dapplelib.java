package net.dappls.dapplelib;

import net.dappls.dapplelib.autoscreen.ScreenKit;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Dapplelib implements ModInitializer {
    public static final String MOD_ID = "dapplelib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Library!");
    }
}
