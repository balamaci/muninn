package ro.fortsoft.monitor.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import ro.fortsoft.monitor.event.EventGroup;
import ro.fortsoft.monitor.rule.base.BaseRule;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author sbalamaci
 */
public class AlertingGateway implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static final Logger log = LoggerFactory.getLogger(AlertingGateway.class);

    public void alert(BaseRule baseRule, List<EventGroup> events) {
        List<String> alerters = baseRule.getAlerters();
        if(alerters != null) {
            alerters.stream().map((alerterString) -> resolveAlerter(baseRule, alerterString))
                    .filter(Optional::isPresent).map(Optional::get)
                    .forEach(consumeAlert(baseRule, events));
        }
    }

    private Consumer<Alerter> consumeAlert(BaseRule baseRule, List<EventGroup> events) {
        return (alerter) -> {
            try {
                String content = resolveConverter(baseRule, alerter).apply(events);
                alerter.alert(baseRule, content);
            } catch (Exception e) {
                log.error("Error alerting rule=" + baseRule.getName(), e);
            }
        };
    }

    private Optional<? extends Alerter> resolveAlerter(BaseRule baseRule, String alerterName) {
        String springNameAlerter = "alerter." + alerterName;
        try {
            log.debug("Looking up Alerter with name {}", springNameAlerter);
            Alerter alerter = (Alerter) applicationContext.getBean(springNameAlerter, baseRule.getConfig());
            return Optional.of(alerter);
        } catch (BeansException e) {
            log.error("Error finding Alerter=" + springNameAlerter, e);
            return Optional.empty();
        }
    }

    private Function<List<EventGroup>, String> resolveConverter(BaseRule baseRule, Alerter alerter) {
        String converterName = baseRule.getConfig().getString(alerter.getName() + ".converter");
        String springNameConverter = "converter." + converterName;

        return (Function<List<EventGroup>, String>)
                applicationContext.getBean(springNameConverter, baseRule.getConfig());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
