package ro.fortsoft.monitor.alert.configuration;

import com.typesafe.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import ro.fortsoft.monitor.alert.mail.MailAlerter;

/**
 * @author sbalamaci
 */

@Configuration
public class MailAlerterConfiguration {

    @Lazy
    @Bean(name = "alerter.email")
    @Scope(value = "prototype")
    public MailAlerter mailAlerter(Config config) {
        return new MailAlerter(config);
    }


}
