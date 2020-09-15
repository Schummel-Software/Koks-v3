package koks.module;

import koks.Koks;
import koks.api.Methods;
import koks.event.Event;
import koks.api.settings.Setting;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:36
 */
public abstract class Module extends Methods {

    private String name,description, info = "";
    private int key;
    private double animation;
    private Category category;
    private boolean toggled,bypass;

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.bypass = false;
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
        if(!this.toggled) {
            animation = 0;
            onDisable();
        } else {
            animation = 0;
            onEnable();
        }
    }

    public void setToggled(boolean toggled) {
        if(this.toggled) {
            animation = 0;
            onDisable();
        } else {
            animation = 0;
            onEnable();
        }
        this.toggled = toggled;
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

    public enum Category{
        COMBAT, MOVEMENT, RENDER, GUI, UTILITIES, PLAYER, DEBUG, WORLD
    }
}
