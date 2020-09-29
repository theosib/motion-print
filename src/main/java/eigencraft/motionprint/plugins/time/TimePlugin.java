package eigencraft.motionprint.plugins.time;

import eigencraft.motionprint.api.MotionPrintPlugin;
import eigencraft.motionprint.data.PlayerDataLogger;

import java.util.Collection;

public class TimePlugin extends MotionPrintPlugin {
    public static final TimePlugin INSTANCE = new TimePlugin();

    public TimePlugin() {
        super("time");
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
        for(PlayerDataLogger dataLogger:players){
            dataLogger.logData(new TimeDataEntry(dataLogger.getPlayer().getEntityWorld().getTime()));
        }
    }
}
