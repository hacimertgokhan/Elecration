package eu.mixeration.Elecration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Elecration {
    public static final Logger LOGGER = LogManager.getLogger(Elecration.class);
    private static Elecration INSTANCE;
    public Elecration() {
        INSTANCE = this;
    }
    public static Elecration get() {
        return INSTANCE == null ? new Elecration() : INSTANCE;
    }


}
