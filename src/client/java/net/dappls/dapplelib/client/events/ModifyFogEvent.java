package net.dappls.dapplelib.client.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@FunctionalInterface
public interface ModifyFogEvent {
    Event<ModifyFogEvent> EVENT = EventFactory.createArrayBacked(ModifyFogEvent.class,
            events -> (color, builder) -> {
                List<ModifyFogEvent> sortedEvents = new ArrayList<>(Arrays.asList(events));
                sortedEvents.sort(Comparator.comparingInt(ModifyFogEvent::getPriority));
                for (ModifyFogEvent event : sortedEvents) {
                    return builder;
                }
                return null;
            });

    default int getPriority() {
        return 1000;
    }

    FogBuilder getFogBuilder(Vec3d color,FogBuilder fogBuilder);


    final class FogBuilder {
        private float environmentalStart;
        private float environmentalEnd;
        private float renderDistanceStart;
        private float renderDistanceEnd;
        private float skyEnd;
        private float cloudEnd;

        public FogBuilder(float environmentalStart, float environmentalEnd, float renderDistanceStart, float renderDistanceEnd, float skyEnd, float cloudEnd) {
            this.environmentalStart = environmentalStart;
            this.environmentalEnd = environmentalEnd;
            this.renderDistanceStart = renderDistanceStart;
            this.renderDistanceEnd = renderDistanceEnd;
            this.skyEnd = skyEnd;
            this.cloudEnd = cloudEnd;
        }

        public float getEnvironmentalStart() { return environmentalStart; }
        public float getEnvironmentalEnd() { return environmentalEnd; }
        public float getRenderDistanceStart() { return renderDistanceStart; }
        public float getRenderDistanceEnd() { return renderDistanceEnd; }
        public float getSkyEnd() { return skyEnd; }
        public float getCloudEnd() { return cloudEnd; }

        public FogBuilder setEnvironmentalStart(float v) { this.environmentalStart = v; return this; }
        public FogBuilder setEnvironmentalEnd(float v) { this.environmentalEnd = v; return this; }
        public FogBuilder setRenderDistanceStart(float v) { this.renderDistanceStart = v; return this; }
        public FogBuilder setRenderDistanceEnd(float v) { this.renderDistanceEnd = v; return this; }
        public FogBuilder setSkyEnd(float v) { this.skyEnd = v; return this; }
        public FogBuilder setCloudEnd(float v) { this.cloudEnd = v; return this; }
    }
}
