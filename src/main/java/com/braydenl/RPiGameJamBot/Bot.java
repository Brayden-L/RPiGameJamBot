package com.braydenl.RPiGameJamBot;

import com.moandjiezana.toml.Toml;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.apache.commons.collections4.multiset.SynchronizedMultiSet;
import org.jetbrains.annotations.NotNull;
import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Objects;

public class Bot extends ListenerAdapter {

    static String TOKEN;
    public static JDA jda;
    MessageChannel channel;

    public static void main(String[] args) throws LoginException, InterruptedException {

        TOKEN = args[0];

        // Start Discord bot.
        System.out.println("Starting bot.");
        jda = JDABuilder.createLight(TOKEN, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new Bot())
                .setActivity(Activity.watching("you."))
                .build();

        Thread.sleep(5000); // This deals with a race condition somehow.

        createAndUpsertCommand(Objects.requireNonNull(jda.getGuildById("204621105720328193")));
    }

    private static void createAndUpsertCommand(@NotNull Guild guild) {
        System.out.println("Setting up commands in " + guild.getName());
        CommandListUpdateAction commands = guild.updateCommands();

        commands.addCommands(
                Commands.slash("deadline", "Gives hour, minutes, and seconds until the submission deadline.")
        ).queue();

        guild.upsertCommand("deadline", "Gives hour, minutes, and seconds until the submission deadline.").queue();

        System.out.println("Done setting up commands in " + guild.getName()+ ".");
    }

    void deadline() {
        channel.sendMessage("I hate working with time. It's just Friday 12pm EST").queue();
    }


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;    // Prevent extraneous operation.
        channel = event.getChannel();
        String command     = event.getName();
        if ("deadline".equals(command)) {
            deadline();
        } else {
            event.reply("Invalid command for RPiGameJamBot.").queue();
        }
    }

}
