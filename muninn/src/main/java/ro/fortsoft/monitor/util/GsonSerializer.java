package ro.fortsoft.monitor.util;

import com.google.gson.Gson;
import ro.fortsoft.monitor.util.json.JsonSerializer;

/**
 * @author sbalamaci
 */
public class GsonSerializer implements JsonSerializer {

    private Gson gson;

    public GsonSerializer(Gson gson) {
        this.gson = gson;
    }

    public <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public String toJson(Object value) {
        return gson.toJson(value);
    }

}
