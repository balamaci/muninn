package ro.fortsoft.monitor.rule.base;

import com.typesafe.config.Config;

/**
 * @author sbalamaci
 */
public interface Rule {

    String getId();

    String getName();

    String getType();

    Config getConfig();

    Class getRuleProcessor();

}
