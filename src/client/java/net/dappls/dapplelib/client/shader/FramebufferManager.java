package net.dappls.dapplelib.client.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class FramebufferManager {
    private int fbo = 0;
    private int colorTex = 0;
    private int depthRb = 0;
    private int width = 0;
    private int height = 0;

    public void ensureSize() {
        RenderSystem.assertOnRenderThread();
        MinecraftClient mc = MinecraftClient.getInstance();
        int w = mc.getWindow().getFramebufferWidth();
        int h = mc.getWindow().getFramebufferHeight();
        if (w <= 0 || h <= 0) return;
        if (w == width && h == height && fbo != 0) return;
        resize(w, h);
    }

    private void resize(int w, int h) {
        destroy();
        width = w;
        height = h;

        fbo = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);

        colorTex = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTex);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorTex, 0);

        depthRb = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthRb);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH_COMPONENT24, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthRb);

        int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
        if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
            destroy();
            throw new IllegalStateException("Framebuffer incomplete: " + status);
        }

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public void bind() {
        RenderSystem.assertOnRenderThread();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
        GL11.glViewport(0, 0, width, height);
    }

    public void unbindToDefault() {
        RenderSystem.assertOnRenderThread();
        MinecraftClient mc = MinecraftClient.getInstance();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
    }

    public int getColorTexture() {
        return colorTex;
    }

    public int getFbo() {
        return fbo;
    }

    public void destroy() {
        RenderSystem.assertOnRenderThread();
        if (depthRb != 0) {
            GL30.glDeleteRenderbuffers(depthRb);
            depthRb = 0;
        }
        if (colorTex != 0) {
            GL11.glDeleteTextures(colorTex);
            colorTex = 0;
        }
        if (fbo != 0) {
            GL30.glDeleteFramebuffers(fbo);
            fbo = 0;
        }
    }
}

