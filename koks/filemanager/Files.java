package koks.filemanager;

import koks.Koks;
import koks.api.Methods;
import net.minecraft.client.Minecraft;

import java.io.*;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 04:10
 */
public abstract class Files implements Methods {

    public File file;
    public final File DIR = new File(mc.mcDataDir + "/" +  Koks.getKoks().NAME + "/files");

    public File getFile() {
        return file;
    }

    public Files() {
        IFile iFile = getClass().getAnnotation(IFile.class);
        this.file = new File(DIR, iFile.name() + "." + Koks.getKoks().NAME.toLowerCase());
    }

    public abstract void readFile(BufferedReader bufferedReader) throws IOException;
    public abstract void writeFile(FileWriter fileWriter) throws IOException;
}
