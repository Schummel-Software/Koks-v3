package koks.command;

import koks.api.util.ClassUtil;
import koks.command.impl.Bind;
import koks.command.impl.RconLogin;
import koks.command.impl.Reload;
import koks.command.impl.Toggle;
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
        ClassUtil classUtil = new ClassUtil();
        String prefix = "koks.command.impl";
        ArrayList<Class> classes = new ArrayList<>();

        try {

            for (Class clazz : classUtil.getClasses(prefix)) {
                if (!classes.contains(clazz)) {
                    classes.add(clazz);
                }
            }


            for (Class<? extends Command> cmd : classes) {
                if (!cmd.getName().contains("$"))
                    addCommand(cmd.newInstance());
            }

        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        commands.sort(Comparator.comparing(Command::getName));
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
}
