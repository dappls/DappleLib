package net.dappls.dapplelib.client.datagen;

import net.dappls.dapplelib.client.datagen.enchantment.DappleLibEnchantmentBuilder;
import net.dappls.dapplelib.client.datagen.enchantment.DappleLibEnchantmentHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class RegistryDataGenerator extends FabricDynamicRegistryProvider {

    public RegistryDataGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
        RegistryWrapper.Impl<Enchantment> enchantmentRegistry =
                wrapperLookup.getOrThrow(RegistryKeys.ENCHANTMENT);
        for (DappleLibEnchantmentBuilder enchantment : DappleLibEnchantmentHelper.customEnchantmentList) {
            RegistryKey<Enchantment> key = RegistryKey.of(
                    RegistryKeys.ENCHANTMENT,
                    Identifier.of(enchantment.getModId(), enchantment.getName())
            );
            entries.add(enchantmentRegistry, key);
        }
    }

    @Override
    public String getName() {
        return "DappleLib enchantments";
    }
}


