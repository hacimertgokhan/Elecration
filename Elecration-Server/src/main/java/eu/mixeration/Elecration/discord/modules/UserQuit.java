package eu.mixeration.Elecration.discord.modules;

import eu.mixeration.Elecration.ElecrationConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class UserQuit extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        String user = event.getMember().getAsMention();
        String guildID = event.getGuild().getId();
        JDA client = event.getJDA();
        String token = ElecrationConfig.config.getString("elecration.discord.output-channel");
        String guild_id = ElecrationConfig.config.getString("elecration.discord.guild-id");
        String message = ElecrationConfig.getMessage("discord.user-quit").replace("<user>", user);
        List<TextChannel> channels = client.getTextChannelsByName(token, true);
        if (guildID.equals(guild_id)) {
            for (TextChannel ch : channels) {
                ch.sendMessage(message).queue();
            }
        }
    }

}
