package eigencraft.motionprint.plugins.events;

import eigencraft.motionprint.data.IDataEntry;

public class EventDataEntry implements IDataEntry {
    private String event;

    public EventDataEntry(String event) {
        this.event = event;
    }

    @Override
    public String getFormattedOutput() {
        return this.event;
    }
}
