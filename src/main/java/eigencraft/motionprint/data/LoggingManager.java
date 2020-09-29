package eigencraft.motionprint.data;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import eigencraft.motionprint.MotionPrint;
import eigencraft.motionprint.api.MotionPrintPlugin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import eigencraft.motionprint.config.Configs;
import eigencraft.motionprint.util.JsonUtils;

public class LoggingManager {
    public static final LoggingManager INSTANCE = new LoggingManager();

    protected final HashMap<PlayerEntity, PlayerDataLogger> playerLoggers = new HashMap<>();
    protected final List<MotionPrintPlugin> plugins = new ArrayList<>();
    protected int flushInterval = 240;
    protected int loggingInterval = 5;
    protected boolean enabled = true;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        Configs.writeConfigsToFile();
    }

    public int getFlushInterval() {
        return this.flushInterval;
    }

    public void setFlushInterval(int interval) {
        this.flushInterval = interval;
        Configs.writeConfigsToFile();
    }

    public void clear() {
        this.flushData();
        this.playerLoggers.clear();
    }

    public void flushData() {
        for (PlayerDataLogger logger : this.playerLoggers.values()) {
            logger.flushData();
        }
    }

    public void rotateSessions(ServerCommandSource source) {
        MinecraftServer server = source.getMinecraftServer();

        if (server != null) {
            int count = 0;

            for (PlayerDataLogger logger : playerLoggers.values()) {

                if (logger != null) {
                    logger.rotateSession();
                    count++;
                }
            }

            source.sendFeedback(new LiteralText("Re-started/rolled " + count + " logging sessions"), false);
        }
        else {
            source.sendError(new LiteralText("Failed to get the server instance"));
        }
    }

    public void onTick(MinecraftServer server, long serverTick) {
        if (this.enabled&&((serverTick % (long) this.loggingInterval) == 0L)) {
            for (MotionPrintPlugin plugin:plugins){
                if (plugin.isEnabled()) {
                    plugin.tick(playerLoggers.values());
                }
            }
        }
    }

    public void startLoggingPlayer(PlayerEntity player) {
        UUID uuid = player.getUuid();

        PlayerDataLogger oldLogger = this.playerLoggers.getOrDefault(player,null);

        // The player is already logged in...
        // Flush the old data and create a new logging session
        if (oldLogger != null) {
            oldLogger.flushData();
        }

        PlayerDataLogger newLogger = new PlayerDataLogger(uuid, player.getName().getString(), System.currentTimeMillis(),player);

        this.playerLoggers.put(player, newLogger);

        for (MotionPrintPlugin plugin:plugins){
            plugin.startLogPlayer(newLogger);
        }
    }

    public void stopLoggingPlayer(PlayerEntity player) {
        UUID uuid = player.getUuid();
        PlayerDataLogger logger = this.playerLoggers.getOrDefault(player,null);

        for (MotionPrintPlugin plugin:plugins){
            plugin.stopLogPlayer(logger);
        }

        if (logger != null) {
            logger.flushData();
            this.playerLoggers.remove(player);
        }
    }

    public void onPlayerLogin(PlayerEntity player) {
        if (ConsentTracker.INSTANCE.hasPlayerConsented(player)) {
            this.startLoggingPlayer(player);

            player.sendMessage(new LiteralText("You have previously consented to your player status data being logged."));
            player.sendMessage(new LiteralText("If you wish to revoke your consent and stop your data being logged any further, you can use the command"));
            player.sendMessage(new LiteralText("/motion-print-revoke-consent").formatted(Formatting.AQUA));
        }
        else if (! ConsentTracker.INSTANCE.isPlayerChoiceKnown(player)) {
            player.sendMessage(new LiteralText("This server uses the MotionPrint mod to track some player data for scientific study purposes."));
            player.sendMessage(new LiteralText("The collected data includes the player's position, velocity, rotation, ground vs. air status, sneaking status, and possibly other pieces of data."));
            player.sendMessage(new LiteralText("If you wish to consent to this data being collected of your player, then run the command"));
            player.sendMessage(new LiteralText("/motion-print-grant-consent").formatted(Formatting.AQUA));
            player.sendMessage(new LiteralText("If you later wish to revoke your consent and stop your data being logged any further, you can use the command"));
            player.sendMessage(new LiteralText("/motion-print-revoke-consent").formatted(Formatting.AQUA));
        }
        else {
            player.sendMessage(new LiteralText("You have previously opted out of your player status data being logged."));
            player.sendMessage(new LiteralText("The collected data includes the player's position, velocity, rotation, ground vs. air status, sneaking status, death and respawn events and possibly other pieces of data."));
            player.sendMessage(new LiteralText("If you wish to consent to this data being collected of your player, then run the command"));
            player.sendMessage(new LiteralText("/motion-print-grant-consent").formatted(Formatting.AQUA));
        }
    }

    public void onPlayerLogout(PlayerEntity player) {
        this.stopLoggingPlayer(player);
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();

        obj.add("enabled", new JsonPrimitive(this.isEnabled()));
        obj.add("flush_interval", new JsonPrimitive(this.getFlushInterval()));
        obj.add("logging_interval",new JsonPrimitive(this.getLoggingInterval()));

        JsonArray disabledPlugins = new JsonArray();
        for (MotionPrintPlugin plugin:plugins){
            if (!plugin.isEnabled()){
                disabledPlugins.add(new JsonPrimitive(plugin.getName()));
            }
        }

        obj.add("disabled_plugins",disabledPlugins);

        return obj;
    }

    public int getLoggingInterval() {
        return this.loggingInterval;
    }

    public void fromJson(JsonObject obj) {
        this.enabled = JsonUtils.getBooleanOrDefault(obj, "enabled", true);
        this.flushInterval = JsonUtils.getIntegerOrDefault(obj, "flush_interval", 240);
        this.loggingInterval = JsonUtils.getIntegerOrDefault(obj, "logging_interval", 5);

        //Disable plugins
        for(JsonElement element:JsonUtils.getJsonElementIterableSafe(obj,"disabled_plugins")){
            if (element.isJsonPrimitive()){
                if (element.getAsJsonPrimitive().isString()){
                    String disabledName =  element.getAsJsonPrimitive().getAsString();
                    for(MotionPrintPlugin plugin:plugins){
                        if (plugin.getName().equals(disabledName)){
                            plugin.disable();
                        }
                    }
                }
            }
        }

    }

    public void addPlugin(MotionPrintPlugin plugin) {
        plugins.add(plugin);
        plugin.enable();
    }

    public void setLoggingInterval(int interval) {
        loggingInterval = interval;
    }

    public Collection<MotionPrintPlugin> getPlugins() {
        return plugins;
    }

    public PlayerDataLogger getPlayerDataLogger(PlayerEntity entity) {
        return playerLoggers.getOrDefault(entity,null);
    }
}
