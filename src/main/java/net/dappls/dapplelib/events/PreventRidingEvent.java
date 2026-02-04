package net.dappls.dapplelib.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.Entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public interface PreventRidingEvent {
    Event<PreventRidingEvent> EVENT = EventFactory.createArrayBacked(PreventRidingEvent.class, events ->(entity) ->{
        List<PreventRidingEvent> sortedlist = new ArrayList<>(
                Arrays.asList(events)
        );
        sortedlist.sort(Comparator.comparingInt(PreventRidingEvent::getPriority));

        for(PreventRidingEvent event: sortedlist) {
            TriState state = event.preventRiding(entity);
            if(state != TriState.DEFAULT) {
                return state;
            }
        }
        return TriState.DEFAULT;
    });


    default int getPriority() {
        return 1000;
    }

    TriState preventRiding(Entity entity);
}
