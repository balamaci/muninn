package ro.fortsoft.monitor.notification.entity;

import java.time.Instant;
import java.util.Set;

/**
 * @author sbalamaci
 */
public class SentAlerts {

    private Instant time;
    private Set<String> ids;


    public SentAlerts(Instant time, Set<String> ids) {
        this.time = time;
        this.ids = ids;
    }

    public Instant getTime() {
        return time;
    }

    public Set<String> getIds() {
        return ids;
    }
}
