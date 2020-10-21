package koks.module;

import koks.Koks;
import koks.api.Methods;
import koks.api.settings.SettingInfo;
import koks.event.Event;
import koks.api.settings.Setting;
import koks.module.impl.player.SendPublic;

import java.lang.reflect.Field;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:36
 */
public abstract class Module extends Methods {

    private String name, description, info = "";
    private int key;
    private double animation;
    private Category category;
    private boolean toggled, bypass;

    public Module() {
        ModuleInfo moduleInfo = getClass().getAnnotation(ModuleInfo.class);
        this.category = moduleInfo.category();
        this.name = moduleInfo.name();
        this.description = moduleInfo.description();

        /* for(Field field : getClass().getDeclaredFields()) {
            try{
                SettingInfo settingInfo = field.getAnnotation(SettingInfo.class);
                switch (field.getType()) {
                    case Float.class:
                        registerSetting(new Setting(settingInfo.name(), field.getFloat()));
                        break;
                }
            }catch (Exception ignore) {
            }
        }*/
    }

    public boolean isBypass() {
        return bypass;
    }

    public void setBypass(boolean bypass) {
        this.bypass = bypass;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void toggle() {
        this.toggled = !this.toggled;
        if (!this.toggled) {
            animation = 0;
            if (getWorld() != null)
                onDisable();
        } else {
            animation = 0;
            if (getWorld() != null)
                onEnable();
        }

        try {
            if (Koks.getKoks().moduleManager.getModule(SendPublic.class).isToggled()) {
                String toggle = toggled ? "Enabled" : "Disabled";
                getPlayer().sendChatMessage(toggle + " " + this.getName());
            }
        } catch (Exception ignored) {
        }
    }

    public void setToggled(boolean toggled) {
        if (this.toggled) {
            animation = 0;
            if (getWorld() != null)
                onDisable();
        } else {
            animation = 0;
            if (getWorld() != null)
                onEnable();
        }
        this.toggled = toggled;

        try {
            if (Koks.getKoks().moduleManager.getModule(SendPublic.class).isToggled()) {
                String toggle = toggled ? "Enabled" : "Disabled";
                getPlayer().sendChatMessage(toggle + " " + this.getName());
            }
        } catch (Exception ignored) {
        }

    }

    public abstract void onEvent(Event event);

    public abstract void onEnable();

    public abstract void onDisable();

    public void registerSetting(Setting setting) {
        Koks.getKoks().settingsManager.registerSetting(setting);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info.equals("") ? "" : " " + info;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public double getAnimation() {
        return animation;
    }

    public void setAnimation(double animation) {
        this.animation = animation;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getArrayName(String colorCode, boolean showTags) {
        return name + (showTags ? colorCode + info : "");
    }

    public enum Category {
        COMBAT, MOVEMENT, RENDER, GUI, UTILITIES, PLAYER, DEBUG, WORLD
    }
}
