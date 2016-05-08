package ro.fortsoft.monitor.rule;

import com.typesafe.config.Config;
import ro.fortsoft.monitor.config.ConfigValue;
import ro.fortsoft.monitor.rule.base.time.FrequencyBasedWithTimeframeRule;
import ro.fortsoft.monitor.rule.processor.threshold.ThresholdRuleProcessor;

/**
 * @author sbalamaci
 */
public class ThresholdRule extends FrequencyBasedWithTimeframeRule {

    @ConfigValue("min_events")
    private Integer minimumNumberOfEvents;

    public ThresholdRule(Config config) {
        super(config);
    }

    @Override
    public Class getRuleProcessor() {
        return ThresholdRuleProcessor.class;
    }

    public Integer getMinimumNumberOfEvents() {
        return minimumNumberOfEvents;
    }

    public void setMinimumNumberOfEvents(Integer minimumNumberOfEvents) {
        this.minimumNumberOfEvents = minimumNumberOfEvents;
    }
}
