package ro.fortsoft.monitor.rule.processor.context;

import java.time.Instant;

/**
 * @author sbalamaci
 */
public class ExecutionContext {

    private Instant executionTime;

    public ExecutionContext() {
        executionTime = Instant.now();
    }

    public Instant getExecutionTime() {
        return executionTime;
    }

    @Override
    public String toString() {
        return "ExecutionContext{" +
                "executionTime=" + executionTime +
                '}';
    }
}
