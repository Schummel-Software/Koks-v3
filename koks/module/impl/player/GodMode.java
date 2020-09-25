package koks.module.impl.player;

import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.event.impl.EventHeadLook;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.api.settings.Setting;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 15:29
 */
public class GodMode extends Module {

    public Setting mode = new Setting("Mode", new String[]{"Intave Border"}, "Intave Border", this);

    public GodMode() {
        super("GodMode", "You cant take any damage", Category.PLAYER);
    }

    public TimeHelper timeHelper = new TimeHelper();

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventHeadLook) {
            if (mode.getCurrentMode().equalsIgnoreCase("Intave Border")) {
                if (mc.thePlayer.isOutsideBorder()) {
                    EventHeadLook eventHeadLook = (EventHeadLook) event;
                    ((EventHeadLook) event).setF1(mc.thePlayer.rotationYaw);
                    ((EventHeadLook) event).setF2(mc.thePlayer.rotationPitch);
                }
            }
        }

        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode());
            if (mode.getCurrentMode().equalsIgnoreCase("Intave Border")) {
                if (!mc.thePlayer.isOutsideBorder()) {
                    if (mc.gameSettings.keyBindForward.pressed) {
                        if (timeHelper.hasReached(150)) {
                            mc.thePlayer.setPosition(mc.thePlayer.posX - Math.sin(Math.toRadians(mc.thePlayer.rotationYaw)) * 0.1, mc.thePlayer.posY, getPlayer().posZ + Math.cos(Math.toRadians(mc.thePlayer.rotationYaw)) * 0.1);
                            timeHelper.reset();
                        }
                    }
                } else {
                    mc.thePlayer.motionY = 0;
                    mc.gameSettings.keyBindForward.pressed = false;
                    mc.gameSettings.keyBindBack.pressed = false;
                    mc.gameSettings.keyBindLeft.pressed = false;
                    mc.gameSettings.keyBindRight.pressed = false;
                }
            }
        }
    }

    @Override
    public void onEnable() {
        timeHelper.reset();
    }

    @Override
    public void onDisable() {

    }
}
