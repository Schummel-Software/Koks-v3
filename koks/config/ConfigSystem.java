package koks.config;

import koks.Koks;
import koks.module.Module;
import koks.api.settings.Setting;

import java.io.*;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 14:36
 */
public class ConfigSystem {

    public void saveConfig(Config config) {
        try{
            FileWriter fileWriter = new FileWriter(config.getFile());
            for(Module module : Koks.getKoks().moduleManager.getModules()) {
                if(module.getCategory() != Module.Category.GUI || module.getCategory() != Module.Category.RENDER || module.getCategory() != Module.Category.DEBUG){
                    fileWriter.write("Module:" + module.getName() + ":" + module.isToggled() + ":" + module.isBypass() + "\n");
                }
            }
            for(Setting setting : Koks.getKoks().settingsManager.getSettings()) {
                String arguments = "";
                switch(setting.getType()) {
                    case SLIDER:
                        arguments = setting.getCurrentValue() + "";
                        break;
                    case CHECKBOX:
                        arguments = setting.isToggled() + "";
                        break;
                    case COMBOBOX:
                        arguments = setting.getCurrentMode();
                        break;
                }
                fileWriter.write("Setting:" + setting.getModule().getName() + ":" + setting.getName() + ":" + arguments + "\n");
            }
            fileWriter.close();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadConfig(Config config) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(config.getFile()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] args = line.split(":");
                switch(args[0]) {
                    case "Module":
                        Module module = Koks.getKoks().moduleManager.getModule(args[1]);
                        if(module == null)continue;
                        module.setToggled(Boolean.parseBoolean(args[2]));
                        module.setBypass(Boolean.parseBoolean(args[3]));
                        break;
                    case "Setting":
                        module = Koks.getKoks().moduleManager.getModule(args[1]);
                        Setting setting = Koks.getKoks().settingsManager.getSetting(module, args[2]);
                        switch(setting.getType()) {
                            case SLIDER:
                                setting.setCurrentValue(Float.parseFloat(args[3]));
                                break;
                            case CHECKBOX:
                                setting.setToggled(Boolean.parseBoolean(args[3]));
                                break;
                            case COMBOBOX:
                                setting.setCurrentMode(args[3]);
                                break;
                        }
                        break;
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
