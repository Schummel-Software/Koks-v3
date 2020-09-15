package koks.config;

import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 14:39
 */
public class Config {

    public Minecraft mc = Minecraft.getMinecraft();

    public File file;

    public Config(String name, File DIR) {
        file = new File(DIR,name);
    }

    public boolean fileExist() {
        return file.exists();
    }

    public void createFile() {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }
}
