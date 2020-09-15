package koks.module.impl.debug;

import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 11:56
 */
public class Test extends Module {

    private final TimeHelper timeHelper = new TimeHelper();

    public Test() {
        super("Test", "Bugs through the Border", Category.DEBUG);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            if (mc.thePlayer.moveForward != 0 && timeHelper.hasReached(150)) {
                double x = -Math.sin(getDirection());
                double z = Math.cos(getDirection());
                mc.thePlayer.setPosition(mc.thePlayer.posX + x * 0.1, mc.thePlayer.posY, mc.thePlayer.posZ + z * 0.1);
                if (mc.thePlayer.isOutsideBorder()) {
                    mc.thePlayer.isDead = true;
                }
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        mc.thePlayer.isDead = false;
    }

}