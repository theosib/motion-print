package eigencraft.motionprint.api;

import eigencraft.motionprint.data.ConsentTracker;
import eigencraft.motionprint.data.LoggingManager;
import eigencraft.motionprint.data.PlayerDataLogger;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collection;

public class MotionPrintUtils {
    public static boolean shouldTrackPlayer(PlayerEntity player){
        return ConsentTracker.INSTANCE.hasPlayerConsented(player);
    }
    public static PlayerDataLogger getPlayerDataLogger(PlayerEntity entity){
        return LoggingManager.INSTANCE.getPlayerDataLogger(entity);
    }
}
