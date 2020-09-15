package koks.gui.tabgui;

import koks.Koks;
import koks.event.impl.EventKeyPress;
import koks.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 12:41
 */
public class DrawCategory {

    private final ArrayList<DrawModule> drawModules = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();
    private final FontRenderer fr = mc.fontRendererObj;
    private int x, y, width, height, currentCategory, currentModule;
    private boolean extended;
    private Module.Category category;

    public DrawCategory(Module.Category category) {
        this.category = category;

        for (Module module : Koks.getKoks().moduleManager.getModules()) {
            if (module.getCategory() == category) {
                drawModules.add(new DrawModule(module));
            }
        }
    }

    public void drawScreen(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        Gui.drawRect(x, y, x + width, y + height, currentCategory == category.ordinal() ? Koks.getKoks().clientColor.getRGB() : 0xBB000000);
        fr.drawStringWithShadow(category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase(), x + 3, y + height / 2 - fr.FONT_HEIGHT / 2 + 1, 0xFFFFFFFF);

        int y2 = y;
        if (extended && Koks.getKoks().tabGUI.extendedCat == category) {
            for (DrawModule drawModule : drawModules) {
                drawModule.drawScreen(x + width, y2, width, height, drawModule.module == drawModules.get(currentModule).module);
                y2 += height;
            }
        }
    }

    public void manageKeys(EventKeyPress eventKeyPress) {
        int key = eventKeyPress.getKey();

        if (!extended && Koks.getKoks().tabGUI.extendedCat == null) {
            if (key == Keyboard.KEY_UP) {
                if (currentCategory > 0) {
                    currentCategory--;
                }
            } else if (key == Keyboard.KEY_DOWN) {
                if (currentCategory < Module.Category.values().length - 1) {
                    currentCategory++;
                }
            } else if (key == Keyboard.KEY_RIGHT && currentCategory == category.ordinal() && !drawModules.isEmpty()) {
                extended = true;
                Koks.getKoks().tabGUI.extendedCat = category;
            }
        } else if (extended) {
            if (key == Keyboard.KEY_LEFT) {
                extended = false;
                Koks.getKoks().tabGUI.extendedCat = null;
            } else if (key == Keyboard.KEY_RIGHT) {
                drawModules.get(currentModule).module.toggle();
            } else if (key == Keyboard.KEY_UP) {
                if (currentModule > 0) {
                    currentModule--;
                }
            } else if (key == Keyboard.KEY_DOWN) {
                if (currentModule < drawModules.size() - 1) {
                    currentModule++;
                }
            }

            for (DrawModule drawModule : drawModules) {
                drawModule.manageKeys(eventKeyPress);
            }
        }
    }

}