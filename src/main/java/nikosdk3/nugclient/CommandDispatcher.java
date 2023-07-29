package nikosdk3.nugclient;

import nikosdk3.nugclient.commands.Command;
import nikosdk3.nugclient.commands.CommandManager;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.modules.ModuleManager;
import nikosdk3.nugclient.settings.Setting;
import nikosdk3.nugclient.utils.Utils;

import java.util.Arrays;

public class CommandDispatcher {
    public static void run(String msg) {
        String[] args = msg.split(" ");

        //Get command, if found then run it and if not get module
        Command command = CommandManager.get(args[0]);
        if (command != null) {
            args = subArray(args, 1);
            command.run(args);
            return;
        }

        //Get module
        Module module = ModuleManager.get(args[0]);
        if (module == null) return;
        args = subArray(args, 1);

        if (args.length <= 0) {
            //Toggle module if nothing after its name
            module.toggle();
        } else {
            //Set or get module setting
            Setting setting = module.getSetting(args[0]);
            if (setting == null) {
                Utils.sendMessage("#redModule #blue'%s' #reddoes not have setting with name #blue'%s'#red.", module.title, args[0]);
                return;
            }
            args = subArray(args, 1);

            if (args.length <= 0) {
                //No args after setting, send its value
                Utils.sendMessage("#yellowValue of #blue'%s' #yellowis #blue'%s'#yellow.", setting.name, setting.get().toString());
            } else {
                //Parse setting's value and report usage if error
                if (setting.parse(String.join(" ", args))) {
                    Utils.sendMessage("#yellowValue of #blue'%s' #yellowset to #blue%s#yellow.", setting.name, setting.get());
                } else {
                    Utils.sendMessage("#redUsage of #blue'%s' #redis #gray%s#red.", setting.name, setting.getUsage());
                }
            }
        }
    }

    private static String[] subArray(String[] arr, int startIndex) {
        return Arrays.copyOfRange(arr, startIndex, arr.length);
    }
}
