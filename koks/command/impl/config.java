package koks.command.impl;

import koks.Koks;
import koks.command.Command;
import koks.command.CommandInfo;
import koks.config.ConfigSystem;

/**
 * @author kroko
 * @created on 15.10.2020 : 00:24
 */

@CommandInfo(name = "config", alias = "cfg")
public class config extends Command {

    @Override
    public void execute(String[] args) {
        if (args.length <= 1) {
            sendError("Usage", ".config load [Name]");
            sendError("Usage", ".config save [Name]");
        } else {
            ConfigSystem config = Koks.getKoks().configSystem;

            if (args[0].equalsIgnoreCase("save")) {
                String cfgname = "";
                for (int i = 1; i < args.length; i++)
                    cfgname += args[i] + " ";
                config.saveConfig(cfgname.substring(0, cfgname.length() - 1));
                sendmsg("§aSaved §e" + cfgname.substring(0, cfgname.length() - 1), true);
            } else if (args[0].equalsIgnoreCase("load")) {
                String cfgname = "";
                for (int i = 1; i < args.length; i++)
                    cfgname += args[i] + " ";
                config.loadConfig(cfgname.substring(0, cfgname.length() - 1));
                sendmsg("§aLoaded §e" + cfgname.substring(0, cfgname.length() - 1), true);
            }
        }
    }
}
