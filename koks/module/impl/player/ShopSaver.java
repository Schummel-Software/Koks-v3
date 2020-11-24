package koks.module.impl.player;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventKeyPress;
import koks.event.impl.EventPacket;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;

/**
 * @author kroko
 * @created on 17.11.2020 : 04:23
 */

@ModuleInfo(name = "ShopSaver", description = "You can open the shop everywhere", category = Module.Category.PLAYER)
public class ShopSaver extends Module {

    public Setting shopOpen = new Setting("Open Shop", Keyboard.KEY_U, this);

    GuiScreen screen;

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacket) {
            Packet<? extends INetHandler> packet = ((EventPacket) event).getPacket();
            if (packet instanceof C0DPacketCloseWindow || packet instanceof S2EPacketCloseWindow) {
                event.setCanceled(true);
            }
        }

        if (event instanceof EventKeyPress) {
            if(((EventKeyPress) event).getKey() == shopOpen.getKey() && screen != null) {
                event.setCanceled(true);
                mc.displayGuiScreen(screen);
            }
        }

        if (event instanceof EventUpdate) {
            if (mc.currentScreen != null)
                if (mc.currentScreen instanceof GuiContainer) {
                    if (screen != mc.currentScreen) {
                        sendmsg("§aShop Saved", true);
                    }
                    screen = mc.currentScreen;
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
