package koks.module.impl.render;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventFOV;
import koks.event.impl.EventUpdate;
import koks.module.Module;

/**
 * @author avox | lmao | kroko
 * @created on 17.09.2020 : 09:01
 */
public class NoFov extends Module {

    public Setting fov = new Setting("FOV", 1F,0.001F, 2F,false,this);

    public NoFov() {
        super("NoFov", "No more Fov changed while sprinting", Category.RENDER);
    }

    @Override
    public void onEvent(Event event) {
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