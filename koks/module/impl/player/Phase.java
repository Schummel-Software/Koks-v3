package koks.module.impl.player;

import god.buddy.aot.BCompiler;
import koks.api.util.MovementUtil;
import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.api.settings.Setting;
import koks.module.ModuleInfo;
import net.minecraft.block.BlockClay;
import net.minecraft.world.WorldSettings;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 16:54
 */

@ModuleInfo(name = "Phase", description = "You can walk through walls", category = Module.Category.PLAYER)
public class Phase extends Module {

    public Setting mode = new Setting("Mode", new String[]{"Hive", "Intave"}, "Hive", this);

    public TimeHelper timeHelper = new TimeHelper();

    public void hive() {
        if (getPlayer().isCollidedHorizontally) {
            double motionX = -(Math.sin(Math.toRadians(mc.thePlayer.rotationYaw)) * 0.7);
            double motionZ = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw)) * 0.7;
            mc.thePlayer.setPosition(mc.thePlayer.posX + motionX, mc.thePlayer.posY, mc.thePlayer.posZ + motionZ);
            getPlayer().setSprinting(true);
            mc.thePlayer.motionY = 0;
            mc.thePlayer.onGround = true;
        }
    }

    @BCompiler(aot = BCompiler.AOT.NORMAL)
    public void intave() {
        if (getPlayer().isCollidedHorizontally) {
            if (isMoving()) {
                if (timeHelper.hasReached(150)) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX - Math.sin(Math.toRadians(getDirection())) * 0.1, mc.thePlayer.posY, getPlayer().posZ + Math.cos(Math.toRadians(getDirection())) * 0.1);
                    timeHelper.reset();
                }
            }
        }
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode());
            switch (mode.getCurrentMode()) {
                case "Hive":
                    hive();
                    break;
                case "Intave":
                    intave();
                    break;
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
