package net.dappls.dapplelib.autoscreen;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public final class RegisteredScreen {

    private final Identifier id;
    private final ScreenHandlerType<?> type;
    private final Class<? extends ScreenHandler> handlerClass;
    private final String screenClassName;

    public RegisteredScreen(
            Identifier id,
            ScreenHandlerType<?> type,
            Class<? extends ScreenHandler> handlerClass,
            String screenClassName
    ) {
        this.id = id;
        this.type = type;
        this.handlerClass = handlerClass;
        this.screenClassName = screenClassName;
    }

    public Identifier id() {
        return id;
    }

    public ScreenHandlerType<?> type() {
        return type;
    }

    public Class<? extends ScreenHandler> handlerClass() {
        return handlerClass;
    }

    public String screenClassName() {
        return screenClassName;
    }
}

