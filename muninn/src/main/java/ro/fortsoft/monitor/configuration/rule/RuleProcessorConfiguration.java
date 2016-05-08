package ro.fortsoft.monitor.configuration.rule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import ro.fortsoft.monitor.rule.base.BaseRule;
import ro.fortsoft.monitor.rule.processor.ListEntriesRuleProcessor;
import ro.fortsoft.monitor.rule.processor.RuleProcessorFactory;
import ro.fortsoft.monitor.rule.processor.threshold.ThresholdRuleProcessor;

/**
 * @author Serban Balamaci
 */
@Configuration
public class RuleProcessorConfiguration {

    @Bean
    public RuleProcessorFactory rulesProcessorFactory() {
        return new RuleProcessorFactory();
    }

    @Bean
    @Lazy
    @Scope(value = "prototype")
    public ThresholdRuleProcessor thresholdRuleProcessor(BaseRule rule) {
        return new ThresholdRuleProcessor(rule);
    }

    @Bean
    @Lazy
    @Scope(value = "prototype")
    public ListEntriesRuleProcessor listEntriesRuleProcessor(BaseRule rule) {
        return new ListEntriesRuleProcessor(rule);
    }

}
