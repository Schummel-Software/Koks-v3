package koks.module.impl.combat;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.module.Module;

/**
 * @author avox | lmao | kroko
 * @created on 17.09.2020 : 20:10
 */
public class Rotations extends Module {

    public Setting smooth = new Setting("Smooth", true, this);
    public Setting precision = new Setting("Precision", 0.1F, 0.05F, 0.50F, false, this);
    public Setting accuracy = new Setting("Accuracy", 0.3F, 0.1F, 0.8F, false, this);
    public Setting predictionMultiplier = new Setting("Prediction Multiplier", 0.4F, 0.0F, 1.0F, false, this);
    public Setting lockView = new Setting("LockView", false, this);

    public Rotations() {
        super("Rotations", "Set up your KillAura Rotations here", Category.COMBAT);
    }

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}