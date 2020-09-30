package koks.module.impl.render;

import koks.Koks;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.event.impl.EventMotion;
import koks.event.impl.EventRender3D;
import koks.module.Module;
import koks.api.settings.Setting;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 06:07
 */
public class TrailESP extends Module {

    public Setting length = new Setting("Length", 50, 5, 1000, true, this);
    public Setting inFirstPerson = new Setting("Show In FirstPerson", true, this);

    public TrailESP() {
        super("TrailESP", "Its render your positions in to a line", Category.RENDER);
    }

    public ArrayList<double[]> positions = new ArrayList<>();

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventRender3D) {
            if (mc.gameSettings.thirdPersonView != 0 || inFirstPerson.isToggled()) {
                GL11.glPushMatrix();
                GL11.glColor4f(Koks.getKoks().clientColor.getRed() / 255F, Koks.getKoks().clientColor.getGreen() / 255F, Koks.getKoks().clientColor.getBlue() / 255F, Koks.getKoks().clientColor.getAlpha() / 255F);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glLineWidth(2F);

                GL11.glBegin(GL11.GL_LINE_STRIP);

                for (double[] pos : positions) {
                    GL11.glVertex3d(pos[0] - mc.getRenderManager().renderPosX, pos[1] - mc.getRenderManager().renderPosY, pos[2] - mc.getRenderManager().renderPosZ);
                }

                GL11.glVertex3d(0, 0.01, 0);
                GL11.glEnd();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glColor4f(1, 1, 1, 1);

                GL11.glPopMatrix();
            }
        }

        if(event instanceof EventUpdate) {

            if(positions.size() > length.getCurrentValue()) {
                int toMush = (int) (positions.size() - length.getCurrentValue());
                for(int i = 0; i < toMush; i++) {
                    positions.remove(i);
                }
            }
        }

        if(event instanceof EventMotion) {
            positions.add(new double[] {mc.thePlayer.posX,mc.thePlayer.posY + 0.01,mc.thePlayer.posZ});

        }
    }

    @Override
    public void onEnable() {
        positions.clear();
    }

    @Override
    public void onDisable() {

    }
}
