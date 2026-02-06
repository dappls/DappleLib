package net.dappls.dapplelib.client.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL20;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ShaderProgram implements AutoCloseable {
    private final int programId;
    private final Map<String, Integer> uniformLocations = new HashMap<>();

    public ShaderProgram(Identifier vertex, Identifier fragment) throws Exception {
        RenderSystem.assertOnRenderThread();

        int vert = compileShader(vertex, GL20.GL_VERTEX_SHADER);
        int frag = compileShader(fragment, GL20.GL_FRAGMENT_SHADER);

        programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vert);
        GL20.glAttachShader(programId, frag);

        // Bind standard attribute locations before linking
        GL20.glBindAttribLocation(programId, 0, "Position");
        GL20.glBindAttribLocation(programId, 1, "TexCoord");

        GL20.glLinkProgram(programId);

        int linked = GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS);
        if (linked == GL20.GL_FALSE) {
            String log = GL20.glGetProgramInfoLog(programId);
            GL20.glDeleteProgram(programId);
            GL20.glDeleteShader(vert);
            GL20.glDeleteShader(frag);
            throw new RuntimeException("Shader link failed: " + log);
        }

        // Cleanup shader objects after linking
        GL20.glDetachShader(programId, vert);
        GL20.glDetachShader(programId, frag);
        GL20.glDeleteShader(vert);
        GL20.glDeleteShader(frag);
    }

    private int compileShader(Identifier id, int type) throws Exception {
        RenderSystem.assertOnRenderThread();

        ResourceManager rm = MinecraftClient.getInstance().getResourceManager();
        Optional<Resource> opt = rm.getResource(id);

        if (opt.isEmpty()) {
            throw new RuntimeException("Shader resource not found: " + id);
        }

        Resource res = opt.get();

        StringBuilder src = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                src.append(line).append('\n');
            }
        }

        int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, src.toString());
        GL20.glCompileShader(shader);

        int compiled = GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS);
        if (compiled == GL20.GL_FALSE) {
            String log = GL20.glGetShaderInfoLog(shader);
            GL20.glDeleteShader(shader);
            throw new RuntimeException("Shader compile failed (" + id + "): " + log);
        }

        return shader;
    }

    public void use() {
        RenderSystem.assertOnRenderThread();
        GL20.glUseProgram(programId);
    }

    public void stop() {
        RenderSystem.assertOnRenderThread();
        GL20.glUseProgram(0);
    }

    private int getUniformLocation(String name) {
        return uniformLocations.computeIfAbsent(name,
                n -> GL20.glGetUniformLocation(programId, n));
    }

    public void setUniform1i(String name, int v) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniform1i(getUniformLocation(name), v);
    }

    public void setUniform1f(String name, float v) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniform1f(getUniformLocation(name), v);
    }

    public void setUniform2f(String name, float x, float y) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniform2f(getUniformLocation(name), x, y);
    }

    public void setUniform3f(String name, float x, float y, float z) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniform3f(getUniformLocation(name), x, y, z);
    }

    public void setUniform4f(String name, float x, float y, float z, float w) {
        RenderSystem.assertOnRenderThread();
        GL20.glUniform4f(getUniformLocation(name), x, y, z, w);
    }

    public int getProgramId() {
        return programId;
    }

    @Override
    public void close() {
        RenderSystem.assertOnRenderThread();
        GL20.glDeleteProgram(programId);
    }
}
