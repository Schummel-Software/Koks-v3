package koks.module.impl.movement;

import koks.api.settings.Setting;
import koks.api.util.MovementUtil;
import koks.event.Event;
import koks.event.impl.EventPacket;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 20:55
 */
public class Fly extends Module {

    public Setting aac3312boost = new Setting("AAC3.3.12-Boost", 9F, 1F, 10F, true, this);
    public Setting mode = new Setting("Mode", new String[]{"AAC3.3.12", "MCCentral", "CubeCraft"}, "AAC3.3.12", this);
    private final MovementUtil movementUtil = new MovementUtil();

    public Fly() {
        super("Fly", "Flying around the world", Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event event) {
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
                    movementUtil.setSpeed(0.8);
                    if (mc.gameSettings.keyBindJump.isKeyDown())
                        mc.thePlayer.motionY = 0.5;
                    if (mc.gameSettings.keyBindSneak.isKeyDown())
                        mc.thePlayer.motionY = -0.5;
                }
                break;
            case "CubeCraft":
                if (event instanceof EventUpdate) {
                    mc.thePlayer.motionY = -0.0;
                    mc.timer.timerSpeed = 0.3F;
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                        mc.thePlayer.motionY -= 0.01;
                    }
                }
                break;
        }
    }

    @Override
    public void onEnable() {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y - 4, z, false));
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
    }

    @Override
    public void onDisable() {
        mc.thePlayer.speedInAir = 0.02F;
        mc.timer.timerSpeed = 1.0F;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
    }

}