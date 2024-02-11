package org.botty;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.botty.commands.Ping;
import org.botty.commands.authorizedUserCommands.BanCommandListener;
import org.botty.permissionshandler.AuthorizedUsersManager;

public class Bot extends ListenerAdapter {
    private final static String TOKEN = (Configuration.TOKEN);

    public static void main(String[] args) {
        JDA jda = JDABuilder.createDefault(TOKEN)
                .addEventListeners(new BotReadyListener())
                .build();
        jda.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT); // Enable intents

    }

    private static class BotReadyListener extends ListenerAdapter {
        @Override
        public void onReady(ReadyEvent event) {
            AuthorizedUsersManager authorizedUsersManager = new AuthorizedUsersManager();
            JDA jda = event.getJDA();

            //SlashCommandListeners
            jda.addEventListener(new Ping());
            //AuthorizedCommandListener
            jda.addEventListener(new BanCommandListener(authorizedUsersManager));

            //SlashCommandCall
            initializeSlashCommands(jda);


        }

        private void initializeSlashCommands(JDA jda) {
            jda.upsertCommand("ping", "Check bot's latency")
                    .queue();

        }
    }
}
