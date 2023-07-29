package nikosdk3.nugclient.json;

import com.google.gson.*;
import nikosdk3.nugclient.Config;

import java.lang.reflect.Type;

public class ConfigSerializerDeserializer implements JsonSerializer<Config>, JsonDeserializer<Config> {
    @Override
    public Config deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Config config = new Config();

        config.prefix = obj.get("prefix").getAsString();

        return config;
    }

    @Override
    public JsonElement serialize(Config src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("prefix", src.prefix);

        JsonArray modules = new JsonArray();
        obj.add("modules", modules);

        return obj;
    }

}
