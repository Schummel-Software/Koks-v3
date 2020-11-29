package koks.module.impl.player;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author kroko
 * @created on 15.11.2020 : 22:06
 */

@ModuleInfo(name = "AntiVoid", description = "You doesn't fall in to the void", category = Module.Category.PLAYER)
public class AntiVoid extends Module {

    public Setting fallDistance = new Setting("Fall Distance", 3, 3, 6, false, this);
    public Setting mode = new Setting("Mode", new String[]{"AAC1.9.10"}, "AAC1.9.10", this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            switch (mode.getCurrentMode()) {
                case "AAC1.9.10":
                    if (getPlayer().fallDistance > (fallDistance.getCurrentValue() > 4.5 ? 4.5 : fallDistance.getCurrentValue()) && !getPlayer().onGround) {
                        sendPacket(new C03PacketPlayer(true));
                        if (getHurtTime() != 0) {
                            getPlayer().fallDistance = 0;
                            getPlayer().motionY = 1.5;
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
