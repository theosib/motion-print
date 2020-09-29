package eigencraft.motionprint.api;

import eigencraft.motionprint.data.ConsentTracker;
import eigencraft.motionprint.data.IDataEntry;
import eigencraft.motionprint.data.LoggingManager;
import eigencraft.motionprint.data.PlayerDataLogger;
import eigencraft.motionprint.plugins.events.EventDataEntry;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collection;
import java.util.function.Supplier;

public class MotionPrintUtils {
    public static boolean shouldTrackPlayer(PlayerEntity player){
        return ConsentTracker.INSTANCE.hasPlayerConsented(player);
    }
    public static PlayerDataLogger getPlayerDataLogger(PlayerEntity entity){
        return LoggingManager.INSTANCE.getPlayerDataLogger(entity);
    }
    public static void logForPlayer(PlayerEntity entity, Supplier<IDataEntry> supplier){
        if (LoggingManager.INSTANCE.isEnabled()&&shouldTrackPlayer(entity)){
            getPlayerDataLogger(entity).logData(supplier.get());
        }
    }
    public static void logEventForPlayer(PlayerEntity entity, String event){
        if (LoggingManager.INSTANCE.isEnabled()&&shouldTrackPlayer(entity)){
            getPlayerDataLogger(entity).logData(new EventDataEntry(event));
        }
    }
}
