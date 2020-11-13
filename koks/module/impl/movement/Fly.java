package koks.module.impl.movement;

import com.sun.org.apache.xpath.internal.operations.Bool;
import god.buddy.aot.BCompiler;
import koks.Koks;
import koks.api.settings.Setting;
import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.*;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 20:55
 */

@ModuleInfo(name = "Fly", description = "Flying around the world", category = Module.Category.MOVEMENT)
public class Fly extends Module {

    public Setting aac3312boost = new Setting("AAC3.3.12-Boost", 9F, 1F, 10F, true, this);
    public Setting mode = new Setting("Mode", new String[]{"AAC3.3.12", "MCCentral", "CubeCraft", "Verus"}, "AAC3.3.12", this);
    public TimeHelper damageTime = new TimeHelper();

    @BCompiler(aot = BCompiler.AOT.NORMAL)
    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            String extra = mode.getCurrentMode().equalsIgnoreCase("AAC3.3.12") ? " [" + aac3312boost.getCurrentValue() + "]" : "";
            setInfo(mode.getCurrentMode() + extra);
        }
        switch (mode.getCurrentMode()) {
            case "Verus":
                if (event instanceof EventUpdate) {
                    if(!getPlayer().onGround) {
                        getPlayer().motionY = 0;
                        getPlayer().motionY = randomUtil.getRandomFloat(-0.05F, 0.05F);
                        getPlayer().capabilities.isFlying = false;
                        getPlayer().capabilities.isCreativeMode = true;
                        getPlayer().cameraYaw = 0.05F;


                        if(timeHelper.hasReached(250)) {
                            float speed = 0.9F;
                            double motionX = -Math.sin(Math.toRadians(movementUtil.getDirection(mc.thePlayer.rotationYaw))) * speed;
                            double motionZ = Math.cos(Math.toRadians(movementUtil.getDirection(mc.thePlayer.rotationYaw))) * speed;

                            getPlayer().setPosition(getX() + motionX, getY(), getZ() + motionZ);
                            timeHelper.reset();
                        }

                    }
                }
                break;
            case "AAC3.3.12":
                if (event instanceof EventUpdate) {
                    if (mc.thePlayer.posY <= -70) {
                        mc.thePlayer.motionY = aac3312boost.getCurrentValue();
                    }
                }
                break;
            case "MCCentral":
                if (event instanceof EventUpdate) {
                    mc.thePlayer.motionY = 0;
                    if (isMoving())
                        movementUtil.setSpeed(0.8, true);
                    if (mc.gameSettings.keyBindJump.isKeyDown())
                        mc.thePlayer.motionY = 0.5;
                    if (mc.gameSettings.keyBindSneak.isKeyDown())
                        mc.thePlayer.motionY = -0.5;
                }
                break;
            case "CubeCraft":
                if (event instanceof EventUpdate) {

                    getPlayer().motionY = 0;
                    getTimer().timerSpeed = 1F;

                    switch (mc.thePlayer.ticksExisted % 10) {
                        case 0:
                        case 6:
                            getPlayer().motionY -= randomUtil.getRandomFloat(0.01F, 0.05F);
                            break;
                        case 2:
                        case 3:
                        case 4:
                            float speed = 0.9F;
                            double motionX = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw)) * speed;
                            double motionZ = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw)) * speed;

                            getPlayer().setPosition(getX() + motionX, getY(), getZ() + motionZ);
                            getPlayer().motionY += 0.2F;
                            break;
                        case 9:
                            getPlayer().motionX = 0;
                            getPlayer().motionZ = 0;
                            break;
                    }

                   /* mc.thePlayer.motionY = 0.0;
                    mc.timer.timerSpeed = 0.3F;
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                        mc.thePlayer.motionY -= 0.01;
                    }

                    if (damageTime.hasReached(1500)) {


                        damageTime.reset();
                    }

                    if (timeHelper.hasReached(800)) {
                        movementUtil.setSpeed(0.4, false);
                        timeHelper.reset();
                    }*/
                }
                break;
        }

    }

    @Override
    public void onEnable() {
        timeHelper.reset();
        damageTime.reset();

        if (getPlayer().onGround && mode.getCurrentMode().equalsIgnoreCase("Verus")) {
            getPlayer().motionY = 0.7;
        }

    }

    @Override
    public void onDisable() {
        mc.thePlayer.speedInAir = 0.02F;
        mc.timer.timerSpeed = 1.0F;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
    }

}