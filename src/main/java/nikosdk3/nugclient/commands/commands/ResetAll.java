package nikosdk3.nugclient.commands.commands;

import nikosdk3.nugclient.commands.Command;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.modules.ModuleManager;
import nikosdk3.nugclient.settings.Setting;

public class ResetAll extends Command {
    public ResetAll() {
        super("reset-all", "Resets every module's settings.");
    }

    @Override
    public void run(String[] args) {
        for (Module module : ModuleManager.getAll())
            for (Setting setting : module.settings) setting.reset();
    }
}
