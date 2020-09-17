package koks.module.impl.movement;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.module.Module;

public class Step extends Module {

    final Setting stepHeight = new Setting("Step Height", 4,1,4,false, this);

    public Step() {
        super("Step", "Goes up blocks automatically", Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public void onEnable() {
        getPlayer().stepHeight = stepHeight.getCurrentValue();
    }

    @Override
    public void onDisable() {
        getPlayer().stepHeight = 0.6f;
    }
}
