package ro.fortsoft.monitor.elasticsearch.response.extractor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ro.fortsoft.monitor.elasticsearch.response.extractor.function.json.ExtractJsonObjectFunction;
import ro.fortsoft.monitor.elasticsearch.response.extractor.function.json.Json;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static ro.fortsoft.monitor.elasticsearch.response.extractor.function.json.Json.arrayToStream;

/**
 * @author sbalamaci
 */
public class Aggregations {

    public static ExtractJsonObjectFunction extractAggregations() {
        return new ExtractJsonObjectFunction("aggregations");
    }

    public static Stream<JsonObject> withAggregationsResponse(JsonObject aggregation,
                                                              Function<JsonObject, JsonObject> aggregationConsumer) {
        return aggregationsStream(aggregation).map(aggregationConsumer);
    }

    public static Stream<JsonObject> aggregationsStream(JsonObject aggregationElement) {
        Stream<JsonElement> aggregations = extractAggregations()
                .andThen(arrayToStream())
                .apply(aggregationElement);

        return aggregations.map(JsonElement::getAsJsonObject);
    }

    public static Stream<JsonObject> bucketsStream(JsonObject aggregation) {
        Optional<Map.Entry<String, JsonElement>> nameNode = aggregation.entrySet().stream().findFirst();
        if(nameNode.isPresent()) {
            JsonObject nameNodeElement = nameNode.get().getValue().getAsJsonObject();
            Stream<JsonElement> buckets = Json.extractJsonArray("buckets")
                    .andThen(arrayToStream())
                    .apply(nameNodeElement);

            return buckets.map(JsonElement::getAsJsonObject);
        }
        return Stream.empty();
    }

    public static void consumeAggregationsResponse(JsonObject aggregation, Consumer<JsonObject> aggregationConsumer) {
        withAggregationsResponse(aggregation, Function.identity())
                .forEach(aggregationConsumer);
    }

}
