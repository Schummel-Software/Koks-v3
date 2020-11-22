package koks.module.impl.player;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventPacket;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;

import java.util.ArrayList;

/**
 * @author kroko
 * @created on 22.11.2020 : 23:14
 */

@ModuleInfo(name = "Blink", category = Module.Category.PLAYER, description = "You can blink")
public class Blink extends Module {

    //TODO: Spawn a Fake Player

    public ArrayList<Packet<?>> packets = new ArrayList<>();

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventPacket) {
            if(((EventPacket) event).getType() == EventPacket.Type.SEND) {
                Packet<?> packet = ((EventPacket) event).getPacket();
                if(!(packet instanceof C00PacketKeepAlive)) {
                    packets.add(packet);
                    event.setCanceled(true);
                }
            }
        }
    }

    @Override
    public void onEnable() {
        packets.clear();
    }

    @Override
    public void onDisable() {
        for(Packet<?> packet : packets) {
            sendPacketUnlogged(packet);
        }
        packets.clear();
    }
}
