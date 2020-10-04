package koks.module.impl.movement;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 13:42
 */
public class NoSlowdown extends Module {

    public Setting speed = new Setting("Speed", 100, 20, 100, true, this);
    public Setting sprint = new Setting("Sprint", true, this);

    public NoSlowdown() {
        super("NoSlowdown", "Prevents you from slowing down while using an item", Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            setInfo(speed.getCurrentValue() + "");
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}