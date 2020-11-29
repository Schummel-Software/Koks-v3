package koks.module.impl.movement;

import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.org.apache.xpath.internal.operations.Bool;
import god.buddy.aot.BCompiler;
import koks.Koks;
import koks.api.settings.Setting;
import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.event.impl.EventDamage;
import koks.event.impl.EventPacket;
import koks.event.impl.EventTick;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 20:55
 */

@ModuleInfo(name = "Fly", description = "Flying around the world", category = Module.Category.MOVEMENT)
public class Fly extends Module {

    public Setting aac1910speed = new Setting("AAC1.9.10-Speed", 2.34F, 0F, 2.8F, false, this);
    public Setting aac1910motion = new Setting("AAC1.9.10-MotionY", 0.8F, 0F, 1.1F, false, this);

    public Setting aac3312boost = new Setting("AAC3.3.12-Boost", 9F, 1F, 10F, true, this);
    public Setting mode = new Setting("Mode", new String[]{"AAC3.3.12", "AAC1.9.10", "Redesky", "MCCentral", "CubeCraft", "Verus", "Bizzi", "Vanilla"}, "AAC3.3.12", this);
    public TimeHelper damageTime = new TimeHelper();

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            String extra = mode.getCurrentMode().equalsIgnoreCase("AAC3.3.12") ? " [" + aac3312boost.getCurrentValue() + "]" : "";
            setInfo(mode.getCurrentMode() + extra);
        }
        switch (mode.getCurrentMode()) {
            case "Vanilla":
                if(event instanceof EventUpdate) {
                    getPlayer().capabilities.isFlying = true;
                    getPlayer().capabilities.allowFlying = true;
                }
                break;
            case "Verus":
                if (event instanceof EventUpdate) {
                    getTimer().timerSpeed = 0.9F;
                    if (!getPlayer().onGround) {
                        getPlayer().capabilities.isFlying = false;
                        getPlayer().capabilities.isCreativeMode = true;
                        getPlayer().cameraYaw = 0.05F;

                        if (getPlayer().motionY < -0.4) {
                            float speed = 3F;
                            double motionX = -Math.sin(Math.toRadians(movementUtil.getDirection(mc.thePlayer.rotationYaw))) * speed;
                            double motionZ = Math.cos(Math.toRadians(movementUtil.getDirection(mc.thePlayer.rotationYaw))) * speed;

                            getPlayer().setPosition(getX() + motionX, getY(), getZ() + motionZ);
                            getPlayer().motionY *= -1.01;
                        }

                    }
                }
                break;
            case "AAC1.9.10":
                if (event instanceof EventUpdate) {
                    if (getPlayer().fallDistance >= 3) {
                        sendPacket(new C03PacketPlayer(true));
                        getPlayer().fallDistance = 0;
                        if (getHurtTime() != 0) {
                            getPlayer().motionY = aac1910motion.getCurrentValue();
                        }
                    }
                    if (!getPlayer().onGround && (getPlayer().hurtTime != 0 || getPlayer().hurtResistantTime != 0))
                        movementUtil.setSpeed(0.25F * aac1910speed.getCurrentValue(), true);
                }

                break;
            case "AAC3.3.12":
                if (event instanceof EventUpdate) {
                    if (mc.thePlayer.posY <= -70) {
                        mc.thePlayer.motionY = aac3312boost.getCurrentValue();
                    }
                }
                break;
            case "Redesky":
                if (event instanceof EventUpdate) {
                    double motionX = -Math.sin(Math.toRadians(movementUtil.getDirection(getPlayer().rotationYaw))) * 6;
                    double motionZ = Math.cos(Math.toRadians(movementUtil.getDirection(getPlayer().rotationYaw))) * 6;

                    double flagX = -Math.sin(Math.toRadians(movementUtil.getDirection(getPlayer().rotationYaw))) * 7.5;
                    double flagZ = Math.cos(Math.toRadians(movementUtil.getDirection(getPlayer().rotationYaw))) * 7.5;

                    if (timeHelper.hasReached(190)) {
                        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX() + flagX, getY() + randomUtil.getRandomDouble(1.1, 1.2), getZ() + flagZ, true));
                        getPlayer().setPosition(getX() + motionX, getY(), getZ() + motionZ);
                        timeHelper.reset();
                    }
                }
                break;
            case "Bizzi":
                if (event instanceof EventUpdate) {
                    if (getGameSettings().keyBindSneak.pressed) {
                        getPlayer().motionY = 5.0;
                    }
                    if (getGameSettings().keyBindForward.pressed) {
                        getPlayer().jump();
                        getTimer().timerSpeed = 0.5f;
                        getPlayer().motionY = -0.5;
                    } else {
                        getTimer().timerSpeed = 0.7f;
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
                    getPlayer().motionY = randomUtil.getRandomDouble(-0.01, 0.01);

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

        if (mode.getCurrentMode().equalsIgnoreCase("Verus")) {
            getPlayer().jump();
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