package koks.module.impl.render;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.module.Module;
import koks.module.ModuleInfo;

@ModuleInfo(name = "FakeAutoBlock", description = "", category = Module.Category.RENDER)
public class FakeAutoBlock extends Module {

    public Setting onlySword = new Setting("OnlySword", true, this);

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
