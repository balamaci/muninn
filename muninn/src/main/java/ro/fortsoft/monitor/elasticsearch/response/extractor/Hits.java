package ro.fortsoft.monitor.elasticsearch.response.extractor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ro.fortsoft.monitor.elasticsearch.response.extractor.function.json.ExtractJsonObjectFunction;

import java.util.stream.Stream;

import static ro.fortsoft.monitor.elasticsearch.response.extractor.function.json.Json.arrayToStream;

/**
 * @author Serban Balamaci
 */
public class Hits {

    public static ExtractJsonObjectFunction extractHitsArray() {
        return new ExtractJsonObjectFunction("hits");
    }

    public static Stream<JsonObject> hitsStream(JsonObject hitsElement) {
        Stream<JsonElement> hits = extractHitsArray()
                .andThen(arrayToStream())
                .apply(hitsElement);

        return hits.map(JsonElement::getAsJsonObject);
    }
}
