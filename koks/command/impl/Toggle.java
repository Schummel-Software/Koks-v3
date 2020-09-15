package koks.command.impl;

import koks.Koks;
import koks.command.Command;
import koks.module.Module;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:57
 */
public class Toggle extends Command {

    public Toggle() {
        super("toggle", "t");
    }

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
