package net.dappls.dapplelib.mixin;

import net.dappls.dapplelib.events.PreventFallDamageEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class, AbstractHorseEntity.class})
public abstract class PreventFallDamageMixin extends Entity { //Abstract to not implement methods and extending superclass of both classes trying to mix into

    public PreventFallDamageMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    public void preventfallDamage(double fallDistance, float damagePerDistance, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if(PreventFallDamageEvent.EVENT.invoker()
                .preventsFallDamage(getEntityWorld(),
                        (LivingEntity) (Object) this,
                        fallDistance,
                        damagePerDistance,
                        damageSource)
                .get()) //this changes the tristate into a boolean
        {
            cir.cancel();
        }
    }

}
