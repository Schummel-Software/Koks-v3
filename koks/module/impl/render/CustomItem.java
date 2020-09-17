package koks.module.impl.render;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.module.Module;

/**
 * @author avox | lmao | kroko
 * @created on 17.09.2020 : 11:39
 */
public class CustomItem extends Module {

    public Setting x = new Setting("X", 0.0F, -0.2F, 0.4F, false, this);
    public Setting y = new Setting("Y", 0.0F, -0.3F, 0.5F, false, this);
    public Setting z = new Setting("Z", 0.0F, -0.3F, 0.1F, false, this);
    public Setting size = new Setting("Size", 0.4F, 0.1F, 0.8F, false, this);

    public CustomItem() {
        super("CustomItem", "Customize the position of you item", Category.RENDER);
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