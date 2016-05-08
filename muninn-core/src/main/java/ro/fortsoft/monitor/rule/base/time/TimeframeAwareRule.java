package ro.fortsoft.monitor.rule.base.time;

import java.time.temporal.TemporalAmount;

/**
 * @author sbalamaci
 */
public interface TimeframeAwareRule {

    TemporalAmount getTimeframe();

}
