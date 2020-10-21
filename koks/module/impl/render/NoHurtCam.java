package koks.module.impl.render;

import koks.event.Event;
import koks.event.impl.EventHurtCamera;
import koks.module.Module;
import koks.module.ModuleInfo;

/**
 * @author avox | lmao | kroko
 * @created on 17.09.2020 : 09:01
 */

@ModuleInfo(name = "NoHurtCam", description = "Less cancer while getting damage", category = Module.Category.RENDER)
public class NoHurtCam extends Module {

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventHurtCamera) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}