package koks.manager.event;

import koks.Koks;
import koks.manager.module.Module;

/**
 * @author deleteboys | kroko
 * @created on 12.09.2020 : 20:39
 */
public class EventManager {

    public void onEvent(Event e) {
        for (Module module : Koks.getKoks().moduleManager.getModules()) {
            module.onEvent(e);
        }
    }
}
