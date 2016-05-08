package ro.fortsoft.monitor.configuration.rule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.fortsoft.monitor.scheduler.RulesScheduler;

/**
 * @author sbalamaci
 */
@Configuration
public class RulesSchedulerConfiguration {

    @Bean
    public RulesScheduler rulesScheduler() {
        return new RulesScheduler();
    }


}
