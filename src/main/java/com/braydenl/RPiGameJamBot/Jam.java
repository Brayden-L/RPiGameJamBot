package com.braydenl.RPiGameJamBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class Jam extends Thread {
    int hours;
    MessageChannel channel;
    String name;

    public Jam(int hours, MessageChannel channel, String name) {
        this.hours = hours;
        this.channel = channel;
        this.name = name;
    }

    @Override
    public void start() {
        channel.sendMessage("Started " + name + " for " + hours + " hours.").queue();
        try {
            TimeUnit.HOURS.sleep(hours);
        } catch (InterruptedException e) {
            e.printStackTrace();
            channel.sendMessage("Command failed ").queue();
        }
    }
}
