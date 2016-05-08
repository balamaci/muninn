package ro.fortsoft.monitor.rule.util.template;

import com.typesafe.config.Config;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sbalamaci
 */
public class SimpleTemplate {

    private String templateContent;

    private static final Pattern importPattern = Pattern.compile("@import\\((.*?)\\)");


    public SimpleTemplate(String templateContent) {
        this.templateContent = templateContent;
    }

    public SimpleTemplate mergeWithKeys(Map<String, String> keyValuePairs) {
        for (Map.Entry<String, String> pair : keyValuePairs.entrySet()) {
            templateContent = StringUtils.replace(templateContent, placeholderKey(pair.getKey()), pair.getValue());
        }
        return this;
    }

    private String placeholderKey(final String key) {
        return "${" + key + "}";
    }

    public SimpleTemplate resolveImports(Config config) {
        Matcher m = importPattern.matcher(templateContent);
        List<Pair<String, String>> importKeys = new ArrayList<>();

        while(m.find()) {
            String importKey = m.group(1);
            String importKeyValue = config.getString(importKey);
            importKeys.add(new Pair<>(importKey, importKeyValue));
        }

        for(Pair<String, String> pair : importKeys) {
            templateContent = StringUtils.replace(templateContent, "@import(" + pair.getKey() + ")", pair.getValue());
        }
        return this;
    }

    public String build() {
        return templateContent;
    }

}
