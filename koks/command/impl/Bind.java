package koks.command.impl;

import koks.Koks;
import koks.command.Command;
import koks.module.Module;
import org.lwjgl.input.Keyboard;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 03:52
 */
public class Bind extends Command {

    public Bind() {
        super("bind");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            Module module = Koks.getKoks().moduleManager.getModule(args[0]);
            if (module != null) {
                module.setKey(Keyboard.getKeyIndex(args[1].toUpperCase()));
                Koks.getKoks().keyBindManager.writeKeyBinds();
                sendmsg("Binded §e" + module.getName() + " to Key " + args[1], true);
            } else {
                sendError("Module", args[0] + " doesn't exist!");
            }
        } else {
            sendError("Usage", ".bind [Module] [Key]");
        }
    }
}
