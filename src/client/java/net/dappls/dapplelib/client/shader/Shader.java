package net.dappls.dapplelib.client.shader;


import net.minecraft.util.Identifier;

public class Shader {
    private final Identifier identifier;

    public Shader(String shader) {
        this.identifier = Identifier.of("bundle", shader);
    }

    public Identifier getIdentifier() {
        return identifier;
    }

}
