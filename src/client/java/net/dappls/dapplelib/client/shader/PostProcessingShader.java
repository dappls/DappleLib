package net.dappls.dapplelib.client.shader;

import net.minecraft.util.Identifier;

public class PostProcessingShader {
    private final Identifier identifier;

    public PostProcessingShader(String shader) {
        this.identifier = Identifier.of("bundle", "shaders/post/" + shader);
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
