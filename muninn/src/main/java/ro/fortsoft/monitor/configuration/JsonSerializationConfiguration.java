package ro.fortsoft.monitor.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.fortsoft.monitor.event.Event;
import ro.fortsoft.monitor.event.JsonEvent;
import ro.fortsoft.monitor.util.GsonSerializer;
import ro.fortsoft.monitor.util.json.JsonSerializer;

import java.lang.reflect.Type;

/**
 * @author sbalamaci
 */
@Configuration
public class JsonSerializationConfiguration {

    @Bean
    public JsonSerializer jsonSerializer() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Event.class, new EventSerializer())
                .create();

        return new GsonSerializer(gson);
    }

    private class EventSerializer implements com.google.gson.JsonSerializer<Event> {
        public JsonElement serialize(Event src, Type typeOfSrc, JsonSerializationContext context) {
            if(src instanceof JsonEvent) {
                return fromJsonEvent((JsonEvent) src);
            }
            throw new RuntimeException("Unregistered type adapter");
        }
    }

    private JsonElement fromJsonEvent(JsonEvent event) {
        JsonObject jsonObject = new JsonObject();

        if(event.getId().isPresent()) {
            jsonObject.add("id", new JsonPrimitive(event.getId().get()));
        } else {
            jsonObject.add("id", JsonNull.INSTANCE);
        }

        if(event.getFilterKey().isPresent()) {
            jsonObject.add("filterKey", new JsonPrimitive(event.getFilterKey().get()));
        } else {
            jsonObject.add("filterKey", JsonNull.INSTANCE);
        }

        jsonObject.add("value", event.getValue());

        return jsonObject;
    }

}
