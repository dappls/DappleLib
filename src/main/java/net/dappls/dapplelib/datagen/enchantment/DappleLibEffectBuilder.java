package net.dappls.dapplelib.datagen.enchantment;

import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.enchantment.effect.TargetedEnchantmentEffect;

import java.util.List;

public class DappleLibEffectBuilder {
    ComponentType<List<TargetedEnchantmentEffect<EnchantmentEntityEffect>>> componentType;
    EnchantmentEffectTarget source;
    EnchantmentEffectTarget target;
    EnchantmentEntityEffect effect;
    public DappleLibEffectBuilder(ComponentType<List<TargetedEnchantmentEffect<EnchantmentEntityEffect>>> componentType, EnchantmentEffectTarget source, EnchantmentEffectTarget target) {
        this.componentType = componentType;
        this.source = source;
        this.target = target;
    }

    public DappleLibEffectBuilder effects(EnchantmentEntityEffect effect) {
        this.effect = effect;
        return this;
    }

}
