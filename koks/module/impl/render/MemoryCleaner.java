package koks.module.impl.render;

import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;

/**
 * @author phatom | dirt | deleteboys | lmao | kroko
 * @created on 18.09.2020 : 21:29
 */
public class MemoryCleaner extends Module {

    public MemoryCleaner() {
        super("MemoryCleaner", "Its hold your memory clean", Category.RENDER);
    }

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            if(Runtime.getRuntime().totalMemory() >= ((Runtime.getRuntime().maxMemory() / 2))){
                sendmsg("§aCleared Memory §7(§c" + Runtime.getRuntime().totalMemory() + "§7/§e" + Runtime.getRuntime().maxMemory() + "§7)", true);
                System.gc();

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
