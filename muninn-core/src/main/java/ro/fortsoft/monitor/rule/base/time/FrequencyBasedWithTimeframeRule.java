package ro.fortsoft.monitor.rule.base.time;

import com.typesafe.config.Config;
import ro.fortsoft.monitor.config.ConfigValue;
import ro.fortsoft.monitor.rule.base.BaseRule;

import java.time.Duration;
import java.time.temporal.TemporalAmount;

/**
 * @author sbalamaci
 */
public abstract class FrequencyBasedWithTimeframeRule extends BaseRule implements FrequencyAwareRule, TimeframeAwareRule {

    @ConfigValue
    private Duration frequency;

    @ConfigValue
    private Duration timeframe;

    public FrequencyBasedWithTimeframeRule(Config config) {
        super(config);
    }


    @Override
    public Duration getFrequency() {
        return frequency;
    }

    public void setFrequency(Duration frequency) {
        this.frequency = frequency;
    }

    @Override
    public TemporalAmount getTimeframe() {
        return this.timeframe;
    }

    public void setTimeframe(Duration timeframe) {
        this.timeframe = timeframe;
    }

}
