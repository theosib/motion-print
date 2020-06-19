package eigencraft.motionprint.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.player.PlayerEntity;
import eigencraft.motionprint.MotionPrint;
import eigencraft.motionprint.config.Configs;

public class PlayerStatusLogger {
    protected final List<IDataEntry> data = new ArrayList<>();
    protected final UUID playerUuid;
    protected final String playerName;
    protected final long loginTime;

    public PlayerStatusLogger(UUID playerUuid, String playerName, long loginTime) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.loginTime = loginTime;
    }

    public void logPlayerStatus(PlayerEntity player, int flushInterval) {
        this.logData(PlayerStatusData.of(player), flushInterval);
    }

    public void logData(IDataEntry data, int flushInterval) {
        this.data.add(data);

        if (this.data.size() > flushInterval) {
            this.flushData();
        }
    }

    public void flushData() {
        ArrayList<String> lines = new ArrayList<>();

        for (IDataEntry data : this.data) {
            lines.add(data.getFormattedOutput());
        }

        this.writeDataToFile(lines);
        this.data.clear();
    }

    protected void writeDataToFile(List<String> lines) {
        File dir = getOutputDirectory();

        if (dir == null) {
            MotionPrint.logger.error("PlayerStatusLogger::writeDataToFile(): Failed to get the output directory");
            return;
        }

        String fileName = this.playerName + "_" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date(this.loginTime)) + ".txt";
        File outFile = new File(dir, fileName);

        try {
            if (! outFile.exists() && ! outFile.createNewFile()) {
                MotionPrint.logger.error("PlayerStatusLogger::writeDataToFile(): Failed to create the file '{}'", fileName);
                return;
            }
        }
        catch (IOException e) {
            MotionPrint.logger.error("PlayerStatusLogger::writeDataToFile(): Failed to create the file '{}'", fileName, e);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile, true))) {
            for (String line : lines) {
                writer.write(line);
            }
        }
        catch (IOException e) {
            MotionPrint.logger.error("PlayerStatusLogger::writeDataToFile(): Exception while writing data to file '{}'", fileName, e);
        }
    }

    @Nullable
    private static File getOutputDirectory() {
        File dir = new File(Configs.getGameDirectory(), "motion_print");

        try {
            if (! dir.exists() && ! dir.mkdirs()) {
                MotionPrint.logger.error("PlayerStatusLogger::getOutputDirectory(): Failed to create the output directory");
                return null;
            }
        }
        catch (Exception e) {
            MotionPrint.logger.error("PlayerStatusLogger::getOutputDirectory(): Failed to get the output directory", e);
            return null;
        }

        return dir;
    }
}
