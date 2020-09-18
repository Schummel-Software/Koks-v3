package koks.module.impl.movement;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventKeyPress;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

/**
 * @author deleteboys | lmao | kroko
 * @created on 17.09.2020 : 18:39
 */
public class VClip extends Module {

    Setting upKey = new Setting("Up Key", Keyboard.KEY_UP, this);
    Setting upHeight = new Setting("Up Height", 5, 1, 5, true, this);

    Setting downKey = new Setting("Down Key", Keyboard.KEY_DOWN, this);
    Setting downHeight = new Setting("Down Height", -5, -5, -1, true, this);
    Setting stuck = new Setting("Stuck", true, this);

    public VClip() {
        super("VClip", "You can teleport through grounds", Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            if (stuck.isToggled())
                mc.thePlayer.motionY = 0;
        }
        if (event instanceof EventKeyPress) {
            double x = mc.thePlayer.posX;
            double y = mc.thePlayer.posY;
            double z = mc.thePlayer.posZ;
            if (((EventKeyPress) event).getKey() == upKey.getKey()) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + upHeight.getCurrentValue(), z, false));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
            } else if (((EventKeyPress) event).getKey() == downKey.getKey()) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + downHeight.getCurrentValue(), z, false));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
