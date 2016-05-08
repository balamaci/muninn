package ro.fortsoft.monitor.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ro.fortsoft.monitor.rule.RulesRegistry;
import ro.fortsoft.monitor.rule.base.BaseRule;
import ro.fortsoft.monitor.rule.base.time.FrequencyAwareRule;
import ro.fortsoft.monitor.rule.processor.RuleProcessor;
import ro.fortsoft.monitor.rule.processor.RuleProcessorFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author sbalamaci
 */
public class RulesScheduler {

//    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private List<ExecutorService> executors = new ArrayList<>();

    @Autowired
    private RuleProcessorFactory ruleProcessorFactory;

    @Autowired
    private RulesRegistry rulesRegistry;

    private static final Logger log = LoggerFactory.getLogger(RulesScheduler.class);
    private static final int AWAIT_TERM_SECONDS = 600; //10 mins

    public RulesScheduler() {
/*
        threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(rulesRegistry.getFrequencyBasedRules().size());

        threadPoolTaskScheduler.setThreadNamePrefix(getClass().getSimpleName());
        threadPoolTaskScheduler.setAwaitTerminationSeconds(AWAIT_TERM_SECONDS);
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskScheduler.setErrorHandler(t -> log.error("Error occurred while executing task.", t));
        threadPoolTaskScheduler.setRejectedExecutionHandler((r, e) -> log.error("Execution of task {} was rejected for unknown reasons.", r));
*/
    }

    @PostConstruct
    public void scheduleRules() {
        rulesRegistry.getFrequencyBasedRules().forEach(this::schedule);
    }

    private void schedule(BaseRule baseRule) {
        RuleProcessor ruleProcessor = ruleProcessorFactory.get(baseRule);

        FrequencyAwareRule frequencyAwareRule = (FrequencyAwareRule) baseRule;
        log.info("Scheduling {} to run for {}secs", baseRule.getName(),
                frequencyAwareRule.getFrequency().get(ChronoUnit.SECONDS));
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(ruleProcessor,
                0, frequencyAwareRule.getFrequency().get(ChronoUnit.SECONDS), TimeUnit.SECONDS);
        executors.add(executorService);
    }

    @PreDestroy
    public void shutdown() {
        executors.stream().forEach(ExecutorService::shutdown);
    }

}
