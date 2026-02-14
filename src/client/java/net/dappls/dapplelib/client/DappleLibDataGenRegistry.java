package net.dappls.dapplelib.client;

import net.dappls.dapplelib.client.datagen.loot.DappleLibLootTableProvider;
import net.dappls.dapplelib.client.datagen.tags.DappleLibBlockTagProvider;
import net.dappls.dapplelib.client.datagen.tags.DappleLibItemTagProvider;
import net.dappls.dapplelib.client.datagen.worldgen.DappleLibWorldgenRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryWrapper;


public interface DappleLibDataGenRegistry {

    String modID();

    void registerRecipes(RecipeGenerator gen);

    void registerItemModels(ItemModelGenerator gen);

    void registerBlockModels(BlockStateModelGenerator gen);

    void registerBlockTags(DappleLibBlockTagProvider provider, RegistryWrapper.WrapperLookup wrapperLookup);

    void registerItemTags(DappleLibItemTagProvider provider, RegistryWrapper.WrapperLookup wrapperLookup);

    void registerLootTables(DappleLibLootTableProvider provider);

    void registerLang(RegistryWrapper.WrapperLookup wrapperLookup, FabricLanguageProvider.TranslationBuilder translationBuilder);

    void registerEnchantments();

    void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator, FabricDataGenerator.Pack pack);

    void buildRegistry(RegistryBuilder registryBuilder);

    void registerWorldgen(DappleLibWorldgenRegistry provider);

}
