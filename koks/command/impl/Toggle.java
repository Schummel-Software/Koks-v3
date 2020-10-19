package koks.command.impl;

import koks.Koks;
import koks.command.Command;
import koks.command.CommandInfo;
import koks.module.Module;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:57
 */

@CommandInfo(name = "toggle", alias = "t")
public class Toggle extends Command {

    @Override
    public void execute(String[] args) {
        if(args.length == 1) {
            Module module = Koks.getKoks().moduleManager.getModule(args[0]);
            if(module != null) {
                module.toggle();
                sendmsg((module.isToggled() ? "§a" : "§c") + "Toggled §e" + module.getName(), true);
            }else{
                sendError("Module", args[0] + " not found!");
            }
        }else{
            sendError("Usage", ".toggle [Module]");
        }
    }
}
