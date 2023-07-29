package nikosdk3.nugclient.commands.commands;

import nikosdk3.nugclient.commands.Command;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.utils.Utils;

public class ResetBind extends Command {

    public ResetBind() {
        super("reset-bind", "Reset module bind.");
    }

    @Override
    public void run(String[] args) {
        Module module = Utils.tryToGetModule(args);
        if (module == null) return;

        Utils.sendMessage("#yellowBind has been reset.");
        module.setKey(-1);
    }
}
