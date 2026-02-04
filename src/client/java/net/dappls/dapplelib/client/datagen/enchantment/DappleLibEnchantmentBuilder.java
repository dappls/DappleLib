package net.dappls.dapplelib.client.datagen.enchantment;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DappleLibEnchantmentBuilder {
    private final String modID;
    private final String name; //name of enchantment
    TagKey<Item> applicableItems = ItemTags.WEAPON_ENCHANTABLE; //What can the enchantment be added to
    int rarityWeight = 1; //rarity of enchantment
    int maxlevel = 1; //max level of enchantment
    int minCostBase = 1;
    int minCostPerLevel = 1;
    int maxCostBase = 1;
    int maxCostPerLevel = 1;
    int anvilCost = 1; //Level cost to apply enchantment in anvil
    AttributeModifierSlot attributeModifierSlot = AttributeModifierSlot.MAINHAND; //when does enchantment apply attributes
    Optional<TagKey<Enchantment>> incompatibleWith = Optional.empty(); //what other enchantments is this one incompatable with
    List<DappleLibEffectBuilder> effectBuilders = new ArrayList<>(); //effects of enchantment




    public DappleLibEnchantmentBuilder(String modID, String name) {
        this.modID = modID;
        this.name = name;

    }
    public String getModId() {
        return this.modID;
    }

    public DappleLibEnchantmentBuilder itemTags(TagKey<Item> itemTags) {
        this.applicableItems = itemTags;
        return this;
    }
    public DappleLibEnchantmentBuilder baseLevelCost1(int baselevelcost1) {
        this.minCostBase = baselevelcost1;
        return this;
    }
    public DappleLibEnchantmentBuilder perLevelCost1(int perlevelcost1) {
        this.minCostPerLevel = perlevelcost1;
        return this;
    }

    public DappleLibEnchantmentBuilder baseLevelCOst2(int baselevelcost2) {
        this.maxCostBase = baselevelcost2;
        return this;
    }

    public DappleLibEnchantmentBuilder perLevvelCost2(int perlevelcost2) {
        this.maxCostPerLevel = perlevelcost2;
        return this;
    }

    public DappleLibEnchantmentBuilder anvilCost(int anvilCost) {
        this.anvilCost = (anvilCost*2);
        return this;
    }

    public DappleLibEnchantmentBuilder maxLevel(int maxlevel) {
        this.maxlevel = maxlevel;
        return this;
    }

    public DappleLibEnchantmentBuilder attributeModifier(AttributeModifierSlot attributeModifierSlot) {
        this.attributeModifierSlot = attributeModifierSlot;
        return this;
    }

    public DappleLibEnchantmentBuilder exclusiveTag(Optional<TagKey<Enchantment>> exclusiveTag) {
        this.incompatibleWith = exclusiveTag;
        return this;
    }

    public DappleLibEnchantmentBuilder addEffect(DappleLibEffectBuilder effect) {
        this.effectBuilders.add(effect);
        return this;
    }


    public String getName() {
        return this.name;
    }
}





