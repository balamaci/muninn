package ro.fortsoft.monitor.util.json;

/**
 * @author sbalamaci
 */
public interface JsonSerializer {

    <T> T fromJson(String json, Class<T> classOfT);

    String toJson(Object value);

}
