package koks.api;

import koks.Koks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
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

    public PlayerControllerMP getPlayerController() {
        return mc.playerController;
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

    public void sendmsg(String msg, boolean prefix) {
        mc.thePlayer.addChatMessage(new ChatComponentText((prefix ? Koks.getKoks().PREFIX : "") + msg));
    }

    public void sendError(String type, String solution) {
        sendmsg("§c§lERROR §e" + type.toUpperCase() + "§7: §f" + solution, true);
    }

    public Koks getKoks(){return Koks.getKoks();}

    public void pushPlayer(double push) {
        double x = -Math.sin(getDirection());
        double z = Math.cos(getDirection());
        mc.thePlayer.motionX = x * push;
        mc.thePlayer.motionZ = z * push;
    }

    public void changePosition(double x, double y, double z) {
        mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
    }

/*    public void stopPlayer() {
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
    }*/

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