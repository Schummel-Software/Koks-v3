package koks.filemanager;

import koks.Koks;
import koks.api.util.ClassUtil;
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
        ClassUtil classUtil = new ClassUtil();
        String prefix = "koks.filemanager.impl";
        ArrayList<Class> classes = new ArrayList<>();

        try {
            for (Class clazz : classUtil.getClasses(prefix)) {
                if (!clazz.getName().contains("$"))
                    addFile((Files) clazz.newInstance());
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public void writeFile(Class<? extends Files> clazz) {
        if (!DIR.exists()) DIR.mkdirs();

        for (Files file : files) {
            if (file.getClass().equals(clazz)) {
                if (!file.getFile().exists()) {
                    try {
                        file.getFile().createNewFile();
                    } catch (IOException ignored) {
                    }
                }
                try {
                    file.writeFile(new FileWriter(file.getFile()));
                } catch (IOException ignored) {
                }

            }

        }
    }

    public void writeAllFiles() {
        if (!DIR.exists()) DIR.mkdirs();

        for (Files file : files) {
            if (!file.getFile().exists()) {
                try {
                    file.getFile().createNewFile();
                } catch (IOException ignored) {
                }
            }
            try {
                file.writeFile(new FileWriter(file.getFile()));
            } catch (IOException ignored) {
            }
        }
    }

    public void readAllFiles() {
        if (!DIR.exists()) DIR.mkdirs();

        if (DIR.exists()) {
            for (Files file : files) {
                if (!file.getFile().exists()) {
                    try {
                        file.getFile().createNewFile();
                    } catch (IOException ignored) {
                    }
                }
                try {
                    file.readFile(new BufferedReader(new FileReader(file.getFile())));
                } catch (IOException ignored) {
                }
            }
        }

    }

    public void addFile(Files file) {
        files.add(file);
    }
}
