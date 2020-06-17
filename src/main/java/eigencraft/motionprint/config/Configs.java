package eigencraft.motionprint.config;

import java.io.File;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import eigencraft.motionprint.MotionPrint;
import eigencraft.motionprint.Reference;
import eigencraft.motionprint.data.LoggingManager;
import eigencraft.motionprint.util.JsonUtils;

public class Configs
{
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File gameDirectory = new File(".");

    public static File getGameDirectory() {
        return gameDirectory;
    }

    private static File getConfigDirectory() {
        File dir = new File(gameDirectory, "config");

        try {
            if (! dir.exists() && ! dir.mkdirs()) {
                MotionPrint.logger.warn("Failed to create the config directory '{}'", dir);
            }
        }
        catch (Exception e) {
            MotionPrint.logger.warn("Failed to create the config directory '{}'", dir, e);
        }

        return dir;
    }

    private static String getConfigFileName() {
        return Reference.MOD_ID + ".json";
    }

    private static void readConfigFromJson(JsonObject obj) {
        LoggingManager.INSTANCE.setEnabled(JsonUtils.getBooleanOrDefault(obj, "enabled", true));
        LoggingManager.INSTANCE.setLoggingInterval(JsonUtils.getIntegerOrDefault(obj, "interval", 1));
    }

    private static JsonObject writeConfigToJson() {
        JsonObject obj = new JsonObject();
        obj.add("enabled", new JsonPrimitive(LoggingManager.INSTANCE.isEnabled()));
        obj.add("interval", new JsonPrimitive(LoggingManager.INSTANCE.getLoggingInterval()));
        return obj;
    }

    public static void readConfigsFromFile() {
        File configFile = new File(getConfigDirectory(), getConfigFileName());

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject()) {
                readConfigFromJson(element.getAsJsonObject());
            }
        }
    }

    public static void writeConfigsToFile() {
        File configFile = new File(getConfigDirectory(), getConfigFileName());
        JsonUtils.writeJsonToFile(GSON, writeConfigToJson(), configFile);
    }
}
