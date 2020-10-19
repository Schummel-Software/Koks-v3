package koks.filemanager.impl;

import koks.Koks;
import koks.filemanager.Files;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author kroko
 * @created on 06.10.2020 : 21:20
 */
public class AlteningToken extends Files {

    public AlteningToken() {
        super("api-keys");
    }

    @Override
    public void readFile(BufferedReader bufferedWriter) throws IOException {
        Koks.getKoks().alteningApiKey = "";
        String line;
        while((line = bufferedWriter.readLine()) != null) {
            String[] args = line.split(":");
            if(args[0].equalsIgnoreCase("apiToken")) {
                if(args.length == 2) {
                    Koks.getKoks().alteningApiKey = args[1];
                }
            }
        }
        bufferedWriter.close();
    }

    @Override
    public void writeFile(FileWriter fileWriter) throws IOException {
        fileWriter.write("apiToken:" + Koks.getKoks().alteningApiKey);
        fileWriter.close();
    }
}
