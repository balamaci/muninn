package ro.fortsoft.monitor.rule.base;

import com.typesafe.config.Config;
import ro.fortsoft.monitor.config.ConfigValue;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * @author sbalamaci
 */
public abstract class BaseRule implements Rule {

    @ConfigValue
    private String id;

    private String name;

    @ConfigValue
    private String type;

    @ConfigValue("elastic.index_pattern")
    private String indexPattern;

    @ConfigValue("search.template.name")
    private String searchTemplate;

    @ConfigValue(value = "search.query_result_key_id", optional = true)
    private String queryResultIdKey;

    @ConfigValue("alerter.alert")
    private List<String> alerters;

    @ConfigValue(value = "alerter.realert.filter_key", optional = true)
    private String realertFilterKey;

    @ConfigValue(value = "alerter.realert.frequency", optional = true)
    private Duration realertFrequency;

    private final Config config;

    public BaseRule(Config config) {
        this.config = config;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndexPattern() {
        return indexPattern;
    }

    public void setIndexPattern(String indexPattern) {
        this.indexPattern = indexPattern;
    }

    public String getSearchTemplate() {
        return searchTemplate;
    }

    public void setSearchTemplate(String searchTemplate) {
        this.searchTemplate = searchTemplate;
    }

    public String getQueryResultIdKey() {
        return queryResultIdKey;
    }

    public void setQueryResultIdKey(String queryResultIdKey) {
        this.queryResultIdKey = queryResultIdKey;
    }

    public List<String> getAlerters() {
        return alerters;
    }

    public void setAlerters(List<String> alerters) {
        this.alerters = alerters;
    }

    public String getRealertFilterKey() {
        return realertFilterKey;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    public Duration getRealertFrequency() {
        return realertFrequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseRule)) return false;
        BaseRule baseRule = (BaseRule) o;
        return Objects.equals(id, baseRule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
