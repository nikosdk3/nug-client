package nikosdk3.nugclient.commands.commands;

import nikosdk3.nugclient.commands.Command;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.modules.ModuleManager;

import java.util.ArrayList;
import java.util.List;

public class Panic extends Command {
    public Panic() {
        super("panic", "Disables all active modules.");
    }

    @Override
    public void run(String[] args) {
        List<Module> active = new ArrayList<>(ModuleManager.getActive());

        for (Module module : active) module.toggle();
    }
}
