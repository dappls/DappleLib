package net.dappls.dapplelib.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.dappls.dapplelib.client.events.ModifyFogEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.fog.FogRenderer;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.nio.ByteBuffer;

@Mixin(FogRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class FogRendererMixin {
    @WrapOperation(
            method = "applyFog(Lnet/minecraft/client/render/Camera;ILnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/fog/FogRenderer;applyFog(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V")
    )
    private void DL$modifyFog(
            FogRenderer instance,
            ByteBuffer buffer,
            int bufPos,
            Vector4f color,
            float environmentalStart,
            float environmentalEnd,
            float renderDistanceStart,
            float renderDistanceEnd,
            float skyEnd,
            float cloudEnd,
            Operation<Void> original
    ) {
        ModifyFogEvent.FogBuilder builder = new ModifyFogEvent.FogBuilder(
                color,
                environmentalStart,
                environmentalEnd,
                renderDistanceStart,
                renderDistanceEnd,
                skyEnd,
                cloudEnd
        );
        ModifyFogEvent.FogBuilder result = ModifyFogEvent.EVENT.invoker().getFogBuilder(builder);

        if (result != null) {
            Vector4f c = result.getColor();
            if (c != null) color.set(c.x, c.y, c.z, c.w);
            original.call(instance, buffer, bufPos, color,
                    result.getEnvironmentalStart(),
                    result.getEnvironmentalEnd(),
                    result.getRenderDistanceStart(),
                    result.getRenderDistanceEnd(),
                    result.getSkyEnd(),
                    result.getCloudEnd());
        } else {
            original.call(instance, buffer, bufPos, color,
                    environmentalStart, environmentalEnd,
                    renderDistanceStart, renderDistanceEnd,
                    skyEnd, cloudEnd);
        }
    }
}