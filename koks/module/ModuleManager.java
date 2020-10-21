package koks.module;

import koks.api.util.ClassUtil;
import koks.command.impl.Friend;
import koks.module.impl.combat.*;
import koks.module.impl.debug.Debug;
import koks.module.impl.debug.Test;
import koks.module.impl.gui.ClickGui;
import koks.module.impl.gui.HUD;
import koks.module.impl.movement.*;
import koks.module.impl.player.*;
import koks.module.impl.render.*;
import koks.module.impl.world.Scaffold;
import org.reflections.util.ClasspathHelper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:37
 */
public class ModuleManager {

    public ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {

        /* if(!Koks.getKoks().purvesManager.getUser().getRole().equals(Role.Developer)) {
            removeCategory(Module.Category.DEBUG);
        }*/
        addModule(new Criticals());
        addModule(new FastBow());
        addModule(new Friends());
        addModule(new KillAura());
        addModule(new Rotations());
        addModule(new SuperHit());
        addModule(new Teams());
        addModule(new TNTBlock());
        addModule(new Velocity());
        addModule(new Debug());
        addModule(new Test());
        addModule(new ClickGui());
        addModule(new HUD());
        addModule(new BoatFly());
        addModule(new Fly());
        addModule(new InvMove());
        addModule(new Jesus());
        addModule(new NoCobweb());
        addModule(new NoFall());
        addModule(new NoSlowdown());
        addModule(new Speed());
        addModule(new Sprint());
        addModule(new StairSpeed());
        addModule(new Step());
        addModule(new VClip());
        addModule(new AutoArmor());
        addModule(new ChestStealer());
        addModule(new CivBreak());
        addModule(new FastPlace());
        addModule(new GodMode());
        addModule(new InventoryCleaner());
        addModule(new Phase());
        addModule(new SendPublic());
        addModule(new SetBack());
        addModule(new Teleport());
        addModule(new BlockOverlay());
        addModule(new CameraClip());
        addModule(new ChestESP());
        addModule(new CustomItem());
        addModule(new FakeRotations());
        addModule(new ItemESP());
        addModule(new MemoryCleaner());
        addModule(new NameProtect());
        addModule(new NameTags());
        addModule(new NoBob());
        addModule(new NoFov());
        addModule(new NoHurtCam());
        addModule(new PlayerESP());
        addModule(new TrailESP());
        addModule(new Scaffold());

        modules.sort(Comparator.comparing(Module::getName));
    }

    public void removeCategory(Module.Category category) {
        for (Module module : getModules()) {
            if (module.getCategory().equals(category)) {
                modules.remove(module);
            }
        }
    }

    public void addModule(Module module) {
        modules.add(module);
    }

    public Module getModule(Class<? extends Module> clazz) {
        for (Module module : getModules()) {
            if (module.getClass() == clazz) {
                return module;
            }
        }
        return null;
    }

    public Module getModule(String name) {
        for (Module module : getModules()) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }
}
