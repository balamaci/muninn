package ro.fortsoft.monitor.rule.processor.threshold;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import ro.fortsoft.monitor.elasticsearch.response.extractor.Aggregations;
import ro.fortsoft.monitor.event.Event;
import ro.fortsoft.monitor.event.EventsHolder;
import ro.fortsoft.monitor.event.JsonEvent;
import ro.fortsoft.monitor.rule.ThresholdRule;
import ro.fortsoft.monitor.rule.base.BaseRule;
import ro.fortsoft.monitor.rule.processor.AggregationsResponseRuleProcessor;
import ro.fortsoft.monitor.rule.processor.TimeframeParamsAware;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sbalamaci
 */
public class ThresholdRuleProcessor extends AggregationsResponseRuleProcessor implements TimeframeParamsAware{

    private static final String COUNT_KEY = "doc_count";

    public ThresholdRuleProcessor(BaseRule baseRule) {
        super(baseRule);
    }

    @Override
    public Function<JsonObject, EventsHolder> processResponseFunction() {
        return (jsonObject) -> {
            List<? extends Event> events = Aggregations
                    .aggregationsStream(jsonObject)
                    .flatMap(Aggregations::bucketsStream)
                    .map(jsonBucket -> {
                        int count = jsonBucket.get(COUNT_KEY).getAsInt();

                        JsonObject eventValue = new JsonObject();
                        eventValue.add(COUNT_KEY, new JsonPrimitive(count));

                        return new JsonEvent(getIdKeyValue(jsonBucket), eventValue,
                                getRealertFilterKeyValue(jsonBucket));
                    }).collect(Collectors.toList());
            return new EventsHolder(events);
        };
    }

    @Override
    protected Function<EventsHolder, EventsHolder> processEventsFunction() {
        return (events) -> {
            List<? extends Event> filteredEvents = events.getEvents().stream()
                    .map((ev) -> (JsonEvent) ev)
                    .filter(ev -> {
                        ThresholdRule thresholdRule = (ThresholdRule) getRule();

                        int count = ev.getValue().get(COUNT_KEY).getAsInt();
                        return count >= thresholdRule.getMinimumNumberOfEvents();
                    }).collect(Collectors.toList());
            return new EventsHolder(events.getMetadata(), filteredEvents);
        };
    }


    @Override
    public Map<String, String> searchParameters() {
        return TimeframeParamsAware.super.searchParameters();
    }
}
