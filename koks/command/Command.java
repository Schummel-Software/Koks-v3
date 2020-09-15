package koks.command;

import koks.Koks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:47
 */
public abstract class Command {

    public String name,alias;
    public Minecraft mc = Minecraft.getMinecraft();

    public Command(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public Command(String name) {
        this.name = name;
    }

    public abstract void execute(String[] args);

    public void sendmsg(String msg, boolean prefix) {
        mc.thePlayer.addChatMessage(new ChatComponentText((prefix ? Koks.getKoks().PREFIX : "") + msg));
    }

    public void sendError(String type, String solution) {
        sendmsg("§c§lERROR §e" + type.toUpperCase() + "§7: §f" + solution, true);
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
