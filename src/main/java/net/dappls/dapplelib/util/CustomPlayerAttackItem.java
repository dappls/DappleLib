package net.dappls.dapplelib.util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public interface CustomPlayerAttackItem {
    void applySoundAfterAttack(PlayerEntity player);
    List<StatusEffectInstance> effectsAfterAttack();
    void spawnParticleAfterAttack(PlayerEntity player, Entity target);

}
