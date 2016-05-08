package ro.fortsoft.monitor.rule.processor;

import ro.fortsoft.monitor.rule.base.BaseRule;

/**
 * @author sbalamaci
 */
public interface RuleProcessor extends Runnable {

    BaseRule getRule();

}
