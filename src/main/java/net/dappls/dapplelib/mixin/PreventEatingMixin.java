package net.dappls.dapplelib.mixin;

import net.dappls.dapplelib.events.PreventEatingEvent;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PreventEatingMixin {
    @Inject(method = "canConsume", at = @At("RETURN"), cancellable = true)
    public void canConsume(boolean ignoreHunger, CallbackInfoReturnable<Boolean> cir) {
        if(PreventEatingEvent.EVENT.invoker().preventsEating(
                (PlayerEntity) (Object) this).get())
        {
            cir.setReturnValue(false);
        }
    }
}
