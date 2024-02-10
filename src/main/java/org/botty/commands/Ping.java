package org.botty.commands;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Ping extends ListenerAdapter {

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")) {
            event.reply("Ping: Calculating...").setEphemeral(true).queue(response -> {
                long ping = event.getJDA().getGatewayPing();
                response.editOriginalFormat("Ping: %d ms", ping).queue();
            });
        }
    }
}
