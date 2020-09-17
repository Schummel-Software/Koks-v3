package koks.module.impl.movement;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author deleteboys | lmao | kroko
 * @created on 17.09.2020 : 18:39
 */
public class VClip extends Module {

    Setting Height = new Setting("Height", -2F, -5F, 5, true,this);

    public VClip() {
        super("VClip", "You can teleport through grounds", Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public void onEnable() {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + Height.getCurrentValue(), z, false));
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
        setToggled(false);
    }

    @Override
    public void onDisable() {

    }
}
