package ro.fortsoft.monitor.storage;

import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author sbalamaci
 */
public class MemoryStorageImpl implements Storage {

    private final Map<String, Map<String, JsonElement>> rulesStorage = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(MemoryStorageImpl.class);

    @Override
    public Optional<JsonElement> load(String ruleId, String key) {
        JsonElement value = rulesStorage.getOrDefault(ruleId, new HashMap<>()).get(key);
        if(value == null) {
            return Optional.empty();
        }
        return Optional.of(value);
    }

    @Override
    public void save(String ruleId, String key, JsonElement content) {
        Map<String, JsonElement> storage = rulesStorage.getOrDefault(ruleId, new HashMap<>());
        storage.put(key, content);
        rulesStorage.put(ruleId, storage);
    }

    @Override
    public void delete(String ruleId, String key) {
        rulesStorage.getOrDefault(key, new HashMap<>()).remove(key);
    }

    @Override
    public void deleteAll(String ruleId) {
        rulesStorage.put(ruleId, new HashMap<>());
    }
}
