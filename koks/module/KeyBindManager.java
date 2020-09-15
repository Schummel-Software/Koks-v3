package koks.module;

import koks.Koks;
import net.minecraft.client.Minecraft;

import java.io.*;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 03:43
 */
public class KeyBindManager {

    public Minecraft mc = Minecraft.getMinecraft();

    public File DIR = new File(mc.mcDataDir + "/" + Koks.getKoks().NAME);

    public void readKeyBinds() {
        if(!DIR.exists())DIR.mkdirs();
        File file = new File(DIR, "keybinds." + Koks.getKoks().NAME.toLowerCase());
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                String[] args = line.split(":");
                Module module = Koks.getKoks().moduleManager.getModule(args[0]);
                if(module != null) module.setKey(Integer.parseInt(args[1]));
            }
            bufferedReader.close();
        } catch (IOException ignored) {
        }
    }

    public void writeKeyBinds() {
        if(!DIR.exists())DIR.mkdirs();
        File file = new File(DIR, "keybinds." + Koks.getKoks().NAME.toLowerCase());
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            for(Module module : Koks.getKoks().moduleManager.getModules()) {
                bufferedWriter.write(module.getName() + ":" + module.getKey() + "\n");
            }
            bufferedWriter.close();
        } catch (IOException ignored) {
        }

    }
}
