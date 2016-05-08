package ro.fortsoft.monitor.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.fortsoft.monitor.notification.Notifications;

/**
 * @author Serban Balamaci
 */
@Configuration
public class NotificationConfiguration {

    @Bean
    public Notifications notifications() {
        return new Notifications();
    }

}
