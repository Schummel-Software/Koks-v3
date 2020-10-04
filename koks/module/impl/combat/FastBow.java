package koks.module.impl.combat;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

/**
 * @author kroko
 * @created on 23.09.2020 : 16:37
 */
public class FastBow extends Module {

    Setting strength = new Setting("Strength", 20F, 5F, 20F, true, this);

    public FastBow() {
        super("FastBow", "You shoot arrows fast", Category.COMBAT);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            setInfo(strength.getCurrentValue() + "");
            if (mc.thePlayer.isUsingItem() && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
                for (int i = 0; i < strength.getCurrentValue(); i++)
                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer(false));
                getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, getPlayer().getPosition().ORIGIN, EnumFacing.DOWN));
                getPlayer().stopUsingItem();
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
