package ro.fortsoft.monitor.rule;

import com.typesafe.config.Config;
import ro.fortsoft.monitor.rule.base.time.FrequencyBasedWithTimeframeRule;
import ro.fortsoft.monitor.rule.processor.ListEntriesRuleProcessor;

/**
 * @author Serban Balamaci
 */
public class ListEntriesRule extends FrequencyBasedWithTimeframeRule {


    public ListEntriesRule(Config config) {
        super(config);
    }

    @Override
    public Class getRuleProcessor() {
        return ListEntriesRuleProcessor.class;
    }
}
