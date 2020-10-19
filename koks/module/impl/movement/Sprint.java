package koks.module.impl.movement;

import koks.Koks;
import koks.event.Event;
import koks.event.impl.EventTick;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;
import koks.module.impl.combat.KillAura;
import koks.module.impl.world.Scaffold;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 20:10
 */

@ModuleInfo(name = "Sprint", description = "Sprints automatically", category = Module.Category.MOVEMENT)
public class Sprint extends Module {

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            mc.gameSettings.keyBindSprint.pressed = true;
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        mc.thePlayer.setSprinting(false);
        mc.gameSettings.keyBindSprint.pressed = false;
    }

}