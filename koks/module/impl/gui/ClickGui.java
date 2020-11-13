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

    public Setting mode = new Setting("Mode", new String[]{"Panel", "PE"}, "Panel", this);

    public ClickGui() {
        setKey(Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEvent(Event event) {
        if (mc.currentScreen == null) {
            switch (mode.getCurrentMode()) {
                case "PE":
                    mc.displayGuiScreen(Koks.getKoks().clickGUIPE);
                    break;
                case "Panel":
                    mc.displayGuiScreen(Koks.getKoks().clickGUI);
                    break;
            }
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