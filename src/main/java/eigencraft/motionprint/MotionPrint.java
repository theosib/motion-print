package eigencraft.motionprint;

import eigencraft.motionprint.api.MotionPrintPlugin;
import eigencraft.motionprint.plugins.events.EventPlugin;
import eigencraft.motionprint.plugins.location.LocationPlugin;
import eigencraft.motionprint.plugins.message.MessagePlugin;
import eigencraft.motionprint.plugins.scoreboard.ScoreboardPlugin;
import eigencraft.motionprint.plugins.time.TimePlugin;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MotionPrint implements ModInitializer {
    public static final Logger logger = LogManager.getLogger(Reference.MOD_ID);

    @Override
    public void onInitialize() {
        //Always leave timePlugin at the beginning
        MotionPrintPlugin.register(TimePlugin.INSTANCE);

        MotionPrintPlugin.register(LocationPlugin.INSTANCE);
        MotionPrintPlugin.register(ScoreboardPlugin.INSTANCE);
        MotionPrintPlugin.register(MessagePlugin.INSTANCE);
        MotionPrintPlugin.register(EventPlugin.INSTANCE);
    }
}
