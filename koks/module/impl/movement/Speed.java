package koks.module.impl.movement;

import koks.api.settings.Setting;
import koks.api.util.MovementUtil;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 18:17
 */
public class Speed extends Module {

    public Setting mode = new Setting("Mode", new String[]{"Mineplex", "Legit"}, "Legit", this);

    public Speed() {
        super("Speed", "Speeeeedy", Category.MOVEMENT);
        registerSetting(mode);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            MovementUtil movementUtil = new MovementUtil();
            switch (mode.getCurrentMode()) {
                case "Legit":
                    if (mc.thePlayer.onGround) mc.thePlayer.jump();
                    mc.thePlayer.speedInAir = 0.02F;
                    break;
                case "Mineplex":
                    if (mc.thePlayer.onGround) {
                        mc.timer.timerSpeed = 10;
                        mc.thePlayer.motionY = 0.42;
                    } else {
                        mc.timer.timerSpeed = 1.05F;
                        movementUtil.setSpeed(0.34);
                    }
                    break;
            }
        }

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        mc.thePlayer.speedInAir = 0.02F;
    }

}