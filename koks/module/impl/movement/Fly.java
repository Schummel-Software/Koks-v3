package koks.module.impl.movement;

import koks.Koks;
import koks.api.settings.Setting;
import koks.api.util.MovementUtil;
import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.event.impl.EventPacket;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;
import koks.module.impl.debug.Debug;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.sql.Time;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 20:55
 */

@ModuleInfo(name = "Fly", description = "Flying around the world", category = Module.Category.MOVEMENT)
public class Fly extends Module {

    public Setting aac3312boost = new Setting("AAC3.3.12-Boost", 9F, 1F, 10F, true, this);
    public Setting mode = new Setting("Mode", new String[]{"AAC3.3.12", "MCCentral", "CubeCraft"}, "AAC3.3.12", this);
    private final MovementUtil movementUtil = new MovementUtil();
    public TimeHelper timeHelper = new TimeHelper();
    public TimeHelper damageTime = new TimeHelper();

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            String extra = mode.getCurrentMode().equalsIgnoreCase("AAC3.3.12") ? " [" + aac3312boost.getCurrentValue() + "]" : "";
            setInfo(mode.getCurrentMode() + extra);
        }
        switch (mode.getCurrentMode()) {
            case "AAC3.3.12":
                if (event instanceof EventUpdate) {
                    if (mc.thePlayer.posY <= -70) {
                        mc.thePlayer.motionY = aac3312boost.getCurrentValue();
                    }
                }
                break;
            case "MCCentral":
                if (event instanceof EventUpdate) {
                    mc.thePlayer.motionY = 0;
                    if (isMoving())
                        movementUtil.setSpeed(0.8, true);
                    if (mc.gameSettings.keyBindJump.isKeyDown())
                        mc.thePlayer.motionY = 0.5;
                    if (mc.gameSettings.keyBindSneak.isKeyDown())
                        mc.thePlayer.motionY = -0.5;
                }
                break;
            case "CubeCraft":
                if (event instanceof EventUpdate) {
                    mc.thePlayer.motionY = 0.0;
                    mc.timer.timerSpeed = 0.3F;
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                        mc.thePlayer.motionY -= 0.01;
                    }

                    if (damageTime.hasReached(1500)) {


                        damageTime.reset();
                    }

                    if (timeHelper.hasReached(800)) {
                        movementUtil.setSpeed(0.4, false);
                        timeHelper.reset();
                    }
                }
                break;
        }
    }

    @Override
    public void onEnable() {
        timeHelper.reset();
        damageTime.reset();

    }

    @Override
    public void onDisable() {
        mc.thePlayer.speedInAir = 0.02F;
        mc.timer.timerSpeed = 1.0F;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
    }

}