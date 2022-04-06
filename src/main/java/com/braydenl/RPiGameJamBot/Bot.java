package com.braydenl.RPiGameJamBot;

import com.moandjiezana.toml.Toml;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.apache.commons.collections4.multiset.SynchronizedMultiSet;
import org.jetbrains.annotations.NotNull;
import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Bot extends ListenerAdapter {

    static String TOKEN;
    public static JDA jda;
    MessageChannel channel;
    SlashCommandInteractionEvent event;

    public static void main(String[] args) throws LoginException, InterruptedException {

        TOKEN = args[0];

        // Start Discord bot.
        System.out.println("Starting bot.");
        jda = JDABuilder.createLight(TOKEN, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new Bot())
                .setActivity(Activity.watching("you."))
                .build();

        jda.awaitReady();   // Much cleaner way of dealing with race condition.

        createAndUpsertCommand(Objects.requireNonNull(jda.getGuildById("204621105720328193")));
    }

    private static void createAndUpsertCommand(@NotNull Guild guild) {
        System.out.println("Setting up commands in " + guild.getName());
        CommandListUpdateAction commands = guild.updateCommands();

        commands.addCommands(
                Commands.slash("deadline", "Gives hour, minutes, and seconds until the submission deadline.")
        ).complete();

        commands.addCommands(
                Commands.slash("startjam", "Start a game jam!")
                        .addOptions(new OptionData(OptionType.INTEGER, "hours", "how many hours will it be?").setRequired(true))
                        .addOptions(new OptionData(OptionType.STRING, "name", "what's the jam name?").setRequired(true))
        ).complete();

        guild.upsertCommand("deadline", "Gives hour, minutes, and seconds until the submission deadline.").complete();
        guild.upsertCommand("startjam", "starts a jam").complete();

        System.out.println("Done setting up commands in " + guild.getName()+ ".");
    }

    void deadline() {
        channel.sendMessage("Figure it out yourself.").queue();
    }

    void startJam() {
        new Jam(
                Integer.parseInt(String.valueOf(event.getOption("hours"))),
                channel,
                String.valueOf(event.getOption("name"))
        );
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;    // Prevent extraneous operation.
        this.event = event;
        channel = event.getChannel();
        String command     = event.getName();
        switch (command) {
            case "deadline": deadline(); break;
            case "startJam": startJam(); break;
            default: channel.sendMessage("Not a valid command.").queue(); break;
        }
    }

}
