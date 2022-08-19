package eu.mixeration.Elecration;

import com.google.common.collect.Sets;
import dev.cobblesword.nachospigot.Nacho;
import dev.cobblesword.nachospigot.hitdetection.LagCompensator;
import dev.cobblesword.nachospigot.protocol.MovementListener;
import dev.cobblesword.nachospigot.protocol.PacketListener;
import eu.mixeration.Elecration.utils.WebhookUtils;
import me.elier.nachospigot.config.NachoConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.sculas.nacho.anticrash.AntiCrash;
import xyz.sculas.nacho.async.AsyncExplosions;

import java.util.Set;

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
