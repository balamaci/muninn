package ro.fortsoft.monitor.elasticsearch.response.extractor.function.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Optional;

/**
 * @author sbalamaci
 */
public class Json {

    public static JsonArrayToStreamFunction arrayToStream() {
        return new JsonArrayToStreamFunction();
    }

    public static ExtractJsonObjectFunction extractJsonArray(String property) {
        return new ExtractJsonObjectFunction(property);
    }

    public static Optional<String> getJsonKeyValueAsString(JsonObject jsonObject, String key) {
        JsonElement value = jsonObject.get(key);
        if(value != null) {
            return Optional.of(value.toString());
        }

        return Optional.empty();
    }


    public static String prettyPrint(JsonObject jsonObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }

    public static JsonElement fromString(String content) {
        JsonParser parser = new JsonParser();
        return parser.parse(content);
    }

}
