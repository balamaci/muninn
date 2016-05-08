package ro.fortsoft.monitor.rule.processor;

import com.google.gson.JsonObject;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ro.fortsoft.monitor.Muninn;
import ro.fortsoft.monitor.elasticsearch.ElasticSearchGateway;
import ro.fortsoft.monitor.elasticsearch.response.extractor.function.json.Json;
import ro.fortsoft.monitor.event.EventsHolder;
import ro.fortsoft.monitor.notification.Notifications;
import ro.fortsoft.monitor.rule.base.BaseRule;
import ro.fortsoft.monitor.rule.processor.context.ExecutionContext;
import ro.fortsoft.monitor.rule.util.template.SimpleTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author sbalamaci
 */
public abstract class BaseRuleProcessor implements RuleProcessor {

    @Autowired
    protected ElasticSearchGateway elasticSearchGateway;

    @Autowired
    protected Notifications notifications;

    protected final BaseRule rule;

    private static final Logger log = LoggerFactory.getLogger(BaseRuleProcessor.class);

    public BaseRuleProcessor(BaseRule rule) {
        this.rule = rule;
    }

    @Override
    public void run() {
        log.info("Running '{}'", rule.getName());
        try {
            ExecutionContext executionContext = buildExecutionContext();

            String query = prepareQueryString(executionContext);
            JsonObject jsonObject = queryElasticSearch(query);

            EventsHolder events =
                    processResponseFunction().andThen(processEventsFunction()).
                    apply(jsonObject);

            notifications.notify(rule, executionContext, events);
        } catch (Exception e) {
            log.error("Error executing rule", e);
        }
    }

    protected ExecutionContext buildExecutionContext() {
        return new ExecutionContext();
    }

    /**
     * Function
     * @return
     */
    protected abstract Function<JsonObject, EventsHolder> processResponseFunction();

    /**
     * Function to modify the produced list of events before passing it for notifications
     * @return Function
     */
    protected Function<EventsHolder, EventsHolder> processEventsFunction() {
        return Function.identity();
    }


    public String prepareQueryString(ExecutionContext executionContext) {
        String template = rule.getSearchTemplate();

        String query = "";

        if(rule.getConfig().hasPath("search.template")) {
            String baseSearchTemplate = loadSearchTemplate(template);

            query = new SimpleTemplate(baseSearchTemplate)
                    .resolveImports(rule.getConfig())
                    .mergeWithKeys(searchParameters())
                    .build();
        }
        return query;
    }



    protected String getIdKeyValue(JsonObject json) {
        String idKey = rule.getQueryResultIdKey();
        if(idKey == null) {
            return null;
        }

        return Json.getJsonKeyValueAsString(json, idKey).orElse(null);
    }


    protected String getRealertFilterKeyValue(JsonObject json) {
        String filterKey = rule.getRealertFilterKey();
        if(filterKey == null) {
            return null;
        }

        return Json.getJsonKeyValueAsString(json, filterKey).orElse(null);
    }


    public abstract Map<String, String> searchParameters();

    private String loadSearchTemplate(String type) {
        Config searchConfig = rule.getConfig().withFallback(Muninn.searchTypesConfig);
        return searchConfig.getString("search.templates." + type);
    }

    protected List<String> getIndexes() {
        return Collections.singletonList(rule.getIndexPattern());
    }

    public JsonObject queryElasticSearch(String query) throws IOException {
        JsonObject jsonObject = elasticSearchGateway.submit(getIndexes(), query);

        System.out.println("Response " + Json.prettyPrint(jsonObject));
        return jsonObject;
    }

    @Override
    public BaseRule getRule() {
        return rule;
    }
}
