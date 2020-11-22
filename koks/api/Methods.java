package koks.api;

import koks.Koks;
import koks.api.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.*;
import net.minecraft.world.World;

/**
 * @author Deleteboys | lmao | kroko
 * @created on 13.09.2020 : 11:23
 */
public class Methods {

    public final Console console = new Console();

    public final Minecraft mc = Minecraft.getMinecraft();

    public EntityPlayerSP getPlayer() {
        return mc.thePlayer;
    }

    public void sendPacket(Packet<?> packet) {
        getPlayer().sendQueue.addToSendQueue(packet);
    }

    public void sendPacketUnlogged(Packet<?> packet) {
        getPlayer().sendQueue.addToSendQueueUnlogged(packet);
    }

    public PlayerControllerMP getPlayerController() {
        return mc.playerController;
    }

    public World getWorld() {
        return mc.theWorld;
    }

    public GameSettings getGameSettings() {
        return mc.gameSettings;
    }

    public BlockPos getPosition() {
        return getPlayer().getPosition();
    }

    public double getX() {
        return getPlayer().posX;
    }

    public double getY() {
        return getPlayer().posY;
    }

    public double getZ() {
        return getPlayer().posZ;
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

    public void setMotion(double motion) {
        getPlayer().motionX = getPlayer().motionZ = 0;
    }

    public void sendURL(String msg, String url, boolean underline, boolean prefix) {
        IChatComponent chatComponent = new ChatComponentText((prefix ? Koks.getKoks().PREFIX : "") + msg);
        chatComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        chatComponent.getChatStyle().setUnderlined(underline);
        mc.thePlayer.addChatMessage(chatComponent);
    }

    public void sendError(String type, String solution) {
        sendmsg("§c§lERROR §e" + type.toUpperCase() + "§7: §f" + solution, true);
    }

    public Koks getKoks() {
        return Koks.getKoks();
    }

    public void pushPlayer(double push) {
        double x = -Math.sin(getDirection());
        double z = Math.cos(getDirection());
        mc.thePlayer.motionX = x * push;
        mc.thePlayer.motionZ = z * push;
    }

    public void debugEntity(Entity finalEntity) {
        System.out.println("----DEBUG----");
        System.out.println("Name: " + finalEntity.getName());
        System.out.println("ExistTime: " + finalEntity.ticksExisted);
        System.out.println("Eye Height: " + finalEntity.getEyeHeight());
        System.out.println("DistanceToPlayer: " + finalEntity.getDistanceToEntity(getPlayer()));
        System.out.println("CanBePushed: " + finalEntity.canBePushed());
        System.out.println("canAttackWithItem: " + finalEntity.canAttackWithItem());
        System.out.println("EntityID: " + finalEntity.getEntityId());
        System.out.println("LookVec: " + finalEntity.getLookVec());
        System.out.println("UUID: " + finalEntity.getUniqueID());
        if (finalEntity.getInventory() != null)
            System.out.println("Inventory Length: " + finalEntity.getInventory().length);
        System.out.println("Position: " + finalEntity.getPosition());
        System.out.println("onGround: " + finalEntity.onGround);
        System.out.println("hurtResistantTime: " + finalEntity.hurtResistantTime);
        System.out.println("MotionY: " + finalEntity.motionY);
        System.out.println("isDead: " + finalEntity.isDead);
        if (finalEntity instanceof EntityPlayer) {
            System.out.println("Health: " + ((EntityPlayer) finalEntity).getHealth());
            System.out.println("MaxHealth: " + ((EntityPlayer) finalEntity).getMaxHealth());
            System.out.println("Team: " + ((EntityPlayer) finalEntity).getTeam());
            System.out.println("AIMoveSpeed: " + ((EntityPlayer) finalEntity).getAIMoveSpeed());
            System.out.println("BedLocation: " + ((EntityPlayer) finalEntity).getBedLocation());
        }
        System.out.println("MaxFallHeight: " + finalEntity.getMaxFallHeight());
    }

    public void setPosition(double x, double y, double z, boolean ground) {
        getPlayer().setPosition(x, y, z);
        getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, ground));
    }

    public void changePosition(double x, double y, double z) {
        mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
        getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z, getPlayer().onGround));
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