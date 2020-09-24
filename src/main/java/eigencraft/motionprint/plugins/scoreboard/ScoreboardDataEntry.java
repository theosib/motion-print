package eigencraft.motionprint.plugins.scoreboard;

import eigencraft.motionprint.data.IDataEntry;
import net.minecraft.entity.player.PlayerEntity;

public class ScoreboardDataEntry implements IDataEntry {
    protected final long worldTick;
    protected final String scoreboard_name;
    protected final int score;

    private ScoreboardDataEntry(long worldTick, String scoreboard_name, int score) {
        this.worldTick = worldTick;
        this.scoreboard_name = scoreboard_name;
        this.score = score;
    }
    @Override
    public String getFormattedOutput() {
        return String.format("%d SCOREBOARD | %s : %d", this.worldTick, this.scoreboard_name,this.score);
    }


    public static ScoreboardDataEntry of(PlayerEntity player, String scoreboardName,int score){
        return new ScoreboardDataEntry(player.getEntityWorld().getTime(),scoreboardName,score);
    }
}
