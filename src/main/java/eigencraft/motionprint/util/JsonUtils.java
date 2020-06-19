package eigencraft.motionprint.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eigencraft.motionprint.MotionPrint;

public class JsonUtils {
    public static boolean hasBoolean(JsonObject obj, String name) {
        JsonElement el = obj.get(name);

        if (el != null && el.isJsonPrimitive()) {
            try {
                el.getAsBoolean();
                return true;
            }
            catch (Exception e) {}
        }

        return false;
    }

    public static boolean hasInteger(JsonObject obj, String name) {
        JsonElement el = obj.get(name);

        if (el != null && el.isJsonPrimitive()) {
            try {
                el.getAsInt();
                return true;
            }
            catch (Exception e) {}
        }

        return false;
    }

    public static boolean hasArray(JsonObject obj, String name) {
        JsonElement el = obj.get(name);

        if (el != null && el.isJsonArray()) {
            return true;
        }

        return false;
    }

    public static boolean getBooleanOrDefault(JsonObject obj, String name, boolean defaultValue) {
        if (obj.has(name) && obj.get(name).isJsonPrimitive()) {
            try {
                return obj.get(name).getAsBoolean();
            }
            catch (Exception e) {}
        }

        return defaultValue;
    }

    public static int getIntegerOrDefault(JsonObject obj, String name, int defaultValue) {
        if (obj.has(name) && obj.get(name).isJsonPrimitive()) {
            try {
                return obj.get(name).getAsInt();
            }
            catch (Exception e) {}
        }

        return defaultValue;
    }

    @Nullable
    public static JsonObject getNestedObject(JsonObject parent, String key, boolean create) {
        if (parent.has(key) == false || parent.get(key).isJsonObject() == false) {
            if (create == false) {
                return null;
            }

            JsonObject obj = new JsonObject();
            parent.add(key, obj);
            return obj;
        }
        else {
            return parent.get(key).getAsJsonObject();
        }
    }

    @Nullable
    public static JsonElement parseJsonFile(File file) {
        if (file != null && file.exists() && file.isFile() && file.canRead()) {
            String fileName = file.getAbsolutePath();

            try (FileReader reader = new FileReader(file)) {
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(reader);
                return element;
            }
            catch (Exception e) {
                MotionPrint.logger.error("Failed to parse the JSON file '{}'", fileName, e);
            }
        }

        return null;
    }

    public static boolean writeJsonToFile(Gson gson, JsonElement root, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(gson.toJson(root));
            return true;
        }
        catch (IOException e) {
            MotionPrint.logger.warn("Failed to write JSON data to file '{}'", file.getAbsolutePath(), e);
        }

        return false;
    }
}
