package eigencraft.motionprint.plugins.events;

import eigencraft.motionprint.api.MotionPrintPlugin;
import eigencraft.motionprint.data.PlayerDataLogger;

import java.util.Collection;

public class EventPlugin extends MotionPrintPlugin {

    public static final EventPlugin INSTANCE = new EventPlugin();

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

    }
}
