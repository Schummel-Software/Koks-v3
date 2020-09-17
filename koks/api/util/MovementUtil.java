package koks.api.util;

import koks.api.Methods;
import net.minecraft.client.Minecraft;

/**
 * @author deleteboys | lmao | kroko
 * @created on 15.09.2020 : 22:12
 */
public class MovementUtil extends Methods {

    public float getDirection(float rotationYaw) {
        float left = mc.gameSettings.keyBindLeft.pressed ? mc.gameSettings.keyBindBack.pressed ? 45 : mc.gameSettings.keyBindForward.pressed ? -45  : -90 : 0;
        float right = mc.gameSettings.keyBindRight.pressed ? mc.gameSettings.keyBindBack.pressed ? -45 : mc.gameSettings.keyBindForward.pressed ? 45  : 90 : 0;
        float back = mc.gameSettings.keyBindBack.pressed ? + 180 : 0;
        float yaw = left + right + back;
        return rotationYaw + yaw;
    }

    public boolean isMoving() {
        return Minecraft.getMinecraft().thePlayer.moveForward != 0 || Minecraft.getMinecraft().thePlayer.moveStrafing != 0;
    }
    
    public void setSpeed(double speed) {
        if(isMoving()) {
            mc.thePlayer.motionX = -Math.sin(Math.toRadians(getDirection(mc.thePlayer.rotationYaw))) * speed;
            mc.thePlayer.motionZ = Math.cos(Math.toRadians(getDirection(mc.thePlayer.rotationYaw))) * speed;
        }
    }
}
