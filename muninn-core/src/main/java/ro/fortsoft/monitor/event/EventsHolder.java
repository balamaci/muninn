package ro.fortsoft.monitor.event;

import java.util.List;

/**
 * @author Serban Balamaci
 */
public class EventsHolder {

    private final Metadata metadata;
    private final List<? extends Event> events;

    public EventsHolder(Metadata metadata, List<? extends Event> events) {
        this.metadata = metadata;
        this.events = events;
    }

    public EventsHolder(List<? extends Event> events) {
        this.events = events;
        this.metadata = new Metadata();
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public List<? extends Event> getEvents() {
        return events;
    }
}
