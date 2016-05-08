package ro.fortsoft.monitor.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.Optional;

/**
 * @author Serban Balamaci
 */
public class JsonEvent implements Event<JsonObject> {

    private final String id;

    /** FilterKey is used to not send alerts if we already alerted and there is a realert interval*/
    private final String filterKey;

    private JsonObject value;


    public JsonEvent(String id, JsonObject value) {
        this.id = id;
        this.value = value;
        this.filterKey = null;
    }

    public JsonEvent(String id, JsonObject value, String filterKey) {
        this.id = id;
        this.value = value;
        this.filterKey = filterKey;
    }

    @Override
    public Optional<String> getId() {
        if(id != null) {
            return Optional.of(id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getFilterKey() {
        if(filterKey != null) {
            return Optional.of(filterKey);
        }
        return Optional.empty();
    }

    @Override
    public JsonObject getValue() {
        return value;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(this);

        return prettyJson;
    }
}
