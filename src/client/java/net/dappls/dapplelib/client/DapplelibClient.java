package net.dappls.dapplelib.client;

import net.dappls.dapplelib.client.events.ModifyFogEvent;
import net.dappls.dapplelib.client.shader.ShaderRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;

public class DapplelibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WorldRenderEvents.END_MAIN.register(context -> {
            float tickDelta = context.gameRenderer().getClient().getRenderTickCounter().getTickProgress(true);
            ShaderRegistry.renderAll(tickDelta);
        });
    }
}









