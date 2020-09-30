package koks.module.impl.movement;

import koks.api.settings.Setting;
import koks.api.util.MovementUtil;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 18:17
 */
public class Speed extends Module {

    public Setting mode = new Setting("Mode", new String[]{"MCCentral", "Legit"}, "Legit", this);
    private final MovementUtil movementUtil = new MovementUtil();

    public Speed() {
        super("Speed", "Speeeeedy", Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            switch (mode.getCurrentMode()) {
                case "Legit":
                    if (mc.thePlayer.onGround) getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ, true));//0.42
                    if(!mc.thePlayer.onGround) getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.1, mc.thePlayer.posZ, true));
                    mc.thePlayer.onGround = true;
                    mc.thePlayer.setSprinting(true);
                    break;
                case "MCCentral":
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    } else {
                        movementUtil.setSpeed(0.7, true);
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