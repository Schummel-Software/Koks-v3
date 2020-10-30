package koks.gui.clickgui.elements.settings;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import koks.Koks;
import koks.api.settings.Setting;
import koks.api.util.RandomUtil;
import koks.gui.clickgui.elements.Element;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 00:34
 */
public class DrawType extends Element {

    public DrawType(Setting setting) {
        this.setting = setting;
    }

    boolean isKeyTyped = false;

    public RandomUtil randomUtil = Koks.getKoks().wrapper.randomUtil;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0xFF202020);
        fr.drawStringWithShadow(setting.getName(), x + 3, y + height / 2 - fr.FONT_HEIGHT / 2 + 1, 0xFFFFFFFF);
        String s = setting.getTyped();
        int rnd = (int) (System.currentTimeMillis() / 1000);
        fr.drawStringWithShadow(s.replace('§', '&') + (isKeyTyped ? rnd % 2 == 0 ? "§7_" : "" : ""), x + 3 + width - fr.getStringWidth(s.replace('§', '&')) - 5 - (isKeyTyped ? fr.getStringWidth("§7_") - 1 : 0), y + height / 2 - fr.FONT_HEIGHT / 2 + 1, 0xFFFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + 2 + width - 4 && mouseY > y + 2 && mouseY < y + height - 2;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        Keyboard.enableRepeatEvents(true);
        if(isKeyTyped) {
            int key = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
            if(key == Keyboard.KEY_RETURN) {
                isKeyTyped = false;
            }else if(key == 14) {
                if(setting.getTyped().length() <= 0) return;
                setting.setTyped(setting.getTyped().substring(0, setting.getTyped().length() -1));
                return;
            }
            if(ChatAllowedCharacters.isAllowedCharacter(typedChar) || typedChar == '§') {
                setting.setTyped(setting.getTyped() + typedChar);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isHovered(mouseX, mouseY)) {
            isKeyTyped = !isKeyTyped;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

}