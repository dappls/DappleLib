package net.dappls.dapplelib.client.datagen.loot;

import net.dappls.dapplelib.client.DappleLibDataGenRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DappleLibLootTableProvider extends FabricBlockLootTableProvider {

    public DappleLibLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        List<DappleLibDataGenRegistry> registries = FabricLoader.getInstance()
                .getEntrypoints("dapplelib-datagen", DappleLibDataGenRegistry.class);

        if (registries.isEmpty()) {
            return;
        }

        registries.forEach(registry -> registry.registerLootTables(this));
    }
}
