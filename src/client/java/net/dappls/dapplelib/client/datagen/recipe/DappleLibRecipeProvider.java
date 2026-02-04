package net.dappls.dapplelib.client.datagen.recipe;

import net.dappls.dapplelib.Dapplelib;
import net.dappls.dapplelib.client.DappleLibDataGenRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.registry.RegistryWrapper;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DappleLibRecipeProvider extends FabricRecipeProvider {

    public DappleLibRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected @NonNull RecipeGenerator getRecipeGenerator(RegistryWrapper.@NonNull WrapperLookup registryLookup, @NonNull RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                List<DappleLibDataGenRegistry> registries = FabricLoader.getInstance()
                        .getEntrypoints("dapplelib-datagen", DappleLibDataGenRegistry.class);

                if (registries.isEmpty()) {
                    Dapplelib.LOGGER.warn("No DappleLibDataGenRegistry implementations found.");
                    return;
                }

                registries.forEach(registry -> registry.registerRecipes(this));
            }
        };
    }

    @Override
    public String getName() {
        return "DappleLib Recipe Provider";
    }
}
