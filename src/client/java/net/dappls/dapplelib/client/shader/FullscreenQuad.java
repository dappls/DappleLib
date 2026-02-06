package net.dappls.dapplelib.client.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class FullscreenQuad {
    private int vao = 0;
    private int vbo = 0;

    public void init() {
        RenderSystem.assertOnRenderThread();
        if (vao != 0) return;

        float[] verts = {
                -1f, -1f, 0f, 0f,
                1f, -1f, 1f, 0f,
                -1f,  1f, 0f, 1f,
                1f,  1f, 1f, 1f
        };

        vao = GL30.glGenVertexArrays();
        vbo = GL15.glGenBuffers();

        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verts, GL15.GL_STATIC_DRAW);

        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 0);

        GL20.glEnableVertexAttribArray(1);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);

        GL30.glBindVertexArray(0);
    }

    public void draw() {
        RenderSystem.assertOnRenderThread();
        GL30.glBindVertexArray(vao);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
        GL30.glBindVertexArray(0);
    }

    public void destroy() {
        RenderSystem.assertOnRenderThread();
        if (vbo != 0) {
            GL15.glDeleteBuffers(vbo);
            vbo = 0;
        }
        if (vao != 0) {
            GL30.glDeleteVertexArrays(vao);
            vao = 0;
        }
    }
}

