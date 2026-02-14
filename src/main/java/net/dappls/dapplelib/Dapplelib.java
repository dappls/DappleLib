package net.dappls.dapplelib;

import net.dappls.dapplelib.chunk.ModifyChunks;
import net.dappls.dapplelib.worldgen.DappleLibBiomeRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dapplelib implements ModInitializer {
    public static final String MOD_ID = "dapplelib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModifyChunks.init();

        FabricLoader.getInstance()
                .getEntrypoints("dapplelib-biome", DappleLibRuntimeRegistry.class)
                .forEach(entry -> {
                    DappleLibBiomeRegistry biomeReg = new DappleLibBiomeRegistry(entry.modID());
                    entry.registerBiomeFeatures(biomeReg);
                    biomeReg.apply();
                });

        LOGGER.info("Initializing Library!");
    }
}
