package koks.module.impl.movement;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Step extends Module {

    Setting mode = new Setting("Mode", new String[]{"Vanilla", "Intave"}, "Intave", this);
    final Setting stepHeight = new Setting("Step Height", 4, 1, 4, false, this);

    public Step() {
        super("Step", "Goes up blocks automatically", Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            String extra = mode.getCurrentMode().equalsIgnoreCase("Vanilla") ? " [" + stepHeight.getCurrentValue() + "]" : "";
            setInfo(mode.getCurrentMode() + extra);
        }
        switch (mode.getCurrentMode()) {
            case "Vanilla":
                if (event instanceof EventUpdate) getPlayer().stepHeight = stepHeight.getCurrentValue();
                break;
            case "Intave": {
                if (event instanceof EventUpdate) {
                    if (getPlayer().isCollidedHorizontally && !getPlayer().isOnLadder() && getPlayer().onGround && isMoving() && getPlayer().stepHeight != 1) {
                        getPlayer().motionY = 0.408;
                        getPlayer().onGround = false;
                        getPlayer().stepHeight = 1;
                    } else {
                        getPlayer().stepHeight = 0.5F;
                        if(getPlayer().isCollidedHorizontally && !getPlayer().isOnLadder() && isMoving())
                            getPlayer().onGround = true;
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        switch (mode.getCurrentMode()) {
            case "Vanilla":
                getPlayer().stepHeight = 0.6f;
                break;
        }
    }
}
