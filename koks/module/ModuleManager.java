package koks.module;

import koks.module.impl.combat.*;
import koks.module.impl.debug.*;
import koks.module.impl.gui.*;
import koks.module.impl.movement.*;
import koks.module.impl.player.*;
import koks.module.impl.render.*;
import koks.module.impl.world.Scaffold;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:37
 */
public class ModuleManager {

    public ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        addModule(new BoatFly());
        addModule(new Debug());
        addModule(new ClickGui());
        addModule(new TrailESP());
        addModule(new HUD());
        addModule(new KillAura());
        addModule(new Sprint());
        addModule(new Test());
        addModule(new ChestStealer());
        addModule(new InventoryCleaner());
        addModule(new AutoArmor());
        addModule(new GodMode());
        addModule(new Jesus());
        addModule(new ESP());
        addModule(new NoCobweb());
        addModule(new Scaffold());
        addModule(new Velocity());
        addModule(new NoSlowdown());
        addModule(new BlockOverlay());
        addModule(new NoFall());
        addModule(new Speed());
        addModule(new Fly());
        addModule(new NameTags());
        addModule(new NoFov());
        addModule(new NoBob());
        addModule(new NoHurtCam());

        modules.sort(Comparator.comparing(Module::getName));
    }

    public void addModule(Module module) {
        modules.add(module);
    }

    public Module getModule(Class<? extends Module> clazz) {
        for(Module module : getModules()) {
            if(module.getClass() == clazz) {
                return module;
            }
        }
        return null;
    }

    public Module getModule(String name) {
        for(Module module : getModules()) {
            if(module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }
}
