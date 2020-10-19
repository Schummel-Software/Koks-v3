package koks.module.impl.gui;

import koks.Koks;
import koks.event.Event;
import koks.module.Module;
import koks.api.settings.Setting;
import koks.module.ModuleInfo;
import org.lwjgl.input.Keyboard;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 00:18
 */

@ModuleInfo(name = "ClickGUI", description = "Opens the ClickGUI", category = Module.Category.GUI)
public class ClickGui extends Module {

    public Setting red = new Setting("Red", 0, 0, 255, true, this);
    public Setting green = new Setting("Green", 140, 0, 255, true, this);
    public Setting blue = new Setting("Blue", 255, 0, 255, true, this);

    public ClickGui() {
        setKey(Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEvent(Event event) {
        if (mc.currentScreen == null) {
            mc.displayGuiScreen(Koks.getKoks().clickGUI);
        }
        toggle();
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}