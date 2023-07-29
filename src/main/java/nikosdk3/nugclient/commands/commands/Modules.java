package nikosdk3.nugclient.commands.commands;

import nikosdk3.nugclient.Config;
import nikosdk3.nugclient.commands.Command;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.modules.ModuleManager;
import nikosdk3.nugclient.utils.Utils;

import java.util.List;

public class Modules extends Command {

    public Modules() {
        super("modules", "Lists all modules.");
    }

    @Override
    public void run(String[] args) {
        Utils.sendMessage("#yellowAll #gray%d #yellowmodules:", ModuleManager.getAll().size());

        for (Category category : ModuleManager.getCategories()) {
            List<Module> group = ModuleManager.getGroup(category);
            Utils.sendMessage("  #pink%s #gray(%d)#pink:", category.toString(), group.size());

            for (Module module : group) {
                Utils.sendMessage("    #yellow%s%s #gray- #yellow%s", Config.instance.prefix, module.name, module.description);
            }
        }
    }
}
