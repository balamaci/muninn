package ro.fortsoft.monitor.scheduler.executor;

import ro.fortsoft.monitor.rule.base.BaseRule;

/**
 * @author sbalamaci
 */
public class RuleExecutor implements Runnable {

    private BaseRule rule;

    public RuleExecutor(BaseRule rule) {
        this.rule = rule;
    }

    @Override
    public void run() {

    }
}
