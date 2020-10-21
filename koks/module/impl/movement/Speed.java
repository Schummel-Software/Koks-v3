package koks.module.impl.movement;

import koks.api.settings.Setting;
import koks.api.util.MovementUtil;
import koks.api.util.RandomUtil;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.MathHelper;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 18:17
 */

@ModuleInfo(name = "Speed", description = "In germany we call it rasant", category = Module.Category.MOVEMENT)
public class Speed extends Module {

    public Setting mode = new Setting("Mode", new String[]{"MCCentral", "Legit"}, "Legit", this);
    private final MovementUtil movementUtil = new MovementUtil();

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode());
            switch (mode.getCurrentMode()) {
                case "Legit":
                    getGameSettings().keyBindSprint.pressed = false;
                    if(getPlayer().onGround) {
                        getPlayer().jump();
                        getPlayer().setSprinting(false);
                        movementUtil.setSpeed(0.15, true);
                        getTimer().timerSpeed = 2F;
                    }else{
                        getPlayer().setSprinting(true);
                        getPlayer().speedInAir = 0.23F;
                    }
                    if(getPlayer().motionY >= 0.3) {
                        getPlayer().motionY = 0;
                    }

                    break;
                case "Mineplex":
                    if (!mc.thePlayer.isInWeb) {
                        if (getPlayer().onGround && isMoving())
                            getPlayer().jump();
                        if (isMoving()) {
                            movementUtil.setSpeed(0.35D, true);
                            getPlayer().speedInAir = 0.044F;
                        }
                    }
                    break;
                case "MCCentral":
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    } else {
                        if (isMoving())
                            movementUtil.setSpeed(0.7, true);
                    }
                    break;
            }
        }

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        mc.thePlayer.speedInAir = 0.02F;
    }

}