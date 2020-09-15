package koks.filemanager.impl;

import koks.Koks;
import koks.command.Command;
import koks.filemanager.Files;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 04:21
 */
public class Test extends Files {

    public Test() {
        super("test");
    }

    @Override
    public void readFile(BufferedReader bufferedWriter) throws IOException {
        String line;
        while((line = bufferedWriter.readLine()) != null) {
            System.out.println(line);
        }
    }

    @Override
    public void writeFile(FileWriter fileWriter) throws IOException {
        for(Command command : Koks.getKoks().commandManager.getCommands()) {
            fileWriter.write(command.name + "\n");
        }
        fileWriter.close();
    }
}
