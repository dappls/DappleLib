package net.dappls.dapplelib.client;

import net.dappls.dapplelib.client.datagen.loot.DappleLibLootTableProvider;
import net.dappls.dapplelib.client.datagen.model.DappleLibModelProvider;
import net.dappls.dapplelib.client.datagen.recipe.DappleLibRecipeProvider;
import net.dappls.dapplelib.client.datagen.tags.DappleLibBlockTagProvider;
import net.dappls.dapplelib.client.datagen.tags.DappleLibItemTagProvider;
import net.dappls.dapplelib.client.datagen.RegistryDataGenerator;
import net.dappls.dapplelib.client.datagen.enchantment.DappleLibEnchantmentHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

import java.util.List;

public class DapplelibClientDataGeneratorEntrypoint implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(RegistryDataGenerator::new);
        pack.addProvider(DappleLibRecipeProvider::new);
        pack.addProvider(DappleLibModelProvider::new);
        pack.addProvider(DappleLibBlockTagProvider::new);
        pack.addProvider(DappleLibItemTagProvider::new);
        pack.addProvider(DappleLibLootTableProvider::new);
        List<DappleLibDataGenRegistry> registries = FabricLoader.getInstance()
                .getEntrypoints("dapplelib-datagen", DappleLibDataGenRegistry.class);
        registries.forEach(dappleLibDataGenRegistry -> dappleLibDataGenRegistry.onInitializeDataGenerator(fabricDataGenerator,pack));
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        List<DappleLibDataGenRegistry> registries = FabricLoader.getInstance()
                .getEntrypoints("dapplelib-datagen", DappleLibDataGenRegistry.class);
        registries.forEach(dappleLibDataGenRegistry -> dappleLibDataGenRegistry.buildRegistry(registryBuilder));

        List<DappleLibDataGenRegistry> registries2 = FabricLoader.getInstance()
                .getEntrypoints("dapplelib-datagen", DappleLibDataGenRegistry.class);
        registries2.forEach(DappleLibDataGenRegistry::registerEnchantments);
        registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, DappleLibEnchantmentHelper::bootstrap);
    }
}
