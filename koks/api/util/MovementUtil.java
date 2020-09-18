package koks.api.util;

import koks.Koks;
import koks.api.Methods;
import koks.event.impl.EventMoveFlying;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;

/**
 * @author deleteboys | lmao | kroko
 * @created on 15.09.2020 : 22:12
 */
public class MovementUtil extends Methods {

    public float getDirection(float rotationYaw) {
        float left = mc.gameSettings.keyBindLeft.pressed ? mc.gameSettings.keyBindBack.pressed ? 45 : mc.gameSettings.keyBindForward.pressed ? -45 : -90 : 0;
        float right = mc.gameSettings.keyBindRight.pressed ? mc.gameSettings.keyBindBack.pressed ? -45 : mc.gameSettings.keyBindForward.pressed ? 45 : 90 : 0;
        float back = mc.gameSettings.keyBindBack.pressed ? +180 : 0;
        float yaw = left + right + back;
        return rotationYaw + yaw;
    }

    public boolean isMoving() {
        return Minecraft.getMinecraft().thePlayer.moveForward != 0 || Minecraft.getMinecraft().thePlayer.moveStrafing != 0;
    }

    public void setSpeed(double speed) {
        if (isMoving()) {
            mc.thePlayer.motionX = -Math.sin(Math.toRadians(getDirection(mc.thePlayer.rotationYaw))) * speed;
            mc.thePlayer.motionZ = Math.cos(Math.toRadians(getDirection(mc.thePlayer.rotationYaw))) * speed;
        }
    }

    public void moveFlying(EventMoveFlying eventMoveFlying, float yaw) {
        float strafe = eventMoveFlying.getStrafe();
        float forward = eventMoveFlying.getForward();
        float friction = eventMoveFlying.getFriction();

        float f = strafe * strafe + forward * forward;

        if (f >= 1.0E-4F) {
            f = MathHelper.sqrt_float(f);

            if (f < 1.0F) {
                f = 1.0F;
            }

            f = friction / f;
            strafe = strafe * f;
            forward = forward * f;

            float f1 = MathHelper.sin(yaw * (float) Math.PI / 180.0F);
            float f2 = MathHelper.cos(yaw * (float) Math.PI / 180.0F);

            mc.thePlayer.motionX += strafe * f2 - forward * f1;
            mc.thePlayer.motionZ += forward * f2 + strafe * f1;
        }
    }

}