package koks.module.impl.player;

import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;

/**
 * @author kroko
 * @created on 07.10.2020 : 16:27
 */

@ModuleInfo(name = "FastPlace", category = Module.Category.PLAYER, description = "You can place fast")
public class FastPlace extends Module {

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if(event instanceof EventUpdate) {
            mc.rightClickDelayTimer = 0;
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
