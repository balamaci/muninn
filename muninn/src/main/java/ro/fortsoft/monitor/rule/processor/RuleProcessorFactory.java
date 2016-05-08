package ro.fortsoft.monitor.rule.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import ro.fortsoft.monitor.elasticsearch.ElasticSearchGateway;
import ro.fortsoft.monitor.rule.base.BaseRule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sbalamaci
 */
public class RuleProcessorFactory implements ApplicationContextAware {

    private Map<String, RuleProcessor> processorMap = new ConcurrentHashMap<>();

    @Autowired
    private ElasticSearchGateway elasticSearchGateway;

    private ApplicationContext applicationContext;

    public RuleProcessorFactory() {
    }


    public RuleProcessor get(BaseRule rule) {
        processorMap.computeIfAbsent(rule.getId(), (key)-> {
                Object bean = applicationContext.getBean(rule.getRuleProcessor(), rule);
                return (RuleProcessor) bean;
             });

        return processorMap.get(rule.getId());
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
