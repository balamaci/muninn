package ro.fortsoft.monitor.rule.base.time;

import java.time.Duration;

/**
 * @author sbalamaci
 */
public interface FrequencyAwareRule {

    Duration getFrequency();

}
