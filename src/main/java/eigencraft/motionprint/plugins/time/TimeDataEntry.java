package eigencraft.motionprint.plugins.time;

import eigencraft.motionprint.data.IDataEntry;

public class TimeDataEntry implements IDataEntry {
    private long time;
    public TimeDataEntry(long time) {
        this.time = time;
    }

    @Override
    public String getFormattedOutput() {
        return String.format("time %d",time);
    }
}
