package koks.module.impl.combat;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventRender3D;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.item.ItemSword;
import org.lwjgl.Sys;

import java.awt.*;

/**
 * @author kroko
 * @created on 06.10.2020 : 19:06
 */

@ModuleInfo(name = "TNTBlock", description = "its block for you when your in a range from a tnt", category = Module.Category.COMBAT)
public class TNTBlock extends Module {

    public Setting blockFuse = new Setting("BlockTime", 5 , 3, 80, true, this);

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            for (Entity entity : getWorld().loadedEntityList) {
                if (getPlayer().getCurrentEquippedItem().getItem() instanceof ItemSword) {
                    if (entity instanceof EntityTNTPrimed) {
                        EntityTNTPrimed entityTNTPrimed = (EntityTNTPrimed) entity;
                        console.log("test");
                        if (entity.getDistanceToEntity(getPlayer()) <= 8) {
                            if (entityTNTPrimed.fuse == 0) {
                                getGameSettings().keyBindUseItem.pressed = false;
                            } else if (entityTNTPrimed.fuse <= blockFuse.getCurrentValue()) {
                                getGameSettings().keyBindUseItem.pressed = true;
                            } else {
                                getGameSettings().keyBindUseItem.pressed = false;
                            }
                        } else {
                            getGameSettings().keyBindUseItem.pressed = false;
                        }
                    }
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
