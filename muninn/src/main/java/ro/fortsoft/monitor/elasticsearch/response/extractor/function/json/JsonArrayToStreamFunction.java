package ro.fortsoft.monitor.elasticsearch.response.extractor.function.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author sbalamaci
 */
public class JsonArrayToStreamFunction implements Function<JsonArray, Stream<JsonElement>>{
    @Override
    public Stream<JsonElement> apply(JsonArray array) {
        return StreamSupport.stream(array.spliterator(), false);
    }
}
