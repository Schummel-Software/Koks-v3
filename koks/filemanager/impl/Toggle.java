package koks.filemanager.impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import koks.Koks;
import koks.filemanager.Files;
import koks.filemanager.IFile;
import koks.module.Module;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author kroko
 * @created on 20.10.2020 : 15:20
 */
@IFile(name = "toggle")
public class Toggle extends Files {

    @Override
    public void readFile(BufferedReader bufferedReader) throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] split = line.split(":");
            Module module = Koks.getKoks().moduleManager.getModule(split[0]);
            if(module != null) {
                module.setToggled(Boolean.parseBoolean(split[1]));
                module.setBypass(Boolean.parseBoolean(split[2]));
            }
        }
    }

    @Override
    public void writeFile(FileWriter fileWriter) throws IOException {
        for(Module module : Koks.getKoks().moduleManager.getModules()) {
            fileWriter.write(module.getName() + ":" + module.isToggled() + ":" + module.isBypass() + "\n");
        }
    }
}
