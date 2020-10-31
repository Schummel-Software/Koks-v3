package koks.module.impl.movement;

import god.buddy.aot.BCompiler;
import koks.Koks;
import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 18:17
 */

@ModuleInfo(name = "Speed", description = "In germany we call it rasant", category = Module.Category.MOVEMENT)
public class Speed extends Module {

    public Setting mode = new Setting("Mode", new String[]{"Intave", "MCCentral", "Mineplex", "Mineplex FAST", "AAC3.3.12", "AAC4", "Tired", "Legit"}, "Intave", this);

    public float mineplexMotion;

    public int aacSpeed;

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode());
            switch (mode.getCurrentMode()) {
                case "Legit":
                    if(getPlayer().onGround)
                        getPlayer().jump();
                    getPlayer().setSprinting(true);
                    break;
                case "AAC4":
                    if (getPlayer().onGround) {
                        aacSpeed++;
                        getPlayer().jump();
                        if (aacSpeed <= 3)
                            getTimer().timerSpeed = 25;
                        else
                            getTimer().timerSpeed = 0.045F;
                    } else {
                        getTimer().timerSpeed = 1.0F;
                    }

                    if(aacSpeed >= 4)
                        aacSpeed = 0;
                    break;
                case "Intave":
                    getPlayer().setSprinting(true);
                    getPlayer().addExhaustion(0.8F);
                    if (getPlayer().onGround && isMoving()) {
                        getPlayer().jump();
                    } else {
                        if (getPlayer().fallDistance >= 0.7)
                            getPlayer().motionY -= 0.01955;
                    }
                    break;
                case "Tired":
                    getGameSettings().keyBindSprint.pressed = false;
                    if (getPlayer().onGround && isMoving()) {
                        getPlayer().jump();
                        getPlayer().setSprinting(false);
                        movementUtil.setSpeed(0.06, true);
                        getTimer().timerSpeed = 5F;
                    } else {
                        getPlayer().setSprinting(true);
                        getPlayer().speedInAir = 0.23F;
                    }
                    if (getPlayer().motionY >= 0.3) {
                        getPlayer().motionY = 0;
                    }
                    break;
                case "AAC3.3.12":
                    if (isMoving()) {
                        if (getPlayer().onGround) {
                            getPlayer().jump();
                        } else {
                            getPlayer().motionY -= 0.022;
                            getPlayer().jumpMovementFactor = 0.032F;
                        }
                    }
                    break;
                case "Mineplex FAST":
                    if (!mc.thePlayer.isInWeb) {

                        if (getPlayer().isCollidedHorizontally) {
                            setMotion(0);
                            mineplexMotion = 0.02F;
                        }

                        if (isMoving()) {
                            if (getPlayer().onGround) {
                                getPlayer().motionX = getPlayer().motionZ = 0;
                                mineplexMotion += 0.25F;
                                getPlayer().jump();
                            } else {

                                /*if(getPlayer().fallDistance >= 0.41 && getPlayer().fallDistance <= 0.43) {
                                    getPlayer().motionY += 0.42F;
                                }*/
                                mineplexMotion -= mineplexMotion / 64;
                                movementUtil.setSpeed(mineplexMotion, false);
                            }
                        } else {
                            mineplexMotion = 0.02F;
                        }
                    }
                    break;
                case "Mineplex":
                    if (!mc.thePlayer.isInWeb) {
                        if (getPlayer().onGround && isMoving())
                            getPlayer().jump();

                        if (isMoving()) {
                            getPlayer().setSprinting(false);
                            getGameSettings().keyBindSprint.pressed = false;
                            movementUtil.setSpeed(0.35D, true);
                            getPlayer().speedInAir = 0.044F;
                        }
                    }
                    break;
                case "MCCentral":
                    if (mc.thePlayer.onGround && isMoving()) {
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
        mineplexMotion = 0.2F;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        mc.thePlayer.speedInAir = 0.02F;
    }

}