package eu.mixeration.Elecration;

import com.google.common.base.Throwables;
import eu.mixeration.Elecration.commands.Management_OP;
import eu.mixeration.Elecration.commands.Management_PCC;
import eu.mixeration.Elecration.commands.Management_SVC;
import eu.mixeration.Elecration.utils.StringUtils;
import eu.mixeration.Elecration.utils.WebhookUtils;
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

import static net.minecraft.server.MinecraftServer.getServer;

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
            getServer().server.getCommandMap().register(entry.getKey(), "Elecration", entry.getValue());
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
        if(config.getBoolean("mxr$elecration")) {
            Elecration.LOGGER.info("elecration.yml loaded...");
        } else {
            elecrationValues();
        }
        if (config.getBoolean("elecration.settings.use-operator-password-security")) {
            commands.put("Op", new Management_OP("Op"));
            commands.put("DeOp", new Management_OP("DeOp"));
        }
        commands.put("Pcc", new Management_PCC("Pcc"));
        commands.put("Svc", new Management_SVC("Svc"));
        readConfig(ElecrationConfig.class, null);
        if(ElecrationConfig.config.getBoolean("elecration.settings.use-discord")) {
            Elecration.LOGGER.info( "Sending test message for discord..." );
            WebhookUtils.sendMessageToDiscord(ElecrationConfig.config.getString("elecration.settings.discord.token"), "Elecration", "Your server has just started...");
        } else {
            Elecration.LOGGER.info( "Elecration Discord Module is not enable, Skipping..." );
        }
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

        /* PCC | Plugin Control Command |  */
        List<String> server_message = new ArrayList<>();
        server_message.add("");
        server_message.add("§8- §a/Svc reload motd §7: Refresh server motd.");
        server_message.add("§8- §a/Svc pvp deny/allow §7: Enable or disable pvp.");
        server_message.add("§8- §a/Svc flight deny/allow §7: Enable or disable pvp.");
        server_message.add("§8- §a/Svc lmt-ambient §7: Change ambient entity limits.");
        server_message.add("§8- §a/Svc lmt-monster §7: Change ambient entity limits.");
        server_message.add("§8- §a/Svc lmt-animal §7: Change ambient entity limits.");
        server_message.add("§8- §a/Svc lmt-wateranimal §7: Change ambient entity limits.");
        server_message.add("§8- §a/Svc create-world-backup <world-name> §7: Create world backup (Copy)");
        server_message.add("");
        /* PCC | Plugin Control Command |  */

        if (!config.getBoolean("mxr$elecration")) {
            config.set("elecration.messages.help.pcc", help_message);
            config.set("elecration.messages.help.operator", operator_message);
            config.set("elecration.messages.help.server", server_message);
            config.set("elecration.messages.no-permission", no_permission);
            config.set("elecration.messages.operator.wrong-password", wrong_password);
            config.set("elecration.messages.operator.unknow", unknow_or_null);
            config.set("elecration.messages.operator.gived", operator_gived);
            config.set("elecration.messages.operator.tooked", operator_tooked);
            config.set("elecration.messages.operator.usage", usage);
            config.set("elecration.messages.operator.title", title);
            config.set("elecration.messages.server.must-be-int", "§7Cant understand, must be §c(0-9/INTEGER)");
            config.set("elecration.messages.server.unknow-value", "§7Cant understand, must be §c(%s)");
            config.set("elecration.messages.server.motd-changed", "§7Server motd just changed");
            config.set("elecration.messages.server.pvp.allow", "§7Players can fight (PCF): Allow");
            config.set("elecration.messages.server.pvp.deny", "§7Players can fight (PCF): Deny");
            config.set("elecration.messages.server.flight.allow", "§7Players can flight (PCFLY): Allow");
            config.set("elecration.messages.server.world-not-found", "§7Cant found world with selected name.");
            config.set("elecration.messages.server.backup-done", "§2§lBACKUP DONE ! (COPIED) §a(World: %s)");
            config.set("elecration.messages.server.backup-name", "§7Backup folder name is: §a(%s)");
            config.set("elecration.messages.server.flight.deny", "§7Players can flight (PCFLY): Deny");
            config.set("elecration.messages.server.spawn-limit-changed", "§7Limit: §e(%s) §f- §bType: (%s) §f- §cWorld: (%s)");
            config.set("elecration.settings.use-operator-password-security", true);
            config.set("elecration.settings.use-motd", true);
            config.set("elecration.settings.use-discord", true);
            config.set("elecration.settings.discord.token", "https://discord.com/api/webhooks/1010134066357600316/Tsn48ucRV46FPzjA4TKVzLy3LkbI-Oo4him2sCoZlS5lADUkwwjJ54ubC1qk7vva5w_4");
            config.set("elecration.settings.operator.password", generateRandomPassword(7));
            set("mxr$elecration", true);
            changeMotd();
            saveElecration();

        }

    }

    public static void changeMotd(){
        if(config.getBoolean("elecration.settings.use-motd")) {

            /* MOTD |  */
            List<String> motd_texts = new ArrayList<>();
            motd_texts.add("§9§lELECRATION §8- §7Like electron §8- §fMixeration<line>§fElecration Spigot 1.8.8 Modernized Fork");
            /* MOTD |  */

            config.set("elecration.settings.motd", motd_texts);

        }
    }

    public static String getMessage(String message) {
        return StringUtils.doColor(config.getString("elecration.messages." + message));
    }


}
