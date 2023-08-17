package nikosdk3.nugclient.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.modules.ModuleManager;
import nikosdk3.nugclient.settings.Setting;

public class ModuleJson {
    public static JsonObject saveModule(Module module) {
        JsonObject object = new JsonObject();

        object.addProperty("name", module.name);
        object.addProperty("active", module.isActive());
        if (module.getKey() != -1) object.addProperty("key", module.getKey());

        JsonArray settings = new JsonArray();
        for (Setting setting : module.settings) settings.add(saveSetting(setting));
        object.add("settings", settings);

        return object;
    }

    public static JsonObject saveSetting(Setting setting) {
        JsonObject object = new JsonObject();

        object.addProperty("name", setting.name);
        object.addProperty("value", setting.get().toString());

        return object;
    }

    public static void loadModule(JsonObject object) {
        Module module = ModuleManager.get(object.get("name").getAsString());
        if (module == null) return;

        if (object.has("key")) module.setKey(object.get("key").getAsInt());

        for (JsonElement e : object.get("settings").getAsJsonArray()) {
            JsonObject o = e.getAsJsonObject();
            String name = o.get("name").getAsString();

            for (Setting setting : module.settings) {
                if (setting.name.equalsIgnoreCase(name)) {
                    setting.parse(o.get("value").getAsString());
                    break;
                }
            }
        }
        if (object.get("active").getAsBoolean()) module.toggle();
    }
}
