package koks.module.impl.render;

import koks.Koks;
import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventRender3D;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Set;

/**
 * @author kroko
 * @created on 25.10.2020 : 06:10
 */
@ModuleInfo(name = "Tracers", category = Module.Category.RENDER, description = "It shows where other players are")
public class Tracers extends Module {

    public Setting width = new Setting("Width", 2F, 1F, 5F, false, this);
    public Setting playerMode = new Setting("Player-Mode", new String[] {"Head", "Foot"}, "Head", this);
    public Setting targetMode = new Setting("Target-Mode", new String[] {"Head", "Foot"}, "Foot", this);

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender3D) {
            for (Entity entity : getWorld().loadedEntityList) {
                if (entity instanceof EntityPlayer && entity != getPlayer() && !entity.isInvisible()) {
                    GL11.glPushMatrix();

                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    GL11.glLineWidth(width.getCurrentValue());

                    drawLine(entity, Koks.getKoks().clientColor);

                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glEnable(GL11.GL_TEXTURE_2D);

                    GL11.glPopMatrix();
                }
            }
        }
    }

    public void drawLine(Entity entity, Color color) {
        GL11.glLoadIdentity();
        mc.entityRenderer.orientCamera(mc.timer.renderPartialTicks);

        double distance = entity.getDistanceToEntity(getPlayer());
        if (distance <= 10)
            GL11.glColor4f(color.brighter().getRed() / 255F, color.brighter().getGreen() / 255F, color.brighter().getBlue() / 255F, color.brighter().getAlpha() / 255F);
        else if (distance >= 35)
            GL11.glColor4f(color.darker().getRed() / 255F, color.darker().getGreen() / 255F, color.darker().getBlue() / 255F, color.darker().getAlpha() / 255F);
        else
            GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);

        GL11.glBegin(GL11.GL_LINE_STRIP);


        double xPos = (entity.lastTickPosX
                + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks)
                - mc.getRenderManager().renderPosX;
        double yPos = (entity.lastTickPosY
                + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks)
                - mc.getRenderManager().renderPosY;
        double zPos = (entity.lastTickPosZ
                + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks)
                - mc.getRenderManager().renderPosZ;

        float targetEye = targetMode.getCurrentMode().equalsIgnoreCase("Head") ? entity.getEyeHeight() : 0;
        float playerEye = playerMode.getCurrentMode().equalsIgnoreCase("Head") ? getPlayer().getEyeHeight() : 0;

        GL11.glVertex3d(xPos, yPos + targetEye, zPos);

        GL11.glVertex3d(0, playerEye, 0);
        GL11.glEnd();
        GL11.glColor4f(1, 1, 1, 1);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
