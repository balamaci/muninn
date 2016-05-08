package ro.fortsoft.monitor.rule.processor;

import ro.fortsoft.monitor.rule.base.time.TimeframeAwareRule;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sbalamaci
 */
public interface TimeframeParamsAware extends RuleProcessor {

    default Map<String, String> searchParameters() {
        Map<String, String> keys = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        TimeframeAwareRule timeframeAwareRule = (TimeframeAwareRule) getRule();
        LocalDateTime ago = now.minus(timeframeAwareRule.getTimeframe());

        keys.put("from", ago.toString());
        keys.put("to", now.toString());

        return keys;
    }


}
