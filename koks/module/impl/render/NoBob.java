package koks.module.impl.render;

import koks.event.Event;
import koks.event.impl.EventBobbing;
import koks.module.Module;
import koks.module.ModuleInfo;

/**
 * @author avox | lmao | kroko
 * @created on 17.09.2020 : 09:01
 */

@ModuleInfo(name = "NoBob", description = "Your hand doesn't bobbing", category = Module.Category.RENDER)
public class NoBob extends Module {

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventBobbing) {
            ((EventBobbing) event).setBobbing(0);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}