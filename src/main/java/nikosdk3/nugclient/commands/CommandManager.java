package nikosdk3.nugclient.commands;

import nikosdk3.nugclient.commands.commands.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CommandManager {
    private static List<Command> commands = new ArrayList<>();

    public static void init() {
        addCommand(new Bind());
        addCommand(new ResetBind());
        addCommand(new ClearChat());
        addCommand(new Commands());
        addCommand(new Modules());
        addCommand(new Settings());
        addCommand(new Reset());
        addCommand(new Panic());
        addCommand(new ResetAll());

        System.out.println("Nug Client loaded " + commands.size() + " commands.");
    }

    public static Command get(String name) {
        for (Command command : commands) {
            if (command.name.equalsIgnoreCase(name))
                return command;
        }
        return null;
    }

    public static void forEach(Consumer<Command> consumer) {
        commands.forEach(consumer);
    }

    public static int getCount() {
        return commands.size();
    }

    private static void addCommand(Command command) {
        commands.add(command);
    }
}
