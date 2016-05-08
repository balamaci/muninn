package ro.fortsoft.monitor.event;

/**
 * @author Serban Balamaci
 */
public class Metadata {

    private String ruleId;
    private String ruleName;

    private Integer totalEvents;
    private Integer filteredEvents;

    public Metadata() {
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(Integer totalEvents) {
        this.totalEvents = totalEvents;
    }

    public Integer getFilteredEvents() {
        return filteredEvents;
    }

    public void setFilteredEvents(Integer filteredEvents) {
        this.filteredEvents = filteredEvents;
    }
}
