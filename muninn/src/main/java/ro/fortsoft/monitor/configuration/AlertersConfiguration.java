package ro.fortsoft.monitor.configuration;

import com.typesafe.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import ro.fortsoft.monitor.alert.AlertingGateway;
import ro.fortsoft.monitor.alert.log.LogAlerter;

/**
 * @author sbalamaci
 */
@Configuration
public class AlertersConfiguration {

    @Bean
    public AlertingGateway alertingGateway() {
        return new AlertingGateway();
    }

    @Lazy
    @Bean(name = "alerter.log")
    @Scope(value = "prototype")
    public LogAlerter logAlerter(Config config) {
        return new LogAlerter();
    }

}
