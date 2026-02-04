package net.dappls.dapplelib.client.datagen.tags;

import net.dappls.dapplelib.client.DappleLibDataGenRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.tag.TagKey;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DappleLibBlockTagProvider extends FabricTagProvider<Block> {

    public DappleLibBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BLOCK, registriesFuture);
    }

    /**
     * Get the tag builder for a tag key.
     */
    public TagBuilder tag(TagKey<Block> tag) {
        return getTagBuilder(tag);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        List<DappleLibDataGenRegistry> registries = FabricLoader.getInstance()
                .getEntrypoints("dapplelib-datagen", DappleLibDataGenRegistry.class);

        if (registries.isEmpty()) {
            return;
        }

        registries.forEach(registry -> registry.registerBlockTags(this, wrapperLookup));
    }
}
