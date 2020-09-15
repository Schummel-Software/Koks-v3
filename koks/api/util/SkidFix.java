package koks.api.util;

import koks.event.impl.EventMoveFlying;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 13:43
 */
public class SkidFix {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void applyStrafeToPlayer(EventMoveFlying event, float yaw2) {
        int dif = (int)((MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw - yaw2 - 23.5F - 135.0F) + 180.0F) / 45.0F);
        float strafe = event.getStrafe();
        float forward = event.getForward();
        float friction = event.getFriction();
        float calcForward = 0.0F;
        float calcStrafe = 0.0F;
        switch (dif) {
            case 0:
                calcForward = forward;
                calcStrafe = strafe;
                break;
            case 1:
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            case 2:
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            case 3:
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            case 4:
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            case 5:
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            case 6:
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            case 7:
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
                break;
        }
        if (calcForward > 1.0F || (calcForward < 0.9F && calcForward > 0.3F) || calcForward < -1.0F || (calcForward > -0.9F && calcForward < -0.3F))
            calcForward *= 0.5F;
        if (calcStrafe > 1.0F || (calcStrafe < 0.9F && calcStrafe > 0.3F) || calcStrafe < -1.0F || (calcStrafe > -0.9F && calcStrafe < -0.3F))
            calcStrafe *= 0.5F;
        float d = calcStrafe * calcStrafe + calcForward * calcForward;
        if (d >= 1.0E-4F) {
            d = MathHelper.sqrt_float(d);
            if (d < 1.0F)
                d = 1.0F;
            d = friction / d;
            calcStrafe *= d;
            calcForward *= d;
            float yawSin = MathHelper.sin((float)(yaw2 * Math.PI / 180.0D));
            float yawCos = MathHelper.cos((float)(yaw2 * Math.PI / 180.0D));
            mc.thePlayer.motionX += (calcStrafe * yawCos - calcForward * yawSin);
            mc.thePlayer.motionZ += (calcForward * yawCos + calcStrafe * yawSin);
        }
    }

}