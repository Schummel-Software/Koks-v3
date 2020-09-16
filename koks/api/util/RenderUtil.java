package koks.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;

import java.awt.*;
import java.util.List;

import static net.minecraft.client.renderer.GlStateManager.*;
import static net.minecraft.client.renderer.GlStateManager.rotate;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glScalef;

public class RenderUtil {
    final Minecraft mc = Minecraft.getMinecraft();
    final RenderManager renderManager = mc.getRenderManager();

    /**
     * {@see net.minecraft.client.renderer.entity.Render#renderLivingLabel(Entity, String, double, double, double, int)}
     * Modified by Phantom
     */
    public void renderNameTag(Entity entityIn, double x, double y, double z, Color color, Color colorSneaking) {
        if (!(entityIn instanceof EntityCreature || entityIn instanceof EntitySquid || entityIn instanceof EntityBat || entityIn instanceof EntityGhast || entityIn instanceof EntitySlime) &&
                entityIn instanceof EntityLivingBase && entityIn != Minecraft.getMinecraft().thePlayer) {
            if (!entityIn.getName().equals("Armor Stand")) {
                final FontRenderer fontRenderer = mc.fontRendererObj;
                double distance = mc.thePlayer.getDistanceToEntity(entityIn) / 4F;
                if (distance <= 3){
                    distance = 3;
                }
                final float scale = (float) (distance / 70F);
                pushMatrix();
                translate((float) x + 0.0F, (float) y + (((EntityLivingBase) entityIn).isChild() ? entityIn.height / 2.0F : entityIn.height + 0.5F), (float) z);
                glNormal3f(0.0F, 1.0F, 0.0F);
                rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                glScalef(-scale, -scale, scale);
                GlStateManager.disableLighting();
                GlStateManager.depthMask(false);
                GlStateManager.disableDepth();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                byte b0 = 0;
                if (entityIn.getName().equals("deadmau5")) {
                    b0 = -10;
                }
                final int width = fontRenderer.getStringWidth(entityIn.getName()) / 2;
                GlStateManager.disableTexture2D();
                Gui.drawRect(-width - 1, b0 - 2, width, b0 + fontRenderer.FONT_HEIGHT - 2, new Color(0, 0, 0, 180).getRGB());
                GlStateManager.enableTexture2D();
                final int playerColor = entityIn.isSneaking() ? colorSneaking.getRGB() : color.getRGB();
                fontRenderer.drawString(entityIn.getName(), -width, b0 - 1, playerColor);
                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                fontRenderer.drawString(entityIn.getName(), -width, b0 - 1, playerColor);
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.popMatrix();
            }
        }
    }
}
