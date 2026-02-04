package net.dappls.dapplelib.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@FunctionalInterface
public interface PreventEatingEvent {
    Event<PreventEatingEvent> EVENT = EventFactory.createArrayBacked(PreventEatingEvent.class,
            events -> (player) -> {
                List<PreventEatingEvent> sortedEvents = new ArrayList<>(
                        Arrays.asList(events)
                );
                sortedEvents.sort(Comparator.comparingInt(PreventEatingEvent::getPriority));
                for (PreventEatingEvent event: sortedEvents) {
                    TriState state = event.preventsEating(player); //gets the state of the current event
                    if (state != TriState.DEFAULT) {
                        return state;
                    }
                }
                return TriState.DEFAULT;
            });


    default int getPriority() {
        return 1000;
    }

    TriState preventsEating(PlayerEntity player);
}
