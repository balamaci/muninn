package ro.fortsoft.monitor.elasticsearch.response.extractor.function.json;

import com.google.gson.JsonObject;

import java.util.function.Function;

/**
 * @author Serban Balamaci
 */
public class ExtractJsonPropertyFunction implements Function<JsonObject, JsonObject> {

    private String key;

    public ExtractJsonPropertyFunction(String key) {
        this.key = key;
    }

    @Override
    public JsonObject apply(JsonObject jsonObject) {
        return jsonObject.getAsJsonObject(key);
    }
}
