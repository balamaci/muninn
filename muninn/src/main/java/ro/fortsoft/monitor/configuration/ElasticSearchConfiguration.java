package ro.fortsoft.monitor.configuration;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.fortsoft.monitor.Muninn;
import ro.fortsoft.monitor.elasticsearch.ElasticSearchGateway;

/**
 * @author Serban Balamaci
 */
@Configuration
public class ElasticSearchConfiguration {


    @Bean
    public ElasticSearchGateway elasticSearchGateway() {
        // Construct a new Jest client according to configuration via factory
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder(Muninn.config.getString("elastic.endpoint"))
                .multiThreaded(true)
                .build());
        JestClient client = factory.getObject();

        ElasticSearchGateway elasticSearchGateway = new ElasticSearchGateway(client);
        return elasticSearchGateway;
    }

}
