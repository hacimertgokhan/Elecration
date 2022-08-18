package eu.mixeration.Elecration.commands;

import eu.mixeration.Elecration.ElecrationConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static eu.mixeration.Elecration.ElecrationConfig.getMessage;
import static eu.mixeration.Elecration.utils.StringUtils.doColor;

public class Management_OP extends Command {
    public String right = doColor("&8&l»");
    public String name = doColor("&9&lElecration");

    public Management_OP(String name) {
        super(name);
        this.description = "Operator Control Command.";
        this.usageMessage = "/op";
        this.setPermission("elecration.management.operator");
    }

    @Override
    @Deprecated
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (testPermission(sender)) {
            if (sender instanceof Player) {
                Player player = ((Player) sender).getPlayer();
                if (player.isOp()) {
                    if (args.length == 3) {
                        if (args[0].equalsIgnoreCase("give")) {
                            String password = ElecrationConfig.config.getString("elecration.settings.operator.password");
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                            if (args[1].equalsIgnoreCase(password)) {
                                if (!(offlinePlayer == null)) {
                                    offlinePlayer.setOp(true);
                                    player.sendMessage(String.format(getMessage("operator.gived"), offlinePlayer.getPlayer().getName()));
                                } else {
                                    player.sendMessage(String.format(getMessage("operator.unknow"), args[2].toString()));
                                }
                            } else {
                                player.sendMessage(getMessage("operator.wrong-password"));
                            }
                        } else if (args[0].equalsIgnoreCase("take")) {
                            String password = ElecrationConfig.config.getString("elecration.settings.operator.password");
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                            if (args[1].equalsIgnoreCase(password)) {
                                if (!(offlinePlayer == null)) {
                                    offlinePlayer.setOp(false);
                                    player.sendMessage(String.format(getMessage("operator.tooked"), offlinePlayer.getPlayer().getName()));

                                } else {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cError: &7Player &f" + args[1] + " &7is null or player didn`t played before."));
                                }
                            } else {
                                player.sendMessage(getMessage("operator.wrong-password"));
                            }
                        }
                    } else if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("list")) {
                            player.sendMessage(getMessage("operator.title"));
                            for (OfflinePlayer operators : Bukkit.getOperators()) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "    &8- &f" + operators.getName()));
                            }
                        } else {
                            help(player);
                        }
                    } else {
                        help(player);
                    }
                } else {
                    player.sendMessage(getMessage("no-permission"));
                }
            } else {
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("give")) {

                        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                        player.setOp(true);

                        Command.broadcastCommandMessage(sender, "Opped " + args[1]);
                        return true;
                    } else if (args[0].equalsIgnoreCase("take")) {

                        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                        player.setOp(false);

                        Command.broadcastCommandMessage(sender, "De-Opped " + args[1]);
                        return true;
                    }
                } else {
                    sender.sendMessage(getMessage("operator.usage"));
                    return false;
                }
            }
        }
        return true;
    }

    public void help(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            for (String help : ElecrationConfig.config.getStringList("elecration.messages.help.operator")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', help));
            }
        } else {
            for (String help : ElecrationConfig.config.getStringList("elecration.messages.help.operator")) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', help));
            }
        }
    }

}
