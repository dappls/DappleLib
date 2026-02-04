package net.dappls.dapplelib.client.datagen.lang;

import net.dappls.dapplelib.client.DappleLibDataGenRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.RegistryWrapper;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DappleLibLangProvider extends FabricLanguageProvider {

    public DappleLibLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        List<DappleLibDataGenRegistry> registries = FabricLoader.getInstance()
                .getEntrypoints("dapplelib-datagen", DappleLibDataGenRegistry.class);
        if (registries.isEmpty()) {
            return;
        }
        registries.forEach(registry -> registry.registerLang(wrapperLookup,translationBuilder));
    }
}
