package ro.fortsoft.monitor.storage;

import com.google.gson.JsonElement;

import java.util.Optional;

/**
 * @author sbalamaci
 */
public interface Storage {

    public Optional<JsonElement> load(String ruleId, String key);

    public void save(String ruleId, String key, JsonElement content);

    public void delete(String ruleId, String key);

    public void deleteAll(String ruleId);

    public static enum Key {
        ALERTS;
    }

}
