package koks.module.impl.render;

import koks.event.Event;
import koks.module.Module;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

/**
 * @author kroko
 * @created on 04.10.2020 : 21:08
 */
public class NameProtect extends Module {

    public NameProtect() {
        super("NameProtect", "Your name are hidden", Category.RENDER);
    }

    @Override
    public void onEvent(Event event) {
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
