package net.dappls.dapplelib.autoscreen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ScreenRegistries {

    private static final List<RegisteredScreen> REGISTERED = new ArrayList<>();
    private static final Map<Class<? extends ScreenHandler>, ScreenHandlerType<?>> TYPE_BY_HANDLER = new HashMap<>();

    private ScreenRegistries() {}

    public static void registerDiscovered(List<DiscoveredScreen> discovered) {
        for (DiscoveredScreen d : discovered) {
            registerOne(d);
        }
    }

    private static void registerOne(DiscoveredScreen d) {
        Identifier id = Identifier.of(d.modId(), d.idPath());

        ScreenHandlerType<?> type = createHandlerType(d, id);

        RegisteredScreen reg = new RegisteredScreen(
                id,
                type,
                d.handlerClass(),
                d.screenClassName()
        );

        REGISTERED.add(reg);
        TYPE_BY_HANDLER.put(d.handlerClass(), type);

        System.out.println("[DappleLib] Registered screen handler: " + id + " -> " + d.screenClassName());
    }

    @SuppressWarnings("unchecked")
    private static <T extends ScreenHandler> ScreenHandlerType<T> createHandlerType(DiscoveredScreen d, Identifier id) {
        // We need to create the type first, then use it in the factory
        // Use a holder array to work around lambda effectively final requirement
        final ScreenHandlerType<T>[] typeHolder = new ScreenHandlerType[1];

        ScreenHandlerType.Factory<T> factory = (syncId, playerInv) -> {
            try {
                // Try constructor with ScreenHandlerType first
                try {
                    var ctor = d.handlerClass()
                            .getConstructor(ScreenHandlerType.class, int.class, PlayerInventory.class);
                    return (T) ctor.newInstance(typeHolder[0], syncId, playerInv);
                } catch (NoSuchMethodException e) {
                    // Fall back to constructor without type
                    var ctor = d.handlerClass()
                            .getConstructor(int.class, PlayerInventory.class);
                    return (T) ctor.newInstance(syncId, playerInv);
                }
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to construct handler for " + id, e);
            }
        };

        ScreenHandlerType<T> type = Registry.register(
                Registries.SCREEN_HANDLER,
                id,
                new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES)
        );

        typeHolder[0] = type;
        return type;
    }

    public static List<RegisteredScreen> getRegistered() {
        return List.copyOf(REGISTERED);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ScreenHandler> ScreenHandlerType<T> getType(Class<T> handlerClass) {
        ScreenHandlerType<?> type = TYPE_BY_HANDLER.get(handlerClass);
        if (type == null) {
            throw new IllegalStateException("No registered screen handler type for: " + handlerClass.getName());
        }
        return (ScreenHandlerType<T>) type;
    }

    public static boolean hasType(Class<? extends ScreenHandler> handlerClass) {
        return TYPE_BY_HANDLER.containsKey(handlerClass);
    }
}

