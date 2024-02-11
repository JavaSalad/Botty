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
        try {
            JDABuilder builder = JDABuilder.createDefault(TOKEN)
                    // Enable intents here
                    .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                    .addEventListeners(new BotReadyListener());

            JDA jda = builder.build();
            jda.awaitReady(); // Optional, but can be useful to ensure JDA is fully initialized
        } catch (Exception e) {
            e.printStackTrace();
        }
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
