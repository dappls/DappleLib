package net.dappls.dapplelib.client.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.GlTexture;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import java.util.LinkedHashMap;

public class ShaderRegistry {
    public static final Identifier FULLSCREEN_VSH = Identifier.of("bundle", "shaders/post/fullscreen.vsh");
    public static final Identifier FULLSCREEN_FSH = Identifier.of("bundle", "shaders/post/fullscreen.fsh");

    @FunctionalInterface
    public interface ShaderUniformCallback {
        void setUniforms(ShaderProgram shader, float tickDelta);
    }

    private static final LinkedHashMap<Identifier, ShaderEntry> entries = new LinkedHashMap<>();
    private static final FramebufferManager fbo = new FramebufferManager();
    private static final FullscreenQuad quad = new FullscreenQuad();
    private static boolean quadInitialized = false;
    private static long startTimeNanos = -1;

    public static void register(Identifier id, Identifier vertexShader, Identifier fragmentShader, ShaderUniformCallback callback) {
        entries.put(id, new ShaderEntry(vertexShader, fragmentShader, callback));
    }
    public static void register(Shader id, PostProcessingShader vertexShader, PostProcessingShader fragmentShader, ShaderUniformCallback callback) {
        entries.put(id.getIdentifier(), new ShaderEntry(vertexShader.getIdentifier(), fragmentShader.getIdentifier(), callback));
    }

    public static void register(Shader id, Identifier vertexShader, PostProcessingShader fragmentShader, ShaderUniformCallback callback) {
        entries.put(id.getIdentifier(), new ShaderEntry(vertexShader, fragmentShader.getIdentifier(), callback));
    }

    public static void register(Identifier id, Identifier vertexShader, Identifier fragmentShader) {
        register(id, vertexShader, fragmentShader, null);
    }

    public static ShaderProgram getShader(Identifier id) {
        ShaderEntry entry = entries.get(id);
        return entry != null ? entry.program : null;
    }

    public static void setEnabled(Identifier id, boolean enabled) {
        ShaderEntry entry = entries.get(id);
        if (entry != null) entry.enabled = enabled;
    }

    public static boolean isEnabled(Identifier id) {
        ShaderEntry entry = entries.get(id);
        return entry != null && entry.enabled;
    }

    public static void renderAll(float tickDelta) {
        if (entries.isEmpty()) return;

        RenderSystem.assertOnRenderThread();
        MinecraftClient mc = MinecraftClient.getInstance();
        int w = mc.getWindow().getFramebufferWidth();
        int h = mc.getWindow().getFramebufferHeight();
        if (w <= 0 || h <= 0) return;

        // Get MC's color texture GL ID directly
        GlTexture mcColorTex = (GlTexture) mc.getFramebuffer().getColorAttachment();
        int mcTexId = mcColorTex != null ? mcColorTex.getGlId() : 0;
        if (mcTexId == 0) return;

        if (!quadInitialized) {
            quad.init();
            quadInitialized = true;
        }

        // Save MC's framebuffer binding
        int mcFboId = GL11.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING);

        // Force the GPU to finish all pending commands so MC's texture is ready to sample
        GL11.glFinish();

        for (ShaderEntry entry : entries.values()) {
            if (!entry.enabled) continue;

            if (entry.program == null) {
                try {
                    entry.program = new ShaderProgram(entry.vertexShader, entry.fragmentShader);
                } catch (Exception e) {
                    e.printStackTrace();
                    entry.enabled = false;
                    continue;
                }
            }

            fbo.ensureSize();

            // Save GL state
            boolean depthWasEnabled = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
            boolean blendWasEnabled = GL11.glIsEnabled(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_BLEND);

            // Pass 1: Render effect to our FBO, sampling from MC's texture
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo.getFbo());
            GL11.glViewport(0, 0, w, h);

            entry.program.use();
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mcTexId);
            entry.program.setUniform1i("u_scene", 0);

            // Default uniforms available to every shader
            if (startTimeNanos == -1) startTimeNanos = System.nanoTime();
            float time = (float) ((System.nanoTime() - startTimeNanos) / 1_000_000_000.0);
            entry.program.setUniform1f("u_time", time);
            entry.program.setUniform2f("u_resolution", (float) w, (float) h);

            if (entry.callback != null) {
                entry.callback.setUniforms(entry.program, tickDelta);
            }

            quad.draw();
            entry.program.stop();

            // Pass 2: Copy our FBO result back into MC's texture
            // Bind our FBO as the read source, then copy into MC's texture
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fbo.getFbo());
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mcTexId);
            GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, w, h);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

            // Restore MC's framebuffer and GL state
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, mcFboId);
            GL11.glViewport(0, 0, w, h);
            if (depthWasEnabled) GL11.glEnable(GL11.GL_DEPTH_TEST);
            if (blendWasEnabled) GL11.glEnable(GL11.GL_BLEND);
        }
    }

    public static void reloadAll() {
        for (ShaderEntry entry : entries.values()) {
            if (entry.program != null) {
                entry.program.close();
                entry.program = null;
            }
        }
    }

    public static void destroyAll() {
        for (ShaderEntry entry : entries.values()) {
            if (entry.program != null) {
                entry.program.close();
                entry.program = null;
            }
        }
        fbo.destroy();
        quad.destroy();
        quadInitialized = false;
    }

    private static class ShaderEntry {
        final Identifier vertexShader;
        final Identifier fragmentShader;
        final ShaderUniformCallback callback;
        ShaderProgram program;
        boolean enabled = true;

        ShaderEntry(Identifier vertexShader, Identifier fragmentShader, ShaderUniformCallback callback) {
            this.vertexShader = vertexShader;
            this.fragmentShader = fragmentShader;
            this.callback = callback;
        }

    }
}
