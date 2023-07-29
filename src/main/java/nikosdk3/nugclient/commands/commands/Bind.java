package nikosdk3.nugclient.commands.commands;

import nikosdk3.nugclient.commands.Command;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.modules.ModuleManager;
import nikosdk3.nugclient.utils.Utils;

public class Bind extends Command {
    public Bind() {
        super("bind", "Binds a module to a key.");
    }

    @Override
    public void run(String[] args) {
        Module module = Utils.tryToGetModule(args);
        if (module == null) return;

        Utils.sendMessage("#yellowPress some key.");
        ModuleManager.moduleToBind = module;
    }
}
