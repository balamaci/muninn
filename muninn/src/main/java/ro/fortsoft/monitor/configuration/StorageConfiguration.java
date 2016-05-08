package ro.fortsoft.monitor.configuration;

import com.typesafe.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import ro.fortsoft.monitor.storage.MemoryStorageImpl;
import ro.fortsoft.monitor.storage.Storage;

/**
 * @author Serban Balamaci
 */
@Configuration
public class StorageConfiguration {

    @Lazy
    @Bean(name = "storage.memory")
    public Storage memoryStorage(Config config) {
        return new MemoryStorageImpl();
    }

}
