package net.dappls.dapplelib.mixin;

import net.dappls.dapplelib.events.PreventRidingEvent;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class PreventRidingMixin {

    @Inject(method = "startRiding*", at = @At("HEAD"),cancellable = true)
    public final void startRiding(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(PreventRidingEvent.EVENT.invoker().preventRiding((Entity) (Object) this).get()) {
            cir.cancel();
        }
    }
}
