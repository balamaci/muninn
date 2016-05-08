package ro.fortsoft.monitor.converter.configuration;

import com.typesafe.config.Config;
import freemarker.template.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import java.io.File;
import java.io.IOException;

/**
 * @author Serban Balamaci
 */
@org.springframework.context.annotation.Configuration
public class FreemarkerConfiguration {

    @Bean
    @Lazy
    public Configuration freemarkerConfig(Config config) {
        Configuration configuration = new freemarker.template.Configuration(Configuration.VERSION_2_3_21);

        String templateDirString = config.getString("alerter.converter.freemarker.templates_dir");
        File templateDir = new File(templateDirString);
        try {
            configuration.setDirectoryForTemplateLoading(templateDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not set Freemarker template directory");
        }
        return configuration;
    }


}
