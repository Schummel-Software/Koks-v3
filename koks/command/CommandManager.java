package koks.command;

import koks.command.impl.Bind;
import koks.command.impl.Reload;
import koks.command.impl.Toggle;

import java.util.ArrayList;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:47
 */
public class CommandManager {

    public ArrayList<Command> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new Bind());
        addCommand(new Toggle());
        addCommand(new Reload());
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
}
