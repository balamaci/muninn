package ro.fortsoft.monitor;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ro.fortsoft.monitor.configuration.AlertersConfiguration;
import ro.fortsoft.monitor.configuration.ConvertersConfiguration;
import ro.fortsoft.monitor.configuration.ElasticSearchConfiguration;
import ro.fortsoft.monitor.configuration.JsonSerializationConfiguration;
import ro.fortsoft.monitor.configuration.NotificationConfiguration;
import ro.fortsoft.monitor.configuration.StorageConfiguration;
import ro.fortsoft.monitor.configuration.rule.RuleProcessorConfiguration;
import ro.fortsoft.monitor.configuration.rule.RulesRegistryConfiguration;
import ro.fortsoft.monitor.configuration.rule.RulesSchedulerConfiguration;

/**
 * @author Serban Balamaci
 */
public class Muninn {

    public static final Config config = ConfigFactory.load("muninn.conf");
    public static final Config searchTypesConfig = ConfigFactory.load("search.conf");

    public void start() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setDisplayName("Muninn");

        context.register(JsonSerializationConfiguration.class);
        context.register(RulesRegistryConfiguration.class);
        context.register(RulesSchedulerConfiguration.class);

        context.register(ElasticSearchConfiguration.class);

        context.register(RuleProcessorConfiguration.class);

        context.register(ConvertersConfiguration.class);
        context.register(AlertersConfiguration.class);
        context.register(StorageConfiguration.class);
        context.register(NotificationConfiguration.class);

        context.scan("ro.fortsoft.monitor.converter.configuration");
        context.scan("ro.fortsoft.monitor.alert.configuration");

        context.refresh();
    }

}
