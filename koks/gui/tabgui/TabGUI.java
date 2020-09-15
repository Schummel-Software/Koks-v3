package koks.gui.tabgui;

import koks.event.impl.EventKeyPress;
import koks.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 12:40
 */
public class TabGUI {

    private final ArrayList<DrawCategory> drawCategories = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();
    private final FontRenderer fr = mc.fontRendererObj;
    private int x, y, width, height, category;
    public Module.Category extendedCat;

    public TabGUI() {
        for (Module.Category category : Module.Category.values()) {
            drawCategories.add(new DrawCategory(category));
        }
    }

    public void drawScreen(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        int y2 = y;
        for (DrawCategory drawCategory : drawCategories) {
            drawCategory.drawScreen(x, y2, width, height);
            y2 += height;
        }
    }

    public void manageKeys(EventKeyPress eventKeyPress) {
        int key = eventKeyPress.getKey();

        for (DrawCategory drawCategory : drawCategories) {
            drawCategory.manageKeys(eventKeyPress);
        }
    }

}