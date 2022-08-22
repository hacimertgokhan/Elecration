package eu.mixeration.Elecration.discord.modules;

import eu.mixeration.Elecration.ElecrationConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class UserJoin extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String user = event.getMember().getAsMention();
        String message = ElecrationConfig.getMessage("discord.user-join").replace("<user>", user);
        String token = ElecrationConfig.config.getString("elecration.discord.output-channel");
        String guildID = event.getGuild().getId();
        JDA client = event.getJDA();
        String guild_id = ElecrationConfig.config.getString("elecration.discord.guild-id");
        List<TextChannel> channels = client.getTextChannelsByName(token, true);
        if (guildID.equals(guild_id)) {
            for (TextChannel ch : channels) {
                ch.sendMessage(message).queue();
            }
        }
    }

}
