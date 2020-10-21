package koks.command;

import koks.api.util.ClassUtil;
import koks.command.impl.*;
import koks.module.Module;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:47
 */
public class CommandManager {

    public ArrayList<Command> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new Bind());
        addCommand(new config());
        addCommand(new Friend());
        addCommand(new RconLogin());
        addCommand(new Reload());
        addCommand(new Toggle());

        commands.sort(Comparator.comparing(Command::getName));
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
}
