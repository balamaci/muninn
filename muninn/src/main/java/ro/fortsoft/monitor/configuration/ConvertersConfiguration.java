package ro.fortsoft.monitor.configuration;

import com.typesafe.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import ro.fortsoft.monitor.convert.Converter;
import ro.fortsoft.monitor.util.json.JsonSerializer;

/**
 * @author Serban Balamaci
 */
@Configuration
public class ConvertersConfiguration {

    @Autowired
    private JsonSerializer jsonSerializer;

    @Lazy
    @Bean(name = "converter.json")
//    @Scope(value = "prototype")
    public Converter converter(Config config) {
        return jsonSerializer::toJson;
    }

}
