package eu.mixeration.Elecration.commands;

import eu.mixeration.Elecration.ElecrationConfig;
import eu.mixeration.Elecration.utils.WebhookUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static eu.mixeration.Elecration.utils.StringUtils.doColor;

public class Management_DC extends Command {
    public String right = doColor("&8&l»");
    public String name = doColor("&9&lElecration");

    public Management_DC(String name) {
        super(name);
        this.description = "Discord Management Command.";
        this.usageMessage = "/Discord";
        this.setPermission("elecration.management.discord");
    }

    /**
     * Executes the command, returning its success
     *
     * @param sender       Source object which is executing this command
     * @param commandLabel The alias of the command used
     * @param args         All arguments passed to the command, split via ' '
     * @return true if the command was successful, otherwise false
     */
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (testPermission(sender)) {
            if (sender instanceof Player) {
                Player player = ((Player) sender).getPlayer();
                if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                    help(player);
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("message")) {
                        String messageWebHook = ElecrationConfig.config.getString("elecration.discord.message-channel-webhook");
                        StringBuilder str = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            str.append(args[i]).append(" ");
                        }
                        String message = str.toString();
                        WebhookUtils.sendMessageToDiscord(messageWebHook, player.getName(), message);
                    } else {
                        help(player);
                    }
                } else {
                    help(player);
                }
            } else {
                if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                    help(sender);
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("message")) {
                        String messageWebHook = ElecrationConfig.config.getString("elecration.discord.message-channel-webhook");
                        StringBuilder str = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            str.append(args[i]).append(" ");
                        }
                        String message = str.toString();
                        WebhookUtils.sendMessageToDiscord(messageWebHook, "Elecration", message);
                    } else {
                        help(sender);
                    }
                } else {
                    help(sender);
                }
            }
        } else {
            discord(sender);
        }
        return false;
    }

    public void help(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            for (String help : ElecrationConfig.config.getStringList("elecration.messages.help.discord")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', help));
            }
        } else {
            for (String help : ElecrationConfig.config.getStringList("elecration.messages.help.discord")) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', help));
            }
        }
    }

    public void discord(CommandSender sender) {
        for (String help : ElecrationConfig.config.getStringList("elecration.messages.discord.invite")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', help));
        }
    }

}
