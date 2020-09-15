package koks.event.impl;

import koks.event.Event;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 05:18
 */
public class EventPacket extends Event {

    private final Packet<? extends INetHandler> packet;
    private final Type type;

    public EventPacket(Packet<? extends INetHandler> packet, Type type) {
        this.packet = packet;
        this.type = type;
    }

    public Packet<? extends INetHandler> getPacket() {
        return packet;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        RECEIVE,SEND;
    }
}
