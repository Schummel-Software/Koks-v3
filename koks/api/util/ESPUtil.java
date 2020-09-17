package koks.api.util;

import koks.Koks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 16:55
 */
public class ESPUtil {

    private final Map<Integer, Boolean> glCapMap = new HashMap<>();
    private final Minecraft mc = Minecraft.getMinecraft();

    public void drawCorners(double x, double y, double z, int xOffset, int yOffset, int length, int width) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glScaled(-0.03, -0.03, -0.03);
        GL11.glRotated(mc.getRenderManager().playerViewY, 0, -1, 0);
        GlStateManager.disableDepth();

        int color = new Color(0xFFFFFFFF).getRGB();

        // Top-Left (SIDE)
        Gui.drawRect(-xOffset, -yOffset, -xOffset + width, -yOffset + length, color);
        // Top-Left (TOP)
        Gui.drawRect(-xOffset, -yOffset, -xOffset + length, -yOffset + width, color);

        // Top-Right (SIDE)
        Gui.drawRect(xOffset, -yOffset, xOffset - width, -yOffset + length, color);
        // Top-Right (TOP)
        Gui.drawRect(xOffset, -yOffset, xOffset - length, -yOffset + width, color);

        // Bottom-Left (SIDE)
        Gui.drawRect(-xOffset, yOffset, -xOffset + width, yOffset - length, color);
        // Bottom-Left (TOP)
        Gui.drawRect(-xOffset, yOffset, -xOffset + length, yOffset - width, color);

        // Bottom-Right (SIDE)
        Gui.drawRect(xOffset, yOffset, xOffset - width, yOffset - length, color);
        // Bottom-Right (TOP)
        Gui.drawRect(xOffset, yOffset, xOffset - length, yOffset - width, color);

        GlStateManager.enableDepth();
        GL11.glPopMatrix();
    }

    public void renderBox(AxisAlignedBB axisalignedbb) {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        enableGlCap(GL11.GL_BLEND);
        disableGlCap(GL11.GL_TEXTURE_2D, GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);

        GL11.glLineWidth(1F);
        RenderGlobal.func_181563_a(axisalignedbb, Koks.getKoks().clientColor.getRed(), Koks.getKoks().clientColor.getGreen(), Koks.getKoks().clientColor.getBlue(), 255);

        GL11.glColor4f(Koks.getKoks().clientColor.getRed() / 255F, Koks.getKoks().clientColor.getGreen() / 255F, Koks.getKoks().clientColor.getBlue() / 255F, 0.2F);
        drawFilledBox(axisalignedbb);

        GlStateManager.resetColor();
        GL11.glDepthMask(true);
        resetCaps();
    }

    // Found in Render:renderOffsetAABB (269)
    public void drawFilledBox(AxisAlignedBB boundingBox) {
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_NORMAL);
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
    }

    public void resetCaps() {
        glCapMap.forEach(ESPUtil::setGlState);
    }

    public void enableGlCap(final int cap) {
        setGlCap(cap, true);
    }

    public void disableGlCap(final int... caps) {
        for (final int cap : caps)
            setGlCap(cap, false);
    }

    public void setGlCap(final int cap, final boolean state) {
        glCapMap.put(cap, GL11.glGetBoolean(cap));
        setGlState(cap, state);
    }

    public static void setGlState(final int cap, final boolean state) {
        if (state)
            GL11.glEnable(cap);
        else
            GL11.glDisable(cap);
    }
}