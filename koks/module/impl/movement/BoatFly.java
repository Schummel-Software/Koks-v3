package koks.module.impl.movement;

import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.api.settings.Setting;
import net.minecraft.util.Vec3;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 05:41
 */
public class BoatFly extends Module {

    public Setting Mode = new Setting("Mode", new String[] {"AAC4"}, "AAC4", this);
    public Setting AAC4Boost = new Setting("AAC4-Boost", 8F, 1F, 10F,true, this);

    public BoatFly() {
        super("BoatFly", "You can fly with the boat", Category.MOVEMENT);
    }

    public boolean ride;

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            String extra = Mode.getCurrentMode().equalsIgnoreCase("AAC4") ? " [" + AAC4Boost.getCurrentValue() + "]" : "";
            setInfo(Mode.getCurrentMode() + extra);
            if(mc.thePlayer.isRiding())ride = true;
            else {
                if(ride) {
                    Vec3 look = mc.thePlayer.getLookVec();
                    mc.thePlayer.motionZ = look.zCoord * (AAC4Boost.getCurrentValue());
                    mc.thePlayer.motionX = look.xCoord * (AAC4Boost.getCurrentValue());
                    mc.thePlayer.motionY = 1;
                    ride = false;
                }
            }
        }
    }

    @Override
    public void onEnable() {
        ride = false;
    }

    @Override
    public void onDisable() {

    }
}
