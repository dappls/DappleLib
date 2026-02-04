package net.dappls.dapplelib.datagen.enchantment;


import net.dappls.dapplelib.Dapplelib;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import java.util.ArrayList;
import java.util.List;

public class DappleLibEnchantmentHelper {
    public static List<DappleLibEnchantmentBuilder> customEnchantmentList = new ArrayList<>();

    public static void registerEnchantment(DappleLibEnchantmentBuilder enchantment) {
        customEnchantmentList.add(enchantment);
    }



    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        for (DappleLibEnchantmentBuilder enchantment : customEnchantmentList) {
            Enchantment.Builder builder = Enchantment.builder(
                    Enchantment.definition(
                            items.getOrThrow(enchantment.applicableItems),
                            enchantment.rarityWeight,
                            enchantment.maxlevel,
                            Enchantment.leveledCost(enchantment.minCostBase, enchantment.minCostPerLevel),
                            Enchantment.leveledCost(enchantment.maxCostBase, enchantment.maxCostPerLevel),
                            enchantment.anvilCost,
                            enchantment.attributeModifierSlot
                    )
            );

            enchantment.incompatibleWith.ifPresent(tag ->
                    builder.exclusiveSet(enchantments.getOrThrow(tag))
            );

            for (DappleLibEffectBuilder effect : enchantment.effectBuilders) {
                builder.addEffect(
                        effect.componentType,
                        effect.source,
                        effect.target,
                        effect.effect);
            }


            register(registerable,
                    RegistryKey.of(RegistryKeys.ENCHANTMENT,
                            Identifier.of(enchantment.getModId(), enchantment.getName())),
                    builder);
        }
    }



    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}