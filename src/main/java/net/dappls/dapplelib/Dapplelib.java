package net.dappls.dapplelib;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dapplelib implements ModInitializer {
    public static final String MOD_ID = "dapplelib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        LOGGER.info("Intializing Library!");
    }
}
