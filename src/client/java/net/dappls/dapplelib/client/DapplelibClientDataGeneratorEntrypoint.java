package net.dappls.dapplelib.client;

import net.dappls.dapplelib.client.datagen.lang.DappleLibLangProvider;
import net.dappls.dapplelib.client.datagen.loot.DappleLibLootTableProvider;
import net.dappls.dapplelib.client.datagen.model.DappleLibModelProvider;
import net.dappls.dapplelib.client.datagen.recipe.DappleLibRecipeProvider;
import net.dappls.dapplelib.client.datagen.tags.DappleLibBlockTagProvider;
import net.dappls.dapplelib.client.datagen.tags.DappleLibItemTagProvider;
import net.dappls.dapplelib.client.datagen.RegistryDataGenerator;
import net.dappls.dapplelib.client.datagen.enchantment.DappleLibEnchantmentHelper;
import net.dappls.dapplelib.client.datagen.worldgen.DappleLibWorldgenRegistry;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DapplelibClientDataGeneratorEntrypoint implements DataGeneratorEntrypoint {

    // Populated in buildRegistry() (called first), consumed in onInitializeDataGenerator()
    private List<DappleLibWorldgenRegistry> worldgenRegistries;

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        // Standard Providers
        pack.addProvider(RegistryDataGenerator::new);
        pack.addProvider(DappleLibRecipeProvider::new);
        pack.addProvider(DappleLibModelProvider::new);
        pack.addProvider(DappleLibBlockTagProvider::new);
        pack.addProvider(DappleLibItemTagProvider::new);
        pack.addProvider(DappleLibLootTableProvider::new);
        pack.addProvider(DappleLibLangProvider::new);

        // Load entrypoints
        List<DappleLibDataGenRegistry> registries = FabricLoader.getInstance()
                .getEntrypoints("dapplelib-datagen", DappleLibDataGenRegistry.class);

        registries.forEach(dappleLibDataGenRegistry ->
                dappleLibDataGenRegistry.onInitializeDataGenerator(fabricDataGenerator, pack));

        // Capture for use in the lambda below
        List<DappleLibWorldgenRegistry> capturedWorldgen = this.worldgenRegistries;

        pack.addProvider((output, registriesFuture) -> new FabricDynamicRegistryProvider(output, registriesFuture) {
            @Override
            public void configure(RegistryWrapper.@NotNull WrapperLookup wrapperLookup, @NotNull Entries entries) {
                if (capturedWorldgen == null || capturedWorldgen.isEmpty()) return;

                var cfgWrapper = wrapperLookup.getOrThrow(RegistryKeys.CONFIGURED_FEATURE);
                var placedWrapper = wrapperLookup.getOrThrow(RegistryKeys.PLACED_FEATURE);

                // Explicitly add only the entries we registered — avoids addAll() which
                // either filters by the lib's own namespace or dumps all vanilla entries.
                for (DappleLibWorldgenRegistry modRegistry : capturedWorldgen) {
                    for (var def : modRegistry.configuredFeatures()) {
                        entries.add(cfgWrapper,
                                RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, def.id()));
                    }
                    for (var def : modRegistry.placedFeatures()) {
                        entries.add(placedWrapper,
                                RegistryKey.of(RegistryKeys.PLACED_FEATURE, def.id()));
                    }
                }
            }

            @Override
            public String getName() {
                return "DappleLib Worldgen Features";
            }
        });
    }

    @Override
    public void buildRegistry(@NotNull RegistryBuilder registryBuilder) {
        List<DappleLibDataGenRegistry> registries = FabricLoader.getInstance()
                .getEntrypoints("dapplelib-datagen", DappleLibDataGenRegistry.class);

        // Build and store so onInitializeDataGenerator() can reference the same list
        worldgenRegistries = new java.util.ArrayList<>();
        for (DappleLibDataGenRegistry entry : registries) {
            DappleLibWorldgenRegistry modWorldgen = new DappleLibWorldgenRegistry(entry.modID());
            entry.registerWorldgen(modWorldgen);
            worldgenRegistries.add(modWorldgen);
        }

        // Bootstrap Configured Features
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, context -> {
            for (DappleLibWorldgenRegistry modRegistry : worldgenRegistries) {
                for (var def : modRegistry.configuredFeatures()) {
                    context.register(
                            RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, def.id()),
                            def.supplier().get()
                    );
                }
            }
        });

        // Bootstrap Placed Features
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, context -> {
            var lookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
            for (DappleLibWorldgenRegistry modRegistry : worldgenRegistries) {
                for (var def : modRegistry.placedFeatures()) {
                    var cfgKey = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, def.configuredId());
                    context.register(
                            RegistryKey.of(RegistryKeys.PLACED_FEATURE, def.id()),
                            new net.minecraft.world.gen.feature.PlacedFeature(lookup.getOrThrow(cfgKey), def.modifiers())
                    );
                }
            }
        });

        // Enchantments
        registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, DappleLibEnchantmentHelper::bootstrap);
    }
}