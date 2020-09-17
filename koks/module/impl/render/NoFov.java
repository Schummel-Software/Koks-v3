package koks.module.impl.render;

import koks.event.Event;
import koks.module.Module;

/**
 * @author avox | lmao | kroko
 * @created on 17.09.2020 : 09:01
 */
public class NoFov extends Module {

    public NoFov() {
        super("NoFov", "No more Fov changed while sprinting", Category.RENDER);
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