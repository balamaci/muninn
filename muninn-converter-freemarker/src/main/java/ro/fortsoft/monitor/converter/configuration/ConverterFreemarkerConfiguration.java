package ro.fortsoft.monitor.converter.configuration;

import com.typesafe.config.Config;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import ro.fortsoft.monitor.convert.Converter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Serban Balamaci
 */
@org.springframework.context.annotation.Configuration
public class ConverterFreemarkerConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static final Logger log = LoggerFactory.getLogger(ConverterFreemarkerConfiguration.class);

    @Lazy
    @Bean(name = "converter.freemarker")
    @Scope(value = "prototype")
    public Converter converter(Config config) {
        return (events) -> {
            try {
                Configuration freemarkerConfiguration = applicationContext.getBean(Configuration.class, config);

                String templateName = config.getString("alerter.converter.freemarker.template");
                Template template = freemarkerConfiguration.getTemplate(templateName);
                StringWriter output = new StringWriter();

                Map templateModel = new HashMap<>();
                templateModel.put("events", events);

                template.process(templateModel, output);
                String result = output.toString();

                log.debug("Freemarker resulted template {}", result);

                return result;
            } catch (TemplateNotFoundException | TemplateException | MalformedTemplateNameException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
