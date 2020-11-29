package koks.module.impl.render;

import koks.event.Event;
import koks.event.impl.EventMotion;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;

/**
 * @author kroko
 * @created on 22.11.2020 : 17:40
 */

@ModuleInfo(name = "AntiAim", description = "You have no aim", category = Module.Category.RENDER)
public class AntiAim extends Module {

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventMotion) {
            if (((EventMotion) event).getType() == EventMotion.Type.PRE) {
                if (getPlayer().swingProgress == 0 && !getGameSettings().keyBindAttack.pressed && !getGameSettings().keyBindUseItem.pressed) {
                    ((EventMotion) event).setYaw(getPlayer().rotationYaw + randomUtil.getRandomFloat(145F, 180F));
                    ((EventMotion) event).setPitch(randomUtil.getRandomFloat(50, 90));
                } else {
                    ((EventMotion) event).setYaw(getPlayer().rotationYaw);
                    ((EventMotion) event).setPitch(getPlayer().rotationPitch);
                }
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
