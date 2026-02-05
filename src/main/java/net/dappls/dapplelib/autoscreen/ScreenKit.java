package net.dappls.dapplelib.autoscreen;

public final class ScreenKit {

    private ScreenKit() {}

    public static void scan(String basePackage, String modId) {
        var discovered = ScreenDiscovery.discover(basePackage, modId);
        ScreenRegistries.registerDiscovered(discovered);
    }
}
