package koks.module;

import koks.api.util.ClassUtil;
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
        ClassUtil classUtil = new ClassUtil();
        String prefix = "koks.module.impl.";
        ArrayList<Class> classes = new ArrayList<>();

        try {
            for (Module.Category category : Module.Category.values()) {

                for (Class clazz : classUtil.getClasses(prefix + category.name().toLowerCase())) {
                    if (!classes.contains(clazz)) {
                        classes.add(clazz);
                    }
                }
            }

            for (Class<? extends Module> mod : classes) {
                if (!mod.getName().contains("$"))
                    addModule(mod.newInstance());
            }

        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

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
