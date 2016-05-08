package ro.fortsoft.monitor.event;

import ro.fortsoft.monitor.rule.processor.context.ExecutionContext;

/**
 * @author Serban Balamaci
 */
public class EventGroup {

    private ExecutionContext key;
    private EventsHolder eventsHolder;


    public EventGroup(ExecutionContext key, EventsHolder eventsHolder) {
        this.key = key;
        this.eventsHolder = eventsHolder;
    }

    public ExecutionContext getKey() {
        return key;
    }

    public EventsHolder getEventsHolder() {
        return eventsHolder;
    }
}
