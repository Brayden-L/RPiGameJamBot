package com.braydenl.RPiGameJamBot;

import com.moandjiezana.toml.Toml;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Launcher {
    final String TOKEN = new Toml().read("config.toml").getString("TOKEN");

    public Launcher() throws LoginException {
        // Start Discord bot.
        System.out.println("Starting bot.");
        JDA jda = JDABuilder.createLight(TOKEN, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new Launcher())
                .setActivity(Activity.watching("you."))
                .build();
    }
}
