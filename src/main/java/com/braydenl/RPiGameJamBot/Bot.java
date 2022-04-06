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
import org.jetbrains.annotations.NotNull;
import javax.security.auth.login.LoginException;
import java.util.List;

public class Bot extends ListenerAdapter {

    static final String TOKEN = new Toml().read("config.toml").getString("TOKEN");
    public static JDA jda;
    MessageChannel channel;

    public static void main(String[] args) throws LoginException {
        // Start Discord bot.
        System.out.println("Starting bot.");
        jda = JDABuilder.createLight(TOKEN, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new Bot())
                .setActivity(Activity.watching("you."))
                .build();

        List<Guild> guilds = jda.getGuilds();
        for (Guild guild : guilds) new Thread(() -> createAndUpsertCommand(guild)).start();
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
