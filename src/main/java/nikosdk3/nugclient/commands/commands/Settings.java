package nikosdk3.nugclient.commands.commands;

import nikosdk3.nugclient.commands.Command;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.Setting;
import nikosdk3.nugclient.utils.Utils;

public class Settings extends Command {
    public Settings() {
        super("settings", "Displays all settings for specified module.");
    }

    @Override
    public void run(String[] args) {
        Module module = Utils.tryToGetModule(args);
        if (module == null) return;

        Utils.sendMessage("#pink%s:", module.title);
        for (Setting setting : module.settings) {
            Utils.sendMessage("  #yellowUsage of #blue'%s' #gray(%s) #yellowis #gray%s#yellow.", setting.name, setting.get().toString(), setting.getUsage());
        }
    }
}
