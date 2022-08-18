package eu.mixeration.Elecration.commands;

import eu.mixeration.Elecration.ElecrationConfig;
import eu.mixeration.Elecration.utils.PluginUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static eu.mixeration.Elecration.utils.StringUtils.doColor;
import static org.bukkit.Bukkit.reload;

public class Management_PCC extends Command {
    public String right = doColor("&8&l⊱");
    public String name = doColor("&9&lElecration");

    public Management_PCC(String name) {
        super(name);
        this.description = "Plugin control command.";
        this.usageMessage = "/pcc";
        this.setPermission("elecration.management.plugins");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (testPermission(sender)) {
            if (sender instanceof Player) {
                Player player = ((Player) sender).getPlayer();
                if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                    help(sender);
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("refresh-all")) {
                        reload();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', name + " " + right + " &7All plugin has been reloaded and refreshed..."));
                    } else if (args[0].equalsIgnoreCase("disable-all")) {
                        Bukkit.getPluginManager().disablePlugins();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', name + " " + right + " &7All plugin has been disabled..."));
                    } else if (args[0].equalsIgnoreCase("enable-all")) {
                        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                            Bukkit.getPluginManager().enablePlugin(plugin);
                        }
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', name + " " + right + " &7All plugin has been enabled..."));
                    } else {
                        help(sender);
                    }
                } else {
                    help(sender);
                }
            } else {
                if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                    help(sender);
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("refresh-all")) {
                        for (Plugin plugins : Bukkit.getPluginManager().getPlugins()) {
                            Bukkit.getPluginManager().disablePlugin(plugins);
                            Bukkit.getPluginManager().enablePlugin(plugins);
                        }
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', name + " " + right + " &7All plugin has been reloaded and refreshed..."));
                    } else if (args[0].equalsIgnoreCase("disable-all")) {
                        Bukkit.getPluginManager().disablePlugins();
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', name + " " + right + " &7All plugin has been disabled..."));
                    } else if (args[0].equalsIgnoreCase("enable-all")) {
                        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                            Bukkit.getPluginManager().enablePlugin(plugin);
                        }
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', name + " " + right + " &7All plugin has been enabled..."));
                    } else {
                        help(sender);
                    }
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("enable")) {
                        Plugin plugin = Bukkit.getPluginManager().getPlugin(args[1]);
                        if (plugin == null) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', name + " " + right + " &7Plugin " + args[1] + " not found..."));
                        }
                        if (plugin.isEnabled()) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', name + " " + right + " &7Plugin " + args[1] + " is already enable..."));
                        } else {
                            Bukkit.getPluginManager().enablePlugin(plugin);
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', name + " " + right + " &7Plugin " + args[1] + " enabled..."));
                        }
                    } else if (args[0].equalsIgnoreCase("disable")) {
                        Plugin plugin = Bukkit.getPluginManager().getPlugin(args[1]);
                        if (plugin == null) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', name + " " + right + " &7Plugin " + args[1] + " not found..."));
                        } else {
                            if (!plugin.isEnabled()) {
                                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', name + " " + right + " &7Plugin " + args[1] + " is already disable..."));
                            } else {
                                Bukkit.getPluginManager().disablePlugin(plugin);
                                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', name + " " + right + " &7Plugin " + args[1] + " disabled..."));
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("load")) {
                        Plugin plugin = Bukkit.getPluginManager().getPlugin(args[1]);
                        if (plugin == null) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', name + " " + right + " &7Plugin " + args[1] + " not found..."));
                        }
                        PluginUtils.load(args[1]);
                    } else if (args[0].equalsIgnoreCase("unload")) {
                        Plugin plugin = Bukkit.getPluginManager().getPlugin(args[1]);
                        if (plugin == null) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', name + " " + right + " &7Plugin " + args[1] + " not found..."));
                        }
                        PluginUtils.unload(plugin);
                    } else {
                        help(sender);
                    }
                } else {
                    help(sender);
                }
            }
        }
        return true;
    }

    public void help(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            for (String help : ElecrationConfig.config.getStringList("elecration.messages.help")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', help));
            }
        } else {
            for (String help : ElecrationConfig.config.getStringList("elecration.messages.help")) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', help));
            }
        }
    }

}
