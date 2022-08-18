package eu.mixeration.Elecration;

import com.google.common.base.Throwables;
import eu.mixeration.Elecration.commands.Management_PCC;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ElecrationConfig {

    private static final String HEADER = "Elecration, All rights reserved.";
    /*========================================================================*/
    public static YamlConfiguration config;
    static int version;
    static boolean value_control;
    static Map<String, Command> commands;
    private static File CONFIG_FILE;

    /*========================================================================*/
    public static void init(File configFile) {
        CONFIG_FILE = configFile;
        config = new YamlConfiguration();
        try {
            config.load(CONFIG_FILE);
        } catch (IOException ignored) {
            // ignored..
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load elecration.yml, please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }

        config.options().header(HEADER);
        config.options().copyDefaults(true);

        commands = new HashMap<>();

        version = getInt("config-version", 0);
        set("mxr$elecration", false);
        set("config-version", 0);
        elecrationValues();
        set("mxr$elecration", true);
        commands.put("Pcc", new Management_PCC("Pcc"));
        readConfig(ElecrationConfig.class, null);
    }

    public static void registerCommands() {
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            MinecraftServer.getServer().server.getCommandMap().register(entry.getKey(), "Elecration", entry.getValue());
        }
    }

    static void readConfig(Class<?> clazz, Object instance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(instance);
                    } catch (InvocationTargetException ex) {
                        throw Throwables.propagate(ex.getCause());
                    } catch (Exception ex) {
                        Bukkit.getLogger().log(Level.SEVERE, "Error invoking " + method, ex);
                    }
                }
            }
        }

        try {
            config.save(CONFIG_FILE);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + CONFIG_FILE, ex);
        }
    }

    private static void set(String path, Object val) {
        config.set(path, val);
    }

    private static boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    private static int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    private static <T> List getList(String path, T def) {
        config.addDefault(path, def);
        return config.getList(path, config.getList(path));
    }

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    private static double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    private static void saveElecration() {
        try {
            config.save(CONFIG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void elecrationValues() {
        List<String> help_message = new ArrayList<>();
        help_message.add("");
        help_message.add("§8- §a/Pcc refresh-all §7: Refresh all plugins in your server.");
        help_message.add("§8- §a/Pcc disable-all §7: Disable all plugins in your server.");
        help_message.add("§8- §a/Pcc enable-all §7: Enable all plugins in your server.");
        help_message.add("§8- §a/Pcc disable <plugin> §7: Disable selected plugin in your server.");
        help_message.add("§8- §a/Pcc enable <plugin> §7: Enable selected plugin in your server.");
        help_message.add("§8- §a/Pcc unload <plugin> §7: Unload selected plugin in your server.");
        help_message.add("§8- §a/Pcc load <plugin> §7: Load selected plugin in your server.");
        help_message.add("");
        if (!config.getBoolean("mxr$elecration")) {
            config.set("elecration.messages.help", help_message);
            saveElecration();
        }

    }

}
