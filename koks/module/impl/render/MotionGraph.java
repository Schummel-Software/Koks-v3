package koks.module.impl.render;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventRender2D;
import koks.event.impl.EventTick;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "MotionGraph", description = "", category = Module.Category.RENDER)
public class MotionGraph extends Module {

    private List<Double> motionSpeed = new ArrayList<>();
    private Setting background = new Setting("Background", true, this);

    @Override
    public void onEvent(Event event) {

        if (event instanceof EventRender2D) {
            ScaledResolution sr = new ScaledResolution(mc);
            GL11.glPushMatrix();
            GL11.glColor3f(0, 0, 0);
            GL11.glLineWidth(4);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            if (background.isToggled()) {
                double add2 = 0;
                for (int i = 0; i < motionSpeed.size(); i++) {
                    GL11.glVertex2d(sr.getScaledWidth() / 2 - 60 + add2, sr.getScaledHeight() - 75 - motionSpeed.get(i));
                    add2 += 2;
                }
            }
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glColor3f(1, 1, 1);
            GL11.glLineWidth(2);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            double add = 0;
            int offset = 0;
            for (int i = 0; i < motionSpeed.size(); i++) {
                GL11.glVertex2d(sr.getScaledWidth() / 2 - 60 + add, sr.getScaledHeight() - 75 - motionSpeed.get(i));
                add += 2;
            }
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
        }
        if (event instanceof EventTick) {
            motionSpeed.add(Math.hypot(getPlayer().motionX, getPlayer().motionZ) * 100);

            if (motionSpeed.size() > 70) {
                motionSpeed.remove((motionSpeed.size() - 71));
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
