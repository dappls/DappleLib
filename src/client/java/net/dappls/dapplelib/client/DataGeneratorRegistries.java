package net.dappls.dapplelib.client;

import net.dappls.dapplelib.Dapplelib;
import net.dappls.dapplelib.client.datagen.loot.DappleLibLootTableProvider;
import net.dappls.dapplelib.client.datagen.tags.DappleLibBlockTagProvider;
import net.dappls.dapplelib.client.datagen.tags.DappleLibItemTagProvider;
import net.dappls.dapplelib.datagen.enchantment.DappleLibEnchantmentBuilder;
import net.dappls.dapplelib.datagen.enchantment.DappleLibEnchantmentHelper;
import net.minecraft.block.Blocks;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

public class DataGeneratorRegistries implements DappleLibDataGenRegistry {

    @Override
    public void registerRecipes(RecipeGenerator gen) {
        // Example: Smelting recipe - cobblestone -> emerald
        gen.offerSmelting(java.util.List.of(Items.COBBLESTONE), RecipeCategory.MISC, Items.EMERALD, 1.0f, 200, "emerald");

        // Example: Blasting recipe - iron ore -> 2 iron ingots
        gen.offerBlasting(java.util.List.of(Items.RAW_IRON), RecipeCategory.MISC, Items.IRON_INGOT, 0.7f, 100, "iron");
    }

    @Override
    public void registerBlockModels(BlockStateModelGenerator gen) {
        // Example: Simple cube block model
        gen.registerSimpleCubeAll(Blocks.DIRT);
    }

    @Override
    public void registerItemModels(ItemModelGenerator gen) {
        // Example: Simple item model
        gen.register(Items.STICK);
    }

    @Override
    public void registerBlockTags(DappleLibBlockTagProvider provider, RegistryWrapper.WrapperLookup wrapperLookup) {
        // Example: Add dirt to pickaxe mineable tag
        provider.getTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(Registries.BLOCK.getId(Blocks.DIRT));

        // Example: Add grass block to needs stone tool tag
        provider.getTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(Registries.BLOCK.getId(Blocks.GRASS_BLOCK));
    }

    @Override
    public void registerItemTags(DappleLibItemTagProvider provider, RegistryWrapper.WrapperLookup wrapperLookup) {
        // Example: Add dirt to beacon payment items
        provider.getTagBuilder(ItemTags.BEACON_PAYMENT_ITEMS)
                .add(Registries.ITEM.getId(Items.DIRT));
    }

    @Override
    public void registerLootTables(DappleLibLootTableProvider provider) {
        // Example: Block drops itself
        provider.addDrop(Blocks.GRASS_BLOCK);

        // Example: Block drops a different item
        provider.addDrop(Blocks.STONE, Items.DIAMOND);
    }

    @Override
    public void registerEnchantments() {
        DappleLibEnchantmentHelper.registerEnchantment(new DappleLibEnchantmentBuilder(Dapplelib.MOD_ID, "test1")
                .anvilCost(12));
    }
}
