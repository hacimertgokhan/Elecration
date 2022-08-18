package eu.mixeration.Elecration;

import com.google.common.base.Throwables;
import eu.mixeration.Elecration.commands.Management_OP;
import eu.mixeration.Elecration.commands.Management_PCC;
import eu.mixeration.Elecration.utils.StringUtils;
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
import java.security.SecureRandom;
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

    public static String no_permission = StringUtils.doColor("&cYou dont have a enough permission...");

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

    public static String wrong_password = StringUtils.doColor("&cWrong password...");

    private static void saveElecration() {
        try {
            config.save(CONFIG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*

        Locale and Messages

     */
    public static String usage = StringUtils.doColor("&cUsage: &7/Op give|take <player>");
    public static String title = StringUtils.doColor("&a&lOperators:");
    public static String operator_gived = StringUtils.doColor("&cWarning: &7Player %s is operator...");
    public static String operator_tooked = StringUtils.doColor("&cWarning: &7Player %s is not operator...");
    public static String unknow_or_null = StringUtils.doColor("&7Player %s has not played before or player name is null.");

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
        if (config.getBoolean("elecration.settings.use-operator-password-security")) {
            commands.put("Op", new Management_OP("Op"));
        }
        commands.put("Pcc", new Management_PCC("Pcc"));
        readConfig(ElecrationConfig.class, null);
    }

    public static String generateRandomPassword(int len) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        return sb.toString();
    }

    public static void elecrationValues() {

        /* OP | Operator Command |  */
        List<String> operator_message = new ArrayList<>();
        operator_message.add("");
        operator_message.add("§8- §a/Op give <password> <player> §7: Give op a selected player.");
        operator_message.add("§8- §a/Op take <password> <player> §7: Take op a selected player.");
        operator_message.add("§8- §a/Op list §7: Show all operators.");
        operator_message.add("");
        /* OP | Operator Command |  */

        /* PCC | Plugin Control Command |  */
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
        /* PCC | Plugin Control Command |  */

        if (!config.getBoolean("mxr$elecration")) {
            config.set("elecration.messages.help.pcc", help_message);
            config.set("elecration.messages.help.operator", operator_message);
            config.set("elecration.messages.no-permission", no_permission);
            config.set("elecration.messages.operator.wrong-password", wrong_password);
            config.set("elecration.messages.operator.unknow", unknow_or_null);
            config.set("elecration.messages.operator.gived", operator_gived);
            config.set("elecration.messages.operator.tooked", operator_tooked);
            config.set("elecration.messages.operator.usage", usage);
            config.set("elecration.messages.operator.title", title);
            config.set("elecration.settings.use-operator-password-security", true);
            config.set("elecration.settings.operator.password", generateRandomPassword(5));
            saveElecration();

        }

    }

    public static String getMessage(String message) {
        return StringUtils.doColor(config.getString("elecration.messages." + message));
    }


}
