package net.dappls.dapplelib.mixin;

import net.dappls.dapplelib.util.CustomPlayerAttackItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class CustomAttackMixin extends LivingEntity {

    @Shadow
    public abstract float getAttackCooldownProgress(float time);

    protected CustomAttackMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "attack", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackCooldownProgress(F)F"))

    public void DappleLib$CustomAttack(Entity target, CallbackInfo ci) {
        if(getAttackCooldownProgress(0.5F) > 0.9F) {
            if(this.getMainHandStack().getItem() instanceof CustomPlayerAttackItem customPlayerAttackItem) {
                if(target instanceof LivingEntity) {
                    for(StatusEffectInstance effect : customPlayerAttackItem.effectsAfterAttack()) {
                        ((LivingEntity) target).addStatusEffect(effect);
                    }
                }
                customPlayerAttackItem.spawnParticleAfterAttack((PlayerEntity) (Object) this, target);
                customPlayerAttackItem.applySoundAfterAttack((PlayerEntity) (Object) this);
            }
        }
    }
}
