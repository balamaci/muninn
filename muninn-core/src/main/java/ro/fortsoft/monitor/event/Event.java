package ro.fortsoft.monitor.event;

import java.util.Optional;

/**
 * @author sbalamaci
 */
public interface Event<T> {

    Optional<String> getId();

    Optional<String> getFilterKey();

    T getValue();

}
