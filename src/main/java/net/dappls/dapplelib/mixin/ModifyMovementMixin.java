package net.dappls.dapplelib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.dappls.dapplelib.events.ModifyMovementEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class ModifyMovementMixin {
    @WrapOperation(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(DDD)V"))
    private void modifyJumpMovement(LivingEntity instance, double x, double y, double z, Operation<Void> original) {
        Vec3d modified = ModifyMovementEvents.JUMP_VELOCITY.invoker().modify(new Vec3d(x, y, z), instance);
        original.call(instance, modified.getX(), modified.getY(), modified.getZ());
    }

    @ModifyVariable(method = "travel", at = @At("HEAD"), argsOnly = true)
    private Vec3d modifyTravelMovement(Vec3d value) {
        return ModifyMovementEvents.MOVEMENT_VELOCITY.invoker().modify(value, (LivingEntity) (Object) this);
    }
}
