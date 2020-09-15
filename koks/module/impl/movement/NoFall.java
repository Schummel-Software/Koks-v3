package koks.module.impl.movement;

import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 18:13
 */
public class NoFall extends Module {

    public NoFall() {
        super("NoFall", "Prevents you from getting falldamage", Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            if (getPlayer().fallDistance > 2) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                getPlayer().fallDistance = 0;
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