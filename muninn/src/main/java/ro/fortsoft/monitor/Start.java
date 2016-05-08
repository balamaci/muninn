package ro.fortsoft.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sbalamaci
 */
public class Start {

    private static final Logger log = LoggerFactory.getLogger(Start.class);

    public static void main(String[] args) {
        Muninn muninn = new Muninn();
        muninn.start();
    }

}
