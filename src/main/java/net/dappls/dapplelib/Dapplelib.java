package net.dappls.dapplelib;

import net.dappls.dapplelib.chunk.BlockTransformAction;
import net.dappls.dapplelib.chunk.ModifyChunks;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dapplelib implements ModInitializer {
    public static final String MOD_ID = "dapplelib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModifyChunks.modifyChunk(new BlockTransformAction(null,false,
                Blocks.GRASS_BLOCK,Blocks.BONE_BLOCK.getDefaultState()));



        ModifyChunks.init();
        LOGGER.info("Initializing Library!");

    }
}
