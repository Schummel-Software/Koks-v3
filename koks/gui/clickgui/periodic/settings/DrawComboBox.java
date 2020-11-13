package koks.gui.clickgui.periodic.settings;

import koks.Koks;
import koks.api.settings.Setting;
import koks.api.util.Animation;
import koks.api.util.RenderUtil;
import koks.gui.clickgui.Element;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author kroko
 * @created on 12.11.2020 : 23:11
 */
public class DrawComboBox extends Element {

    public Animation animation = new Animation();

    public DrawComboBox(Setting setting) {
        this.setting = setting;
    }

    public RenderUtil renderUtil = Koks.getKoks().wrapper.renderUtil;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if(animation.getY() == 0)
            animation.setY(y + (fr.FONT_HEIGHT + 2) * (extended ? setting.getModes().length + 1 : 1));

        animation.setGoalY(y + (fr.FONT_HEIGHT + 2) * (extended ? setting.getModes().length + 1 : 1));
        animation.setSpeed(0.3F);

        System.out.println(animation.getAnimationY());

        renderUtil.drawOutlineRect(x - 3, y, x - 3 + fr.getStringWidth(setting.getName()) + 20, animation.getAnimationY(), 1, setting.getModule().getCategory().getCategoryColor().getRGB(), new Color(16, 16, 16).getRGB());
        fr.drawString(setting.getName(), x - 3 + 2, y + 2, -1);
        fr.drawString(">", x - 3 + fr.getStringWidth(setting.getName()) + 13, y + fr.FONT_HEIGHT / 2 - 2,- 1);
        GL11.glPushMatrix();
        renderUtil.scissor(x - 3, y, x - 3 + fr.getStringWidth(setting.getName()) + 20, (int) animation.getAnimationY());
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        if(extended) {
            for(int i = 0; i < setting.getModes().length; i++) {
                String mode = setting.getModes()[i];
                fr.drawString(mode, x - 3 + 2, y + (fr.FONT_HEIGHT + 2) * (i + 1) + 2, setting.getCurrentMode().equalsIgnoreCase(mode) ? setting.getModule().getCategory().getCategoryColor().getRGB() : -1);
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0) {
            if(mouseX >= x - 3 && mouseX <= x - 3 + fr.getStringWidth(setting.getName()) + 20 && mouseY >= y && mouseY <= y + fr.FONT_HEIGHT + 2) {
                extended = !extended;
            }else if(extended) {
                for(int i = 0; i < setting.getModes().length; i++) {
                    String mode = setting.getModes()[i];
                    if(mouseX >= x - 3 && mouseX <= x - 3 + fr.getStringWidth(setting.getName()) + 20 && mouseY >= y + (fr.FONT_HEIGHT + 2) * (i + 1) && mouseY <= y + (fr.FONT_HEIGHT + 2) * (i + 2)) {
                        setting.setCurrentMode(mode);
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }
}
