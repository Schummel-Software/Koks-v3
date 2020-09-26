package koks.module.impl.movement;

import koks.api.settings.Setting;
import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * @author kroko
 * @created on 26.09.2020 : 03:01
 */
public class InvMove extends Module {

    private final TimeHelper timeHelper = new TimeHelper();

    public Setting jump = new Setting("Jump", true, this);
    public Setting sprint = new Setting("Sprint", true, this);
    public Setting look = new Setting("Look", true, this);

    public InvMove() {
        super("InvMove", "You can walk in the inventory", Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode());
            mc.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode());
            mc.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode());
            mc.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode());
            if (jump.isToggled())
                mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
            if (sprint.isToggled())
                mc.gameSettings.keyBindSprint.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode());
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindPlayerList.getKeyCode()) && look.isToggled()) {
                if (mc.currentScreen != null) {
                    if (timeHelper.hasReached(200)) {
                        mc.inGameHasFocus = !mc.inGameHasFocus;
                        if (mc.inGameHasFocus) {
                            mc.mouseHelper.grabMouseCursor();
                        } else {
                            KeyBinding.unPressAllKeys();
                            mc.mouseHelper.ungrabMouseCursor();
                        }
                        timeHelper.reset();
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
