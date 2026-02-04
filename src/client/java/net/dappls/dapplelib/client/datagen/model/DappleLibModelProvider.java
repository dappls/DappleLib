package net.dappls.dapplelib.client.datagen.model;

import net.dappls.dapplelib.Dapplelib;
import net.dappls.dapplelib.client.DappleLibDataGenRegistry;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;

import java.util.List;

public class DappleLibModelProvider extends FabricModelProvider {

    public DappleLibModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        List<DappleLibDataGenRegistry> registries = FabricLoader.getInstance()
                .getEntrypoints("dapplelib-datagen", DappleLibDataGenRegistry.class);

        if (registries.isEmpty()) {
            Dapplelib.LOGGER.warn("No DappleLibDataGenRegistry implementations found. " +
                    "Create a class implementing DappleLibDataGenRegistry and register it as a 'dapplelib-datagen' entrypoint in your fabric.mod.json");
            return;
        }

        registries.forEach(registry -> registry.registerBlockModels(blockStateModelGenerator));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        List<DappleLibDataGenRegistry> registries = FabricLoader.getInstance()
                .getEntrypoints("dapplelib-datagen", DappleLibDataGenRegistry.class);

        if (registries.isEmpty()) {
            return;
        }

        registries.forEach(registry -> registry.registerItemModels(itemModelGenerator));
    }
}
