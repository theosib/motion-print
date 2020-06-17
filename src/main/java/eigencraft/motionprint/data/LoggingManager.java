package eigencraft.motionprint.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

public class LoggingManager {
    public static final LoggingManager INSTANCE = new LoggingManager();

    protected final Map<UUID, PlayerStatusLogger> playerLoggers = new HashMap<>();
    protected int loggingInterval = 1;
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

    public void flushData() {
        for (PlayerStatusLogger logger : this.playerLoggers.values()) {
            logger.flushData();
        }
    }

    public void onTick(MinecraftServer server, long serverTick) {
        if (this.enabled && (this.loggingInterval <= 1 || (serverTick % (long) this.loggingInterval) == 0L)) {
            for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
                PlayerStatusLogger logger = this.playerLoggers.get(player.getUuid());

                if (logger != null) {
                    logger.logPlayerStatus(player);
                }
            }
        }
    }

    public void onPlayerLogin(PlayerEntity player) {
        UUID uuid = player.getUuid();

        PlayerStatusLogger oldLogger = this.playerLoggers.get(uuid);

        // The player is already logged in...
        // Flush the old data and create a new logging session
        if (oldLogger != null) {
            oldLogger.flushData();
        }

        this.playerLoggers.put(uuid, new PlayerStatusLogger(uuid, player.getName().getString(), System.currentTimeMillis()));
    }

    public void onPlayerLogout(PlayerEntity player) {
        UUID uuid = player.getUuid();
        PlayerStatusLogger logger = this.playerLoggers.get(uuid);

        if (logger != null) {
            logger.flushData();
            this.playerLoggers.remove(uuid);
        }
    }
}
