package koks.event;

import koks.Koks;
import koks.module.Module;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:39
 */
public class EventManager {

    public void onEvent(Event e) {
        try {
            for (Module module : Koks.getKoks().moduleManager.getModules()) {
                if (module.isToggled()) {
                    module.onEvent(e);
                }
            }
        }catch(Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
