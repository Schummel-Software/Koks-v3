package koks.module.impl.movement;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 20:55
 */
public class Fly extends Module {

    public Setting aac3312boost = new Setting("AAC3.3.12-Boost", 9F, 1F, 10F, true, this);
    public Setting mode = new Setting("Mode", new String[]{"AAC3.3.12"}, "AAC3.3.12", this);

    public Fly() {
        super("Fly", "Flying around the world", Category.MOVEMENT);
        registerSetting(aac3312boost);
        registerSetting(mode);
    }

    @Override
    public void onEvent(Event event) {
        switch (mode.getCurrentMode()) {
            case "AAC3.3.12":
                if (event instanceof EventUpdate) {
                    if (mc.thePlayer.posY <= -70) {
                        mc.thePlayer.motionY = aac3312boost.getCurrentValue();
                    }
                }
                break;
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
    }

}