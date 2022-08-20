package eu.mixeration.Elecration.commands.shorts;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static eu.mixeration.Elecration.ElecrationConfig.getMessage;
import static eu.mixeration.Elecration.utils.StringUtils.doColor;

public class PluginList extends Command {
    public String right = doColor("&8&l»");
    public String name = doColor("&9&lElecration");

    public PluginList(String name) {
        super(name);
        this.description = "Plugin List Command.";
        this.usageMessage = "/plugins";
        this.setPermission("elecration.management.plugins");
    }

    @Override
    @Deprecated
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (testPermission(sender)) {
            if (sender instanceof Player) {
                Player player = ((Player) sender).getPlayer();
                /*

                    If sender is player

                 */
                if (player.isOp() || player.hasPermission("elecration.management.plugins")) {
                    player.sendMessage(doColor(right + " &fPlugins &2(" + Bukkit.getPluginManager().getPlugins().length + ");"));
                    for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                        player.sendMessage(doColor("    " + right + " &a" + plugin.getName() + " &7/ &F" + plugin.getDescription().getVersion() + "&7, Author(s) [&2" + plugin.getDescription().getAuthors().size() + "&7]: &f" + plugin.getDescription().getAuthors().toString()));
                    }
                } else {
                    player.sendMessage(getMessage("no-permission"));
                }
            } else {

                /*

                    If sender is console

                 */
                Bukkit.getConsoleSender().sendMessage(doColor(right + " &fPlugins &2(" + Bukkit.getPluginManager().getPlugins().length + ")"));
                for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                    Bukkit.getConsoleSender().sendMessage(doColor(right + " &a" + plugin.getName() + " &7/ &F" + plugin.getDescription().getVersion() + "&7, Author(s) [&2" + plugin.getDescription().getAuthors().size() + "&7]: &f" + plugin.getDescription().getAuthors().toString()));
                }

            }
        }
        return true;
    }
}
