package eu.mixeration.Elecration.commands;

import eu.mixeration.Elecration.Elecration;
import eu.mixeration.Elecration.ElecrationConfig;
import eu.mixeration.Elecration.utils.IntegerUtils;
import eu.mixeration.Elecration.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import static eu.mixeration.Elecration.ElecrationConfig.getMessage;
import static eu.mixeration.Elecration.utils.StringUtils.doColor;
import static eu.mixeration.Elecration.utils.WorldUtils.copyWorld;
import static org.bukkit.Bukkit.getServer;

public class Management_SVC extends Command {
    public String right = doColor("&8&l»");
    public String name = doColor("&9&lElecration");

    public Management_SVC(String name) {
        super(name);
        this.description = "Server Control Command.";
        this.usageMessage = "/svc";
        this.setPermission("elecration.management.server");
    }

    @Override
    @Deprecated
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (testPermission(sender)) {
            if (sender instanceof Player) {
                Player player = ((Player) sender).getPlayer();
                if (player.isOp()) {
                    if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
                        help(player);
                    } else if (args.length == 2) {
                        if(args[0].equalsIgnoreCase("reload")) {
                            if(args[1].equalsIgnoreCase("motd")) {
                                player.sendMessage(getMessage("server.motd-changed"));
                                for(String motd : ElecrationConfig.config.getStringList("elecration.settings.motd")) {
                                    CraftServer.console.setMotd(doColor(motd.replace("<line>", "\n").replace("<online>", String.valueOf(getServer().getOnlinePlayers()))));
                                }
                            } else {
                                help(player);
                            }
                        } else if (args[0].equalsIgnoreCase("pvp")) {
                            if(args[1].equalsIgnoreCase("deny")) {
                                CraftServer.console.setPVP(false);
                                player.sendMessage(getMessage("server.pvp.deny"));
                            } else if (args[1].equalsIgnoreCase("allow")) {
                                CraftServer.console.setPVP(true);
                                player.sendMessage(getMessage("server.pvp.allow"));
                            } else {
                                player.sendMessage(String.format(getMessage("server.unknow-value"), "deny/allow"));
                            }
                        } else if (args[0].equalsIgnoreCase("flight")) {
                            if(args[1].equalsIgnoreCase("deny")) {
                                CraftServer.console.setAllowFlight(false);
                                player.sendMessage(getMessage("server.flight.deny"));
                            } else if (args[1].equalsIgnoreCase("allow")) {
                                CraftServer.console.setAllowFlight(true);
                                player.sendMessage(getMessage("server.flight.allow"));
                            } else {
                                player.sendMessage(String.format(getMessage("server.unknow-value"), "deny/allow"));
                            }
                        } else if (args[0].equalsIgnoreCase("lmt-ambient")) {
                            if (IntegerUtils.isInt(args[1])) {
                                player.sendMessage(String.format(getMessage("server.spawn-limit-changed"), args[1], "TYPE-OF: Ambient", player.getWorld().getName()));
                                Bukkit.getWorld(player.getWorld().getName()).setAmbientSpawnLimit(Integer.parseInt(args[1]));
                            } else {
                                player.sendMessage(getMessage("server.must-be-int"));
                            }
                        } else if (args[0].equalsIgnoreCase("lmt-monster")) {
                            if (IntegerUtils.isInt(args[1])) {
                                player.sendMessage(String.format(getMessage("server.spawn-limit-changed"), args[1], "TYPE-OF: Monster", player.getWorld().getName()));
                                Bukkit.getWorld(player.getWorld().getName()).setMonsterSpawnLimit(Integer.parseInt(args[1]));
                            } else {
                                player.sendMessage(getMessage("server.must-be-int"));
                            }
                        } else if (args[0].equalsIgnoreCase("lmt-animal")) {
                            if (IntegerUtils.isInt(args[1])) {
                                player.sendMessage(String.format(getMessage("server.spawn-limit-changed"), args[1], "TYPE-OF: Animal", player.getWorld().getName()));
                                Bukkit.getWorld(player.getWorld().getName()).setAnimalSpawnLimit(Integer.parseInt(args[1]));
                            } else {
                                player.sendMessage(getMessage("server.must-be-int"));
                            }
                        } else if (args[0].equalsIgnoreCase("lmt-wateranimal")) {
                            if (IntegerUtils.isInt(args[1])) {
                                player.sendMessage(String.format(getMessage("server.spawn-limit-changed"), args[1], "TYPE-OF: Water Animal", player.getWorld().getName()));
                                Bukkit.getWorld(player.getWorld().getName()).setWaterAnimalSpawnLimit(Integer.parseInt(args[1]));
                            } else {
                                player.sendMessage(getMessage("server.must-be-int"));
                            }
                        } else if (args[0].equalsIgnoreCase("create-world-backup")) {
                            World world = Bukkit.getWorld(args[1]);
                            if (world == null) {
                                player.sendMessage(getMessage("server.world-not-found"));
                            } else {
                                Format dayName = new SimpleDateFormat("EEEE");
                                String str = dayName.format(new Date());
                                String backupName = args[1] + " " + str + "$" + StringUtils.random(4);
                                File worldFolder = Bukkit.getWorld(args[1]).getWorldFolder();
                                File folder = new File(getServer().getWorldContainer().getAbsolutePath(), File.separator + "backup-worlds" + File.separator + backupName);
                                if (!folder.exists()) {
                                    try {
                                        folder.mkdir();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                copyWorld(worldFolder, folder);
                                player.sendMessage(String.format(getMessage("server.backup-done"), world.getName()));
                                player.sendMessage(String.format(getMessage("server.backup-name"), backupName));
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
                Elecration.LOGGER.warn("Hey ! I am sorry but you cannot use that command from console...");
            }
        }
        return true;
    }

    public void help(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            for (String help : ElecrationConfig.config.getStringList("elecration.messages.help.server")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', help));
            }
        } else {
            for (String help : ElecrationConfig.config.getStringList("elecration.messages.help.server")) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', help));
            }
        }
    }

}
