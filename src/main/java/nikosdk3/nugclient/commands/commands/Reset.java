package nikosdk3.nugclient.commands.commands;

import nikosdk3.nugclient.commands.Command;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.Setting;
import nikosdk3.nugclient.utils.Utils;

public class Reset extends Command {
    public Reset() {
        super("reset", "Resets settings for a module.");
    }

    @Override
    public void run(String[] args) {
        Module module = Utils.tryToGetModule(args);
        if (module == null) return;

        for (Setting setting : module.settings) setting.reset();
    }
}
