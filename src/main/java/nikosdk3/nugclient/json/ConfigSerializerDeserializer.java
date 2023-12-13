package nikosdk3.nugclient.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import nikosdk3.nugclient.Config;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.modules.ModuleManager;
import nikosdk3.nugclient.utils.Vector2;

import java.lang.reflect.Type;
import java.util.Map;

public class ConfigSerializerDeserializer implements JsonSerializer<Config>, JsonDeserializer<Config> {
    @Override
    public JsonElement serialize(Config src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("prefix", src.prefix);
        obj.add("guiPositions", context.serialize(src.guiPositions));

        JsonArray modules = new JsonArray();
        for (Module module : ModuleManager.getAll())
            modules.add(ModuleJson.saveModule(module));
        obj.add("modules", modules);

        return obj;
    }
    @Override
    public Config deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Config config = new Config();

        config.prefix = obj.get("prefix").getAsString();
        JsonElement guiPositionE = obj.get("guiPositions");
        if (guiPositionE != null)
            config.guiPositions = context.deserialize(guiPositionE, new TypeToken<Map<Category, Vector2>>() {}.getType());

        for (JsonElement element : obj.get("modules").getAsJsonArray())
            ModuleJson.loadModule(element.getAsJsonObject());

        return config;
    }
}
