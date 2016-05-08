package ro.fortsoft.monitor.rule;

import ro.fortsoft.monitor.rule.base.BaseRule;
import ro.fortsoft.monitor.rule.base.time.FrequencyAwareRule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sbalamaci
 */
public class RulesRegistry {

    private List<BaseRule> rules = new ArrayList<>();

    public void addRule(BaseRule rule) {
        rules.add(rule);
    }

    public List<BaseRule> getFrequencyBasedRules() {
        return rules.stream().filter(rule -> rule instanceof FrequencyAwareRule)
                .map(BaseRule.class::cast)
                .collect(Collectors.toList());
    }

}
