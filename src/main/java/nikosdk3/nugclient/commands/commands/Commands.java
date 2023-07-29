package nikosdk3.nugclient.commands.commands;

import nikosdk3.nugclient.Config;
import nikosdk3.nugclient.commands.Command;
import nikosdk3.nugclient.commands.CommandManager;
import nikosdk3.nugclient.utils.Utils;

public class Commands extends Command {

    public Commands() {
        super("commands", "Lists all commands.");
    }

    @Override
    public void run(String[] args) {
        Utils.sendMessage("#yellowAll #gray%d #yellowcommands:", CommandManager.getCount());
        CommandManager.forEach(command -> {
            Utils.sendMessage(" #yellow%s%s #gray- #yellow%s", Config.instance.prefix, command.name, command.description);
        });
    }
}
