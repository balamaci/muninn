package ro.fortsoft.monitor.rule.processor;

import com.google.gson.JsonObject;
import ro.fortsoft.monitor.elasticsearch.response.extractor.Hits;
import ro.fortsoft.monitor.event.Event;
import ro.fortsoft.monitor.event.EventsHolder;
import ro.fortsoft.monitor.event.JsonEvent;
import ro.fortsoft.monitor.rule.base.BaseRule;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Serban Balamaci
 */
public class ListEntriesRuleProcessor extends BaseRuleProcessor implements TimeframeParamsAware {

    public ListEntriesRuleProcessor(BaseRule rule) {
        super(rule);
    }

    @Override
    protected Function<JsonObject, EventsHolder> processResponseFunction() {
        return (jsonObject) -> {
            JsonObject hitsObj = jsonObject.getAsJsonObject("hits");

            List<? extends Event> events = Hits.hitsStream(hitsObj)
                    .map(jsonHitEntry -> {
                        JsonObject source = jsonHitEntry.getAsJsonObject("_source");
                        return new JsonEvent(getIdKeyValue(jsonHitEntry), source, getRealertFilterKeyValue(jsonHitEntry));
                    })
                    .collect(Collectors.toList());
            return new EventsHolder(events);
        };
    }


    @Override
    public Map<String, String> searchParameters() {
        return TimeframeParamsAware.super.searchParameters();
    }
}
