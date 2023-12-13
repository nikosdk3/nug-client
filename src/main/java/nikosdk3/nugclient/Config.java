package nikosdk3.nugclient;

import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.utils.Vector2;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public static Config instance = new Config();
    public String prefix = ".";
    public Map<Category, Vector2> guiPositions = new HashMap<>();
}
