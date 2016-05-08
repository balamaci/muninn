package ro.fortsoft.monitor.configuration.rule;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.fortsoft.monitor.Muninn;
import ro.fortsoft.monitor.rule.RuleType;
import ro.fortsoft.monitor.rule.RulesRegistry;
import ro.fortsoft.monitor.rule.base.BaseRule;
import ro.fortsoft.monitor.config.ConfigInjector;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author sbalamaci
 */
@Configuration
public class RulesRegistryConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RulesRegistryConfiguration.class);


    @Bean
    public RulesRegistry createRuleRegistry() {
        RulesRegistry rulesRegistry = new RulesRegistry();

        List<BaseRule> rules = readRules();
        rules.stream().forEach(rulesRegistry::addRule);
        return rulesRegistry;
    }


    private List<BaseRule> readRules() {
        try {
            Path path = Paths.get("./rules");

            RuleFileVisitor ruleFileVisitor = new RuleFileVisitor();
            Files.walkFileTree(path, ruleFileVisitor);

            return ruleFileVisitor.getRules();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Optional<BaseRule> initRule(String filename, Config ruleConfig) {
        String ruleName = ruleConfig.getString("name");
        String ruleType = ruleConfig.getString("type");

        if(ruleConfig.hasPath("disabled")) {
            if(ruleConfig.getBoolean("disabled")) {
                log.info("Rule name='{}' type={} is DISABLED so it won't run", ruleName, ruleType);

                return Optional.empty();
            }
        }

        log.info("Initializing rule name='{}' type={}", ruleName, ruleType);
        try {
            Class ruleTypeClass = ruleTypeClass(ruleType);
            if(! BaseRule.class.isAssignableFrom(ruleTypeClass)) {
                log.error("{} must extend BaseRule", ruleTypeClass.getCanonicalName());
                return Optional.empty();
            }

            Object ruleInstance = ruleTypeClass.getConstructor(Config.class).newInstance(ruleConfig);

            BaseRule rule = (BaseRule) ruleInstance;
            rule.setName(ruleName);

            ConfigInjector ruleConfigInjector = new ConfigInjector(rule, ruleConfig);
            ruleConfigInjector.inject();

            return Optional.of(rule);
        } catch (Exception e) {
            log.error("Error parsing rule with name " + filename, e);
            return Optional.empty();
        }
    }

    private static Class ruleTypeClass(String ruleType) throws ClassNotFoundException {
        Optional<RuleType> ruleTypeOptional = Arrays.stream(RuleType.values())
                .filter(rule -> rule.getRuleName().equalsIgnoreCase(ruleType))
                .findFirst();

        if(ruleTypeOptional.isPresent()) {
            return ruleTypeOptional.get().getRuleType();
        }
        return Class.forName(ruleType);
    }

    private static class RuleFileVisitor extends SimpleFileVisitor<Path> {

        private List<BaseRule> rules = new ArrayList<>();

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attributes) throws IOException {
            if(file.toFile().getName().endsWith(".conf")) {
                Config ruleConfig = ConfigFactory.parseFile(file.toFile()).
                        withFallback(Muninn.config);

                initRule(file.toFile().getName(), ruleConfig).ifPresent(rules::add);
            }

            return FileVisitResult.CONTINUE;
        }

        public List<BaseRule> getRules() {
            return rules;
        }
    }


}
