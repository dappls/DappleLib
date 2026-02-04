package net.dappls.dapplelib.util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public interface CustomPlayerAttackItem {
    /*
    Use to create particles, sounds, etc. Specific methods will be added to reduce boilerplate
    code but otherwise use attackLogic.
    */
    void attackLogic(PlayerEntity player, Entity target);

    default List<StatusEffectInstance> effectsAfterAttack() {
        return List.of();}
}
