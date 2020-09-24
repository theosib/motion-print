package eigencraft.motionprint.plugins.location;

import eigencraft.motionprint.api.MotionPrintPlugin;
import eigencraft.motionprint.data.PlayerDataLogger;

import java.util.Collection;

public class LocationPlugin extends MotionPrintPlugin {

    public static final LocationPlugin INSTANCE = new LocationPlugin();

    public LocationPlugin() {
        super("player-location");
    }

    @Override
    public void onEnabled() {
    }

    @Override
    public void onDisabled() {

    }

    @Override
    public void startLogPlayer(PlayerDataLogger newLogger) {
    }

    @Override
    public void stopLogPlayer(PlayerDataLogger logger) {
    }

    @Override
    public void tick(Collection<PlayerDataLogger> players) {
        if (isEnabled()) {
            for (PlayerDataLogger playerLogger : players) {
                playerLogger.logData(PlayerStatusDataEntry.of(playerLogger.getPlayer()));
            }
        }
    }
}
