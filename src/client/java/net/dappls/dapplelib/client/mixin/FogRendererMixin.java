package net.dappls.dapplelib.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.dappls.dapplelib.client.events.ModifyFogEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class FogRendererMixin  {
    @Inject(method = "applyFog(Lnet/minecraft/client/render/Camera;ILnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/fog/FogRenderer;applyFog(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V",
            shift = At.Shift.BEFORE)
    )
    private void DL$modifyFog(Camera camera,
                              int viewDistance,
                              RenderTickCounter renderTickCounter,
                              float f,
                              ClientWorld clientWorld,
                              CallbackInfoReturnable<org.joml.Vector4f> cir,
                              @Local Vector4f vanillaColor,
                              @Local FogData fogData) {


        ModifyFogEvent.FogBuilder builder = new ModifyFogEvent.FogBuilder(
                vanillaColor,
                fogData.environmentalStart,
                fogData.environmentalEnd,
                fogData.renderDistanceStart,
                fogData.renderDistanceEnd,
                fogData.skyEnd,
                fogData.cloudEnd
        );
        ModifyFogEvent.FogBuilder result = ModifyFogEvent.EVENT.invoker().getFogBuilder(builder);

        if (result == null) { return; }

        vanillaColor.x = result.getColor().x;
        vanillaColor.y = result.getColor().y;
        vanillaColor.z = result.getColor().z;
        fogData.environmentalStart = result.getEnvironmentalStart();
        fogData.environmentalEnd = result.getEnvironmentalEnd();
        fogData.renderDistanceStart = result.getRenderDistanceStart();
        fogData.renderDistanceEnd = result.getRenderDistanceEnd();
        fogData.skyEnd = result.getSkyEnd();
        fogData.cloudEnd = result.getCloudEnd();

    }
}