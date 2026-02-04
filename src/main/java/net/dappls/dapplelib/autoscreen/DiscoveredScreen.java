package net.dappls.dapplelib.autoscreen;

import net.minecraft.screen.ScreenHandler;

public final class DiscoveredScreen {

    private final String modId;
    private final String idPath;
    private final Class<? extends ScreenHandler> handlerClass;
    private final String screenClassName;

    public DiscoveredScreen(
            String modId,
            String idPath,
            Class<? extends ScreenHandler> handlerClass,
            String screenClassName
    ) {
        this.modId = modId;
        this.idPath = idPath;
        this.handlerClass = handlerClass;
        this.screenClassName = screenClassName;
    }

    public String modId() {
        return modId;
    }

    public String idPath() {
        return idPath;
    }

    public Class<? extends ScreenHandler> handlerClass() {
        return handlerClass;
    }

    public String screenClassName() {
        return screenClassName;
    }
}
