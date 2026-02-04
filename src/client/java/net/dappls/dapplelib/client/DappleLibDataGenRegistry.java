package net.dappls.dapplelib.client;

import net.dappls.dapplelib.client.datagen.loot.DappleLibLootTableProvider;
import net.dappls.dapplelib.client.datagen.tags.DappleLibBlockTagProvider;
import net.dappls.dapplelib.client.datagen.tags.DappleLibItemTagProvider;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.registry.RegistryWrapper;


public interface DappleLibDataGenRegistry {
    default void registerRecipes(RecipeGenerator gen) {}
    default void registerItemModels(ItemModelGenerator gen) {}
    default void registerBlockModels(BlockStateModelGenerator gen) {}
    default void registerBlockTags(DappleLibBlockTagProvider provider, RegistryWrapper.WrapperLookup wrapperLookup) {}
    default void registerItemTags(DappleLibItemTagProvider provider, RegistryWrapper.WrapperLookup wrapperLookup) {}
    default void registerLootTables(DappleLibLootTableProvider provider) {}
    default void registerEnchantments() {}
}
