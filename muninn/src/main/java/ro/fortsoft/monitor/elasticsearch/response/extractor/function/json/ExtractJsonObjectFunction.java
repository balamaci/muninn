package ro.fortsoft.monitor.elasticsearch.response.extractor.function.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.function.Function;

/**
 * @author sbalamaci
 */
public class ExtractJsonObjectFunction implements Function<JsonObject, JsonArray> {

    private String property;

    public ExtractJsonObjectFunction(String property) {
        this.property = property;
    }

    @Override
    public JsonArray apply(JsonObject jsonObject) {
        if(jsonObject == null) {
            return null;
        }

        boolean hasAggregations = jsonObject.has(property);
        if(hasAggregations) {
            JsonElement jsonElement = jsonObject.get(property);

            if(jsonElement instanceof JsonArray) {
                return (JsonArray) jsonElement;
            } else {
                JsonArray array = new JsonArray();
                array.add(jsonElement);
                return array;
            }
        }

        return new JsonArray();
    }
}
