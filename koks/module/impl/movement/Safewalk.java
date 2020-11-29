package koks.module.impl.movement;

import koks.event.Event;
import koks.event.impl.EventSafeWalk;
import koks.module.Module;
import koks.module.ModuleInfo;

/**
 * @author kroko
 * @created on 01.11.2020 : 02:21
 */

@ModuleInfo(name = "Safewalk", category = Module.Category.MOVEMENT, description = "You stay safe")
public class Safewalk extends Module {
    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if(event instanceof EventSafeWalk) {
            if(getPlayer().onGround)((EventSafeWalk) event).setSafe(true);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
