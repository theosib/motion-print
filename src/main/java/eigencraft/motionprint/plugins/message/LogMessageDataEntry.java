package eigencraft.motionprint.plugins.message;

import eigencraft.motionprint.data.IDataEntry;
import net.minecraft.entity.player.PlayerEntity;

public class LogMessageDataEntry implements IDataEntry {
    protected final long worldTick;
    protected final String message;

    private LogMessageDataEntry(long worldTick, String message) {
        this.worldTick = worldTick;
        this.message = message;
    }
    @Override
    public String getFormattedOutput() {
        return String.format("%d LOG_MESSAGE | %s", this.worldTick, this.message);
    }

    public static LogMessageDataEntry of(PlayerEntity player, String message) {
        return new LogMessageDataEntry(player.getEntityWorld().getTime(), message);
    }
}
