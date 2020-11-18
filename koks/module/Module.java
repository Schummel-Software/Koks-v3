package koks.module;

import koks.Koks;
import koks.api.Methods;
import koks.api.settings.SettingInfo;
import koks.api.util.*;
import koks.event.Event;
import koks.api.settings.Setting;
import koks.module.impl.player.SendPublic;

import java.awt.*;
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

    public TimeHelper timeHelper = new TimeHelper();

    public ESPUtil espUtil;
    public Logger logger;
    public LoginUtil loginUtil;
    public MovementUtil movementUtil;
    public RandomUtil randomUtil;
    public RayCastUtil rayCastUtil;
    public RenderUtil renderUtil;
    public RotationUtil rotationUtil;
    public InventoryUtil inventoryUtil;

    public Module() {
        ModuleInfo moduleInfo = getClass().getAnnotation(ModuleInfo.class);
        this.category = moduleInfo.category();
        this.name = moduleInfo.name();
        this.description = moduleInfo.description();

        espUtil = Koks.getKoks().wrapper.espUtil;
        logger = Koks.getKoks().wrapper.logger;
        loginUtil = Koks.getKoks().wrapper.loginUtil;
        movementUtil = Koks.getKoks().wrapper.movementUtil;
        randomUtil = Koks.getKoks().wrapper.randomUtil;
        rayCastUtil = Koks.getKoks().wrapper.rayCastUtil;
        renderUtil = Koks.getKoks().wrapper.renderUtil;
        rotationUtil = Koks.getKoks().wrapper.rotationUtil;
        inventoryUtil = Koks.getKoks().wrapper.inventoryUtil;

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
        setToggled(!isToggled());
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        try {
            if (!this.toggled) {
                animation = 0;
                onDisable();
            } else {
                animation = 0;
                onEnable();
            }

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
        COMBAT, MOVEMENT, RENDER, GUI, UTILITIES, PLAYER, DEBUG, WORLD;


        public Color getCategoryColor() {
            switch (this) {
                case COMBAT:
                    return new Color(0xFF555D);
                case MOVEMENT:
                    return new Color(0xDEE955);
                case RENDER:
                    return new Color(0xFC56FF);
                case GUI:
                    return new Color(0xFF8056);
                case UTILITIES:
                    return new Color(0xCCCCCC);
                case PLAYER:
                    return new Color(0x52EE61);
                case DEBUG:
                    return new Color(0x3AEFB6);
                case WORLD:
                    return new Color(0x4CDDF3);
            }
            return Color.white;
        }
    }
}
