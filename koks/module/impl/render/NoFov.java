package koks.module.impl.render;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventFOV;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;

/**
 * @author avox | lmao | kroko
 * @created on 17.09.2020 : 09:01
 */

@ModuleInfo(name = "NoFov", description = "Its set your FOV", category = Module.Category.RENDER)
public class NoFov extends Module {

    public Setting fov = new Setting("FOV", 1F,0.001F, 2F,false,this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if(event instanceof EventUpdate) {
            setInfo(fov.getCurrentValue() + "");
        }
        if(event instanceof EventFOV) {
            ((EventFOV) event).setFOV(fov.getCurrentValue());
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}