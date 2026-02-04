package net.dappls.dapplelib.autoscreen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a ScreenHandler for automatic registration.
 * Place this on your ScreenHandler class (common side).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Screen {
    /**
     * The registry path for this screen (e.g., "my_screen" becomes "modid:my_screen")
     */
    String id();

    /**
     * The fully qualified class name of the client-side HandledScreen class.
     * Example: "com.yourmod.client.MyScreen"
     */
    String screen();
}
