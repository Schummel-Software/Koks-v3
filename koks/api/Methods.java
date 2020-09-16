package koks.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;
import net.minecraft.world.World;

/**
 * @author Deleteboys | lmao | kroko
 * @created on 13.09.2020 : 11:23
 */
public class Methods {

    public final Minecraft mc = Minecraft.getMinecraft();

    public EntityPlayerSP getPlayer() {
        return mc.thePlayer;
    }

    public World getWorld() {
        return mc.theWorld;
    }

    public Timer getTimer() {
        return mc.timer;
    }

    public int getHurtTime() {
        return mc.thePlayer.hurtTime;
    }

    public boolean isMoving() {
        return getPlayer().moveForward != 0 || getPlayer().moveStrafing != 0;
    }

    public double getDirection() {
        return Math.toRadians(getPlayer().rotationYaw);
    }

    public void pushPlayer(double push) {
        double x = -Math.sin(getDirection());
        double z = Math.cos(getDirection());
        mc.thePlayer.motionX = x * push;
        mc.thePlayer.motionZ = z * push;
    }

    public void changePosition(double x, double y, double z) {
        mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
    }

    public boolean validEntityName(Entity entity) {
        if (!(entity instanceof EntityPlayer))
            return true;
        String name = entity.getName();
        if (name.length() < 3 || name.length() > 16)
            return false;
        if (name.contains("^"))
            return false;
        if (name.contains("°"))
            return false;
        if (name.contains("!"))
            return false;
        if (name.contains("§"))
            return false;
        if (name.contains("$"))
            return false;
        if (name.contains("%"))
            return false;
        if (name.contains("&"))
            return false;
        if (name.contains("/"))
            return false;
        if (name.contains("("))
            return false;
        if (name.contains(")"))
            return false;
        if (name.contains("="))
            return false;
        if (name.contains("{"))
            return false;
        if (name.contains("}"))
            return false;
        if (name.contains("["))
            return false;
        if (name.contains("]"))
            return false;
        if (name.contains("ß"))
            return false;
        if (name.contains("?"))
            return false;
        if (name.contains("-"))
            return false;
        if (name.contains("."))
            return false;
        if (name.contains(":"))
            return false;
        if (name.contains(","))
            return false;
        if (name.contains("+"))
            return false;
        if (name.contains("*"))
            return false;
        if (name.contains("~"))
            return false;
        if (name.contains("#"))
            return false;
        if (name.contains(";"))
            return false;
        if (name.contains("'"))
            return false;
        return true;
    }

}