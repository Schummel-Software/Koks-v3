package koks.module.impl.debug;

import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.event.impl.EventKeyPress;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 11:56
 */
public class Test extends Module {

    private final TimeHelper timeHelper = new TimeHelper();

    public Test() {
        super("Test", "Bugs through the Border", Category.DEBUG);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
    }

}