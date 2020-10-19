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
                    if (isMoving() && getPlayer().motionY >= 0.38799855) {
                        getPlayer().motionY -= 0.01;
                    }

                    getPlayer().speedInAir = 0.023F;
                    getPlayer().jumpMovementFactor = 0.0305F;
                    getPlayer().addExhaustion(0.8F);
                    getPlayer().isAirBorne = true;

                    if (getPlayer().onGround)
                        getPlayer().setSprinting(true);

                    boolean back = getGameSettings().keyBindBack.pressed;
                    int backYaw = back ? 180 : 0;
                    float f = (getPlayer().rotationYaw + backYaw) * 0.017453292F;
                    if (getHurtTime() == 0 && getPlayer().onGround && isMoving()) {
                        if (getPlayer().isSprinting()) {
                            getPlayer().motionX -= MathHelper.sin(f) * 0.2F;
                            getPlayer().motionZ += MathHelper.cos(f) * 0.2F;
                        }
                    }

                    if (mc.thePlayer.onGround) {
                        if (isMoving()) getPlayer().motionY = 0.42F;
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