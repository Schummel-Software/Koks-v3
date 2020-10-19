package koks.module.impl.movement;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 13:42
 */

@ModuleInfo(name = "NoSlowdown", description = "Prevents you from slowing down while using an item", category = Module.Category.MOVEMENT)
public class NoSlowdown extends Module {

    public Setting speed = new Setting("Speed", 100, 20, 100, true, this);
    public Setting sprint = new Setting("Sprint", true, this);

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