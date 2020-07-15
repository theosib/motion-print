package eigencraft.motionprint.data;

import net.minecraft.entity.player.PlayerEntity;

public class LogMessage implements IDataEntry {
    protected final long worldTick;
    protected final String message;

    private LogMessage(long worldTick, String message) {
        this.worldTick = worldTick;
        this.message = message;
    }
    @Override
    public String getFormattedOutput() {
        return String.format("%d LOG_MESSAGE | %s\n", this.worldTick, this.message);
    }

    public static LogMessage of(PlayerEntity player, String message) {
        return new LogMessage(player.getEntityWorld().getTime(), message);
    }
}
