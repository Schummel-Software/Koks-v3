package koks.module.impl.combat;

import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;

/**
 * @author kroko
 * @created on 11.10.2020 : 13:57
 */

@ModuleInfo(name = "Criticals", description = "Every hit is a critical hit", category = Module.Category.COMBAT)
public class Criticals extends Module {

    @Override
    public void onEvent(Event event) {
        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            if (getPlayer().onGround)
                getPlayer().jump();
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
