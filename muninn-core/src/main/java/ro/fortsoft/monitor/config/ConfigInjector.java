package ro.fortsoft.monitor.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author sbalamaci
 */
public class ConfigInjector {

    private Object target;
    private Config config;

    public ConfigInjector(Object target, Config config) {
        this.target = target;
        this.config = config;
    }

    public void inject() {
        Field[] fields = FieldUtils.getFieldsWithAnnotation(target.getClass(), ConfigValue.class);

        initFields(config, fields);
    }

    private void initFields(Config config, Field[] fields) {
        for(Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();

            Optional<Annotation> annotation = Arrays.stream(annotations).
                    filter(ann -> ann instanceof ConfigValue).findAny();
            if(annotation.isPresent()) {
                setFieldFromConfig(config, field, (ConfigValue) annotation.get());
            }
        }
    }

    private void setFieldFromConfig(Config config, Field field, ConfigValue annotation) {
        String paramName = annotation.value();
        if(paramName.isEmpty()) {
            paramName = field.getName();
        }
        field.setAccessible(true);

        if(annotation.optional() && !config.hasPath(paramName)) {
            return;
        }

        try {
            if (field.getType().isAssignableFrom(String.class)) {
                field.set(target, config.getString(paramName));
            }
            if (field.getType().isAssignableFrom(Integer.class)) {
                field.set(target, config.getInt(paramName));
            }
            if (field.getType().isAssignableFrom(Long.class)) {
                field.set(target, config.getLong(paramName));
            }
            if (field.getType().isAssignableFrom(Duration.class)) {
                field.set(target, config.getDuration(paramName));
            }
            if (field.getType().isAssignableFrom(List.class)) {
                field.set(target, config.getStringList(paramName));
            }
        } catch (ConfigException e) {
            throw new RuntimeException("Could not extract value for " + paramName, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not set field value for " + field.getName(), e);
        }
    }


}
