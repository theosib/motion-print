package eigencraft.motionprint;

import eigencraft.motionprint.api.MotionPrintPlugin;
import eigencraft.motionprint.plugins.location.LocationPlugin;
import eigencraft.motionprint.plugins.message.MessagePlugin;
import eigencraft.motionprint.plugins.scoreboard.ScoreboardPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.api.ModInitializer;

public class MotionPrint implements ModInitializer {
    public static final Logger logger = LogManager.getLogger(Reference.MOD_ID);

    @Override
    public void onInitialize() {
        MotionPrintPlugin.register(LocationPlugin.INSTANCE);
        MotionPrintPlugin.register(ScoreboardPlugin.INSTANCE);
        MotionPrintPlugin.register(MessagePlugin.INSTANCE);
    }
}
