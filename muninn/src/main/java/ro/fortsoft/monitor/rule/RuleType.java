package ro.fortsoft.monitor.rule;

/**
 * @author sbalamaci
 */
public enum RuleType {

    THRESHOLD("threshold", ThresholdRule.class),
    LIST("list", ListEntriesRule.class);

    private String ruleName;
    private Class ruleType;

    RuleType(String ruleName, Class ruleType) {
        this.ruleName = ruleName;
        this.ruleType = ruleType;
    }

    public String getRuleName() {
        return ruleName;
    }

    public Class getRuleType() {
        return ruleType;
    }

}
