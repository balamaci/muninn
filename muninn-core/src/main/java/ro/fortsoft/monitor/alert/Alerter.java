package ro.fortsoft.monitor.alert;

import ro.fortsoft.monitor.rule.base.BaseRule;

/**
 * @author sbalamaci
 */
public interface Alerter {

    String getName();

    void alert(BaseRule baseRule, String content) throws Exception;

}
