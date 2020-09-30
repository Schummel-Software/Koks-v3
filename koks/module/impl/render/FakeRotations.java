package koks.module.impl.render;

import koks.event.Event;
import koks.module.Module;

/**
 * @author kroko
 * @created on 26.09.2020 : 14:07
 */
public class FakeRotations extends Module {

    public FakeRotations() {
        super("FakeRotations", "You doesn't rotate in third person", Category.RENDER);
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
