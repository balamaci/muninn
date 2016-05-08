package ro.fortsoft.monitor.convert;

import ro.fortsoft.monitor.event.EventGroup;

import java.util.List;
import java.util.function.Function;

/**
 * @author sbalamaci
 */
public interface Converter extends Function<List<EventGroup>, String> {
}
