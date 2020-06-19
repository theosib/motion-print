package eigencraft.motionprint.config;

import java.io.File;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import eigencraft.motionprint.MotionPrint;
import eigencraft.motionprint.Reference;
import eigencraft.motionprint.data.ConsentTracker;
import eigencraft.motionprint.data.LoggingManager;
import eigencraft.motionprint.util.JsonUtils;

public class Configs
{
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File gameDirectory = new File(".");

    public static File getGameDirectory() {
        return gameDirectory;
    }

    public static File getConfigDirectory() {
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

    private static File getConfigFile() {
        return new File(getConfigDirectory(), getConfigFileName());
    }

    public static void readConfigsFromFile() {
        JsonElement element = JsonUtils.parseJsonFile(getConfigFile());

        if (element != null && element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            JsonObject nested = JsonUtils.getNestedObject(obj, "generic", false);

            if (nested != null) {
                LoggingManager.INSTANCE.fromJson(obj);
            }
        }
    }

    public static void writeConfigsToFile() {
        JsonObject obj = new JsonObject();

        obj.add("generic", LoggingManager.INSTANCE.toJson());

        JsonUtils.writeJsonToFile(GSON, obj, getConfigFile());
    }
}
