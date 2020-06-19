package eigencraft.motionprint.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import eigencraft.motionprint.util.JsonUtils;

public class LoggingManager {
    public static final LoggingManager INSTANCE = new LoggingManager();

    protected final Map<UUID, PlayerStatusLogger> playerLoggers = new HashMap<>();
    protected int flushInterval = 240;
    protected int loggingInterval = 5;
    protected boolean enabled = true;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getLoggingInterval() {
        return this.loggingInterval;
    }

    public void setLoggingInterval(int interval) {
        this.loggingInterval = interval;
    }

    public int getFlushInterval() {
        return this.flushInterval;
    }

    public void setFlushInterval(int interval) {
        this.flushInterval = interval;
    }

    public void clear() {
        this.flushData();
        this.playerLoggers.clear();
    }

    public void flushData() {
        for (PlayerStatusLogger logger : this.playerLoggers.values()) {
            logger.flushData();
        }
    }

    public void rotateSessions(ServerCommandSource source) {
        MinecraftServer server = source.getMinecraftServer();

        if (server != null) {
            HashSet<UUID> keys = new HashSet<>(this.playerLoggers.keySet());
            long currentTime = System.currentTimeMillis();
            int count = 0;

            for (UUID uuid : keys) {
                PlayerStatusLogger logger = this.playerLoggers.get(uuid);

                if (logger != null) {
                    logger.flushData();
                }

                PlayerEntity player = server.getPlayerManager().getPlayer(uuid);

                if (player != null) {
                    this.playerLoggers.put(uuid, new PlayerStatusLogger(uuid, player.getName().getString(), currentTime));
                    ++count;
                }
                else {
                    this.playerLoggers.remove(uuid);
                }
            }

            source.sendFeedback(new LiteralText("Re-started/rolled " + count + " logging sessions"), false);
        }
        else {
            source.sendError(new LiteralText("Failed to get the server instance"));
        }
    }

    public void onTick(MinecraftServer server, long serverTick) {
        if (this.enabled && (this.loggingInterval <= 1 || (serverTick % (long) this.loggingInterval) == 0L)) {
            for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
                PlayerStatusLogger logger = this.playerLoggers.get(player.getUuid());

                if (logger != null) {
                    logger.logPlayerStatus(player, this.getFlushInterval());
                }
            }
        }
    }

    public void addLogger(PlayerEntity player) {
        UUID uuid = player.getUuid();

        PlayerStatusLogger oldLogger = this.playerLoggers.get(uuid);

        // The player is already logged in...
        // Flush the old data and create a new logging session
        if (oldLogger != null) {
            oldLogger.flushData();
        }

        this.playerLoggers.put(uuid, new PlayerStatusLogger(uuid, player.getName().getString(), System.currentTimeMillis()));
    }

    public void removeLogger(PlayerEntity player) {
        UUID uuid = player.getUuid();
        PlayerStatusLogger logger = this.playerLoggers.get(uuid);

        if (logger != null) {
            logger.flushData();
            this.playerLoggers.remove(uuid);
        }
    }

    public void onPlayerLogin(PlayerEntity player) {
        if (ConsentTracker.INSTANCE.hasPlayerConsented(player)) {
            this.addLogger(player);

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
        this.removeLogger(player);
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();

        obj.add("enabled", new JsonPrimitive(this.isEnabled()));
        obj.add("flush_interval", new JsonPrimitive(this.getFlushInterval()));
        obj.add("logging_interval", new JsonPrimitive(this.getLoggingInterval()));

        return obj;
    }

    public void fromJson(JsonObject obj) {
        this.setEnabled(JsonUtils.getBooleanOrDefault(obj, "enabled", true));
        this.setFlushInterval(JsonUtils.getIntegerOrDefault(obj, "flush_interval", 240));
        this.setLoggingInterval(JsonUtils.getIntegerOrDefault(obj, "logging_interval", 5));
    }
}
