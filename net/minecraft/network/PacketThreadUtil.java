package net.minecraft.network;

import koks.Koks;
import koks.manager.event.impl.EventPacket;
import net.minecraft.util.IThreadListener;

public class PacketThreadUtil
{
    public static <T extends INetHandler> void checkThreadAndEnqueue(final Packet<T> p_180031_0_, final T p_180031_1_, IThreadListener p_180031_2_) throws ThreadQuickExitException
    {
        EventPacket eventPacket = new EventPacket(p_180031_0_, EventPacket.Type.RECEIVE);
        Koks.getKoks().eventManager.onEvent(eventPacket);
        if (!p_180031_2_.isCallingFromMinecraftThread())
        {
            p_180031_2_.addScheduledTask(new Runnable()
            {
                public void run()
                {
                    p_180031_0_.processPacket(p_180031_1_);
                }
            });
            throw ThreadQuickExitException.field_179886_a;
        }
    }
}
