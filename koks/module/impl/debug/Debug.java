package koks.module.impl.debug;

import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.module.Module;
import koks.api.settings.Setting;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:46
 */
public class Debug extends Module {

    public Setting testCheck = new Setting("Check", true, this);
    public Setting testCombo = new Setting("Combo", new String[]{"M1", "M2"}, "M1", this);
    public Setting testSlider = new Setting("Slider", 10, 5, 20, false, this);

    public Debug() {
        super("Debug", "Test Module", Category.DEBUG);
    }

    public TimeHelper timeHelper = new TimeHelper();

    @Override
    public void onEvent(Event event) {
    }


    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {

    }

}
