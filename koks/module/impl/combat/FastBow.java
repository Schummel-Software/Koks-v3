package koks.module.impl.combat;

import god.buddy.aot.BCompiler;
import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

/**
 * @author kroko
 * @created on 23.09.2020 : 16:37
 */

@ModuleInfo(name = "FastBow", description = "You shoot arrows fast", category = Module.Category.COMBAT)
public class FastBow extends Module {

    Setting mode = new Setting("Mode", new String[]{"Vanilla", "Timer"}, "Vanilla", this);
    Setting strength = new Setting("Strength", 20F, 5F, 20F, true, this);

    @BCompiler(aot = BCompiler.AOT.NORMAL)
    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode() + (mode.getCurrentMode().equalsIgnoreCase("Vanilla") ? " " + strength.getCurrentValue() : ""));
            switch (mode.getCurrentMode()) {
                case "Vanilla":
                    if (mc.thePlayer.isUsingItem() && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
                        for (int i = 0; i < strength.getCurrentValue(); i++)
                            getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer(false));
                        getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, getPlayer().getPosition().ORIGIN, EnumFacing.DOWN));
                        getPlayer().stopUsingItem();
                    }
                    break;
                case "Timer":
                    if (mc.thePlayer.isUsingItem() && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
                        getTimer().timerSpeed = 5.0F;
                        int i = getPlayer().getCurrentEquippedItem().getMaxItemUseDuration() - getPlayer().getItemInUseCount();
                        if (i >= 18) {
                            getTimer().timerSpeed = 1.0F;
                            getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, getPlayer().getPosition().ORIGIN, EnumFacing.DOWN));
                            getPlayer().stopUsingItem();
                        }
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

    }
}
