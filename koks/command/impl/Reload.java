package koks.command.impl;

import koks.Koks;
import koks.command.Command;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 12:25
 */
public class Reload extends Command {

    public Reload() {
        super("Reload", "rl");
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 0)
            return;

        Koks.getKoks().stopClient();
        Koks.getKoks().startClient();
    }

}