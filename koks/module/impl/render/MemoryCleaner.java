package koks.module.impl.render;

import koks.api.settings.Setting;
import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;

/**
 * @author phatom | dirt | deleteboys | lmao | kroko
 * @created on 18.09.2020 : 21:29
 */

@ModuleInfo(name = "MemoryCleaner", description = "Its holds your memory clean", category = Module.Category.RENDER)
public class MemoryCleaner extends Module {

    public Setting memorySize = new Setting("MemorySize", new String[]{"Byte", "Kilobyte", "Megabyte", "Gigabyte", "Terabyte", "Petabyte", "Exabyte"}, "Gigabyte", this);
    public Setting minutes = new Setting("Minutes", 3, 1, 10, true, this);

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {

            setInfo(memorySize.getCurrentMode() + "," + minutes.getCurrentValue());

            if (timeHelper.hasReached((long) (60000 * minutes.getCurrentValue()))) {

                String mode = memorySize.getCurrentMode();
                int memo = mode.equalsIgnoreCase("Byte") ? 0 : mode.equalsIgnoreCase("Kilobyte") ? 1 : mode.equalsIgnoreCase("Megabyte") ? 2 : mode.equalsIgnoreCase("Gigabyte") ? 3 : mode.equalsIgnoreCase("Terabyte") ? 4 : mode.equalsIgnoreCase("Petabyte") ? 5 : mode.equalsIgnoreCase("Exabyte") ? 6 : 0;

                double memory = Math.round((Runtime.getRuntime().totalMemory() / Math.pow(1024.0, memo) * 100));
                double roundedMemory = memory / 100;
                String gigabyte = roundedMemory + "§f" + memorySize.getCurrentMode().substring(0, 1).toUpperCase() + "B";

                sendmsg("§aCleared §e" + (memo != 0 ? gigabyte : Runtime.getRuntime().totalMemory() + "§fB"), true);

                Thread clearMemory = new Thread("clearMemory") {
                    public void run() {
                        System.gc();
                        this.stop();
                    }
                };
                clearMemory.start();
                timeHelper.reset();
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
