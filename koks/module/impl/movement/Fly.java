package koks.module.impl.movement;

import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 20:55
 */
public class Fly extends Module {

    public Fly() {
        super("Fly", "Flying around the world", Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            mc.thePlayer.motionY = 0;
            mc.timer.timerSpeed = 10.0F;
            if (mc.thePlayer.ticksExisted % 2 == 0) {
                pushPlayer(1);
            }
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