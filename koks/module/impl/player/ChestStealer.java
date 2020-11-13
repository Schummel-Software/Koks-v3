package koks.module.impl.player;

import koks.Koks;
import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.api.settings.Setting;
import koks.module.ModuleInfo;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 16:37
 */

@ModuleInfo(name = "ChestStealer", description = "You steal the items from a chest", category = Module.Category.PLAYER)
public class ChestStealer extends Module {

    public Setting startDelay = new Setting("Start Delay", 100.0F, 0.0F, 500.0F, true, this);
    public Setting takeDelay = new Setting("Take Delay", 100.0F, 0.0F, 150.0F, true, this);
    private final TimeHelper startTimer = timeHelper;
    private final TimeHelper throwTimer = new TimeHelper();
    private InventoryCleaner inventoryCleaner;

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            setInfo(Math.round(takeDelay.getCurrentValue()) + "");
            if (mc.currentScreen instanceof GuiChest) {
                if (!startTimer.hasReached((long) startDelay.getCurrentValue()))
                    return;
                ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
                boolean empty = true;
                if(chest.getLowerChestInventory().getDisplayName().getFormattedText().contains("Chest")) {
                    for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
                        if (chest.getSlot(i).getHasStack() && chest.getSlot(i).getStack() != null && !inventoryCleaner.trashItems.contains(chest.getSlot(i).getStack().getItem())) {
                            if (throwTimer.hasReached((long) ((long) takeDelay.getCurrentValue() + randomUtil.getRandomGaussian(20)))) {
                                mc.playerController.windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
                                throwTimer.reset();
                            }
                            empty = false;
                            break;
                        }
                    }
                    if (empty) {
                        mc.thePlayer.closeScreen();
                    }
                }

            } else {
                startTimer.reset();
            }
        }
    }

    @Override
    public void onEnable() {
        inventoryCleaner = (InventoryCleaner) Koks.getKoks().moduleManager.getModule(InventoryCleaner.class);
    }

    @Override
    public void onDisable() {

    }

}