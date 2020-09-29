package eigencraft.motionprint.plugins.events;

import eigencraft.motionprint.data.IDataEntry;

public class EventDataEntry implements IDataEntry {
    private final String event;

    public EventDataEntry(String event) {
        this.event = event;
    }

    @Override
    public String getFormattedOutput() {
        return String.format("event %s",this.event);
    }
}
