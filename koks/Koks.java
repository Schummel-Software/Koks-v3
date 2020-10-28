package koks;

import koks.api.util.DiscordUtil;
import koks.command.CommandManager;
import koks.config.ConfigSystem;
import koks.event.EventManager;
import koks.filemanager.FileManager;
import koks.friends.FriendManager;
import koks.gui.tabgui.TabGUI;
import koks.module.KeyBindManager;
import koks.gui.clickgui.ClickGUI;
import koks.module.ModuleManager;
import koks.api.settings.SettingsManager;

import java.awt.*;
import java.io.File;

import koks.cl.CLManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;
import org.lwjgl.opengl.Display;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 19:20
 */
public class Koks {

    private static final Koks KOKS;

    public final String NAME = "Koks";
    public final String PREFIX = "§c" + NAME + " §7>> ";
    public final String VERSION = "3.0.0";
    public final String[] AUTHORS = new String[]{"DasDirt", "Deleteboys","Kroko", "Phantom"};
    public long initTime = System.currentTimeMillis();

    public String alteningApiKey = "";

    static {
        KOKS = new Koks();
    }

    public Color clientColor = Color.ORANGE;

    public boolean isNew = false;

    public static Koks getKoks() {
        return KOKS;
    }

    public SettingsManager settingsManager;
    public final Minecraft mc = Minecraft.getMinecraft();

    public ModuleManager moduleManager;
    public EventManager eventManager;
    public CommandManager commandManager;
    public ClickGUI clickGUI;
    public KeyBindManager keyBindManager;
    public FileManager fileManager;
    public TabGUI tabGUI;
    public ConfigSystem configSystem;
    public CLManager CLManager;
    public FriendManager friendManager;

    public void startClient() {
        CLManager = new CLManager(Main.clName);
        System.out.println("Client Launcher Name: " + Main.clName);
        settingsManager = new SettingsManager();
        moduleManager = new ModuleManager();
        eventManager = new EventManager();
        commandManager = new CommandManager();
        clickGUI = new ClickGUI();
        keyBindManager = new KeyBindManager();
        keyBindManager.readKeyBinds();
        fileManager = new FileManager();
        tabGUI = new TabGUI();
        fileManager.readAllFiles();
        friendManager = new FriendManager();
        configSystem = new ConfigSystem();
        new DiscordUtil();

        DiscordUtil.getSingleton().setupRPC("769964846506311690");

        StringBuilder author = new StringBuilder();
        for (int i = 0; i < KOKS.AUTHORS.length; i++) {
            author.append(KOKS.AUTHORS[i]).append(", ");
        }

        author = new StringBuilder(author.substring(0, author.length() - 2));

        Display.setTitle(NAME + " v" + VERSION + " | by " + author + " | Minecraft 1.8.8");
    }

    public void stopClient() {
        fileManager.writeAllFiles();
    }
}
