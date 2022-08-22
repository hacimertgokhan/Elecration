package eu.mixeration.Elecration.discord;

import eu.mixeration.Elecration.ElecrationConfig;
import eu.mixeration.Elecration.discord.modules.UserJoin;
import eu.mixeration.Elecration.discord.modules.UserQuit;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class DiscordBotLoader {
    public static JDABuilder builder;
    public static boolean userJoinModule = ElecrationConfig.config.getBoolean("elecration.discord.modules.user-join");
    public static boolean userQuitModule = ElecrationConfig.config.getBoolean("elecration.discord.modules.user-quit");

    static void listenerAdapterLoader() {
        if (userJoinModule) {
            builder.addEventListeners(new UserJoin());
        }
        if (userQuitModule) {
            builder.addEventListeners(new UserQuit());
        }
    }

    public static void buildDiscordBotLoader() throws LoginException {
        builder = JDABuilder.createDefault(ElecrationConfig.config.getString("elecration.settings.discord.token"));
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setCompression(Compression.NONE);
        builder.setActivity(Activity.watching(ElecrationConfig.config.getString("elecration.settings.discord.status")));
        listenerAdapterLoader();
        builder.build();
    }

}
