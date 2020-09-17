package koks.module.impl.movement;

import koks.Koks;
import koks.event.Event;
import koks.event.impl.EventTick;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.impl.combat.KillAura;
import koks.module.impl.world.Scaffold;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 20:10
 */
public class Sprint extends Module {

    public Sprint() {
        super("Sprint", "Sprints automaticly", Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            KillAura killAura = (KillAura) Koks.getKoks().moduleManager.getModule(KillAura.class);
            Scaffold scaffold = (Scaffold) Koks.getKoks().moduleManager.getModule(Scaffold.class);
            if (scaffold.isToggled())
                return;
            if (killAura.isToggled() && killAura.finalEntity != null && killAura.stopSprinting.isToggled())
                return;
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