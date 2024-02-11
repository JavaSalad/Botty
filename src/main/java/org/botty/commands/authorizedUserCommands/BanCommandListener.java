package org.botty.commands.authorizedUserCommands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.botty.permissionshandler.AuthorizedUsersManager;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BanCommandListener extends ListenerAdapter {

    private AuthorizedUsersManager authorizedUsersManager;
    private static final Pattern MENTION_PATTERN = Pattern.compile("<@!?([0-9]+)>");

    public BanCommandListener(AuthorizedUsersManager manager) {
        this.authorizedUsersManager = manager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        // Check if the command is '!ban'
        if (args[0].equalsIgnoreCase("!ban")) {
            String authorId = event.getAuthor().getId();

            // Check if the author is authorized to use the command
            if (authorizedUsersManager.isAuthorized(authorId)) {
                // Process the ban command
                if (args.length < 2) {
                    event.getChannel().sendMessage("Please mention a user to ban.").queue();
                    return;
                }

                // Parse the mentioned user
                Matcher matcher = MENTION_PATTERN.matcher(args[1]);
                if (matcher.matches()) {
                    String userId = matcher.group(1);
                    Member target = event.getGuild().retrieveMemberById(userId).complete();

                    if (target != null) {
                        //String reason = args.length > 2 ? String.join(" ", args).substring(args[0].length() + args[1].length()).trim() : "No reason provided";
                        String reason = args.length > 2 ? String.join(" ", args).substring(args[0].length() + 1 + args[1].length() + 1).trim() : "No reason provided";

                        // Ban the user with a deletion timeframe of 0 days
                        event.getGuild().ban(target, 0, TimeUnit.DAYS)
                                .reason(reason)
                                .queue(
                                        success -> event.getChannel().sendMessage("Banned " + target.getEffectiveName() + " for: " + reason).queue(),
                                        error -> event.getChannel().sendMessage("Failed to ban the user. Error: " + error.getMessage()).queue()
                                );
                    } else {
                        event.getChannel().sendMessage("Could not find the user to ban.").queue();
                    }
                } else {
                    event.getChannel().sendMessage("Please mention a valid user to ban.").queue();
                }
            } else {
                // Send a message if the user is not authorized
                event.getChannel().sendMessage("You are not authorized to use this command.").queue();
            }
        }
    }
}
