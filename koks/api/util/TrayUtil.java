package koks.api.util;

import jdk.internal.util.xml.impl.Input;
import koks.Koks;
import koks.api.settings.Setting;
import koks.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author kroko
 * @created on 20.11.2020 : 14:56
 */
public class TrayUtil {

    public PopupMenu popupMenu;
    public SystemTray tray;

    public ArrayList<Menu> categories = new ArrayList<>();

    private Minecraft mc = Minecraft.getMinecraft();

    public TrayUtil() {
        initTray();
    }

    public void initTray() {
        if (!SystemTray.isSupported())
            return;
        popupMenu = new PopupMenu();
        TrayIcon trayIcon = createIcon("icon/icon.png", Koks.getKoks().NAME + " v" + Koks.getKoks().VERSION);
        tray = SystemTray.getSystemTray();
        addComponents(tray, popupMenu);
        trayIcon.setPopupMenu(popupMenu);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private TrayIcon createIcon(String path, String description) {
        URL image = this.getClass().getResource(path);
        return new TrayIcon(Toolkit.getDefaultToolkit().createImage(image), description);
    }

    private void addComponents(SystemTray tray, PopupMenu popupMenu) {
        Menu debug = new Menu("Debug");
        MenuItem reloadChunks = new MenuItem("Reload Chunks");

        ActionListener reloadChunksAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hey");
                Minecraft.getMinecraft().renderGlobal.loadRenderers();
            }
        };

        Menu modules = new Menu("Modules");

        for (Module.Category category : Module.Category.values()) {
            Menu cat = new Menu(category.name());
            modules.add(cat);
            categories.add(cat);
        }

        for (Module module : Koks.getKoks().moduleManager.modules) {
            Menu category = null;
            Menu mod = new Menu(module.getName() + (module.getKey() != 0 ? " [" + Keyboard.getKeyName(module.getKey()) + "]" : ""));

            for (int i = 0; i < categories.size(); i++) {
                Menu menu = categories.get(i);
                Module.Category cat = Module.Category.values()[i];
                if (cat.equals(module.getCategory())) {
                    category = menu;
                    category.add(mod);
                }
            }

            CheckboxMenuItem toggle = new CheckboxMenuItem("Toggle", module.isToggled());

            ItemListener toggleAction = new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    module.setToggled(toggle.getState());
                }
            };

            toggle.addItemListener(toggleAction);

            mod.add(toggle);

            for (Setting setting : Koks.getKoks().settingsManager.getSettings()) {
                if (setting.getModule().equals(module)) {
                    switch (setting.getType()) {
                        case CHECKBOX:
                            CheckboxMenuItem checkbox = new CheckboxMenuItem(setting.getName(), setting.isToggled());

                            ItemListener clickEvent = new ItemListener() {
                                @Override
                                public void itemStateChanged(ItemEvent e) {
                                    setting.setToggled(checkbox.getState());
                                }
                            };

                            checkbox.addItemListener(clickEvent);

                            mod.add(checkbox);
                            break;
                        case COMBOBOX:
                            Menu combo = new Menu(setting.getName());
                            for (String modes : setting.getModes()) {
                                MenuItem mode = new MenuItem(modes);
                                mode.setEnabled(!modes.equalsIgnoreCase(setting.getCurrentMode()));

                                ActionListener choose = new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        setting.setCurrentMode(modes);
                                    }
                                };

                                mode.addActionListener(choose);

                                combo.add(mode);
                            }
                            mod.add(combo);
                            break;
                        case SLIDER:
                            //TODO
                            break;
                        case KEY:
                            //TODO
                            break;
                        case TYPE:
                            //TODO
                            break;
                    }
                }
            }
        }

        reloadChunks.addActionListener(reloadChunksAction);

        popupMenu.add(modules);
        popupMenu.add(debug);
        debug.add(reloadChunks);
    }
}
