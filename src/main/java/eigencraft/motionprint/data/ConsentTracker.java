package eigencraft.motionprint.data;

import java.io.File;
import java.util.HashSet;
import java.util.UUID;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import eigencraft.motionprint.Reference;
import eigencraft.motionprint.config.Configs;
import eigencraft.motionprint.util.JsonUtils;

public class ConsentTracker {
    public static final ConsentTracker INSTANCE = new ConsentTracker();

    protected final HashSet<UUID> consentedPlayers = new HashSet<>();
    protected final HashSet<UUID> optedOutPlayers = new HashSet<>();

    public void clear() {
        this.consentedPlayers.clear();
        this.optedOutPlayers.clear();
    }

    /**
     * Returns true if the player has either consented or opted out, ie. if the player
     * has made a deliberate choice.
     * @param player
     * @return
     */
    public boolean isPlayerChoiceKnown(PlayerEntity player) {
        UUID uuid = player.getUuid();
        return this.consentedPlayers.contains(uuid) || this.optedOutPlayers.contains(uuid);
    }

    public boolean hasPlayerConsented(PlayerEntity player) {
        UUID uuid = player.getUuid();
        return this.consentedPlayers.contains(uuid);
    }

    public void grantConsent(PlayerEntity player) {
        UUID uuid = player.getUuid();

        if (! this.consentedPlayers.contains(uuid)) {
            this.consentedPlayers.add(uuid);
            this.optedOutPlayers.remove(uuid);

            player.sendMessage(new LiteralText("You have now successfully consented to your player data being logged, thank you!").formatted(Formatting.GREEN));
            player.sendMessage(new LiteralText("The collected data includes the player's position, velocity, rotation, ground vs. air status, sneaking status, and possibly other pieces of data."));
            player.sendMessage(new LiteralText("If you wish to revoke your consent and stop any further of your data being logged, you can use the command"));
            player.sendMessage(new LiteralText("/motion-print-revoke-consent").formatted(Formatting.AQUA));

            LoggingManager.INSTANCE.startLoggingPlayer(player);
            this.writeToFile();
        }
        else {
            player.sendMessage(new LiteralText("You have already given your consent").formatted(Formatting.RED));
        }
    }

    public void revokeConsent(PlayerEntity player) {
        UUID uuid = player.getUuid();

        if (this.consentedPlayers.contains(uuid)) {
            this.consentedPlayers.remove(uuid);
            this.optedOutPlayers.add(uuid);

            LoggingManager.INSTANCE.stopLoggingPlayer(player);
            this.writeToFile();

            player.sendMessage(new LiteralText("You have successfully revoked your consent for your player data being logged.").formatted(Formatting.GREEN));
            player.sendMessage(new LiteralText("If you wish grant consent again, run the command"));
            player.sendMessage(new LiteralText("/motion-print-grant-consent").formatted(Formatting.AQUA));
        }
        else {
            player.sendMessage(new LiteralText("You hadn't given your consent yet in the first place").formatted(Formatting.RED));
        }
    }

    private void writeData(JsonObject obj, String key, HashSet<UUID> set) {
        JsonArray arr = new JsonArray();

        for (UUID uuid : set) {
            arr.add(new JsonPrimitive(uuid.toString()));
        }

        obj.add(key, arr);
    }

    private void readData(JsonObject obj, String key, HashSet<UUID> set) {
        set.clear();

        if (JsonUtils.hasArray(obj, key)) {
            JsonArray arr = obj.getAsJsonArray(key);
            final int size = arr.size();

            for (int i = 0; i < size; ++i) {
                set.add(UUID.fromString(arr.get(i).getAsString()));
            }
        }
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();

        this.writeData(obj, "consented_players", this.consentedPlayers);
        this.writeData(obj, "opted_out_players", this.optedOutPlayers);

        return obj;
    }

    public void fromJson(JsonObject obj) {
        this.readData(obj, "consented_players", this.consentedPlayers);
        this.readData(obj, "opted_out_players", this.optedOutPlayers);
    }

    private static String getConfigFileName() {
        return Reference.MOD_ID + "_consents.json";
    }

    private static File getConfigFile() {
        return new File(Configs.getConfigDirectory(), getConfigFileName());
    }

    public void readFromFile() {
        JsonElement element = JsonUtils.parseJsonFile(getConfigFile());

        if (element != null && element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            this.fromJson(obj);
        }
    }

    public void writeToFile() {
        JsonUtils.writeJsonToFile(Configs.GSON, this.toJson(), getConfigFile());
    }
}
