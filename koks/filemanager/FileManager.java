package koks.filemanager;

import koks.Koks;
import koks.filemanager.impl.Test;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.ArrayList;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 04:10
 */
public class FileManager {

    public final Minecraft mc = Minecraft.getMinecraft();
    public final File DIR = new File(mc.mcDataDir + "/" + Koks.getKoks().NAME + "/files");
    public ArrayList<Files> files = new ArrayList<>();

    public FileManager() {
        addFile(new Test());
    }

    public void writeAllFiles() {
        if(!DIR.exists())DIR.mkdirs();

        for(Files files : files) {
            if(!files.getFile().exists()) {
                try {
                    files.getFile().createNewFile();
                } catch (IOException ignored) {
                }
            }
            try {
                files.writeFile(new FileWriter(files.getFile()));
            } catch (IOException ignored) {
            }
        }
    }

    public void readAllFiles() {
        if(DIR.exists()) {
            for (Files files : files) {
                if(!files.getFile().exists()) {
                    try {
                        files.getFile().createNewFile();
                    } catch (IOException ignored) {
                    }
                }
                try {
                    files.readFile(new BufferedReader(new FileReader(files.getFile())));
                } catch (IOException ignored) {
                }
            }
        }
    }

    public void addFile(Files file) {
        files.add(file);
    }
}
