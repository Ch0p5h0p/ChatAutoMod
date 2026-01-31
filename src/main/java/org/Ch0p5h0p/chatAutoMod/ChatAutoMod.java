package org.Ch0p5h0p.chatAutoMod;

import org.bukkit.plugin.java.JavaPlugin;
import org.Ch0p5h0p.chatAutoMod.processor.Config;
import org.Ch0p5h0p.chatAutoMod.processor.Processor;

import java.io.IOException;
import java.util.logging.Logger;

public final class ChatAutoMod extends JavaPlugin {

    @Override
    public void onEnable() {
        // Initialize Processor
        try {
            Processor.init(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Register listener
        getServer().getPluginManager().registerEvents(new ChatHandler(this), this);
        getLogger().info("Enabled chat hooks");

        getServer().getPluginManager().registerEvents(new SignHandler(this), this);
        getLogger().info("Enabled sign reader");

        // Register commands
        this.getCommand("automodstring").setExecutor((sender, command, label, args) -> {
            sender.sendMessage("Current master string: "+Processor.getMasterString());
            return true;
        });

        this.getCommand("automodreload").setExecutor((sender, command, label, args) -> {
            sender.sendMessage("Reloading data...");
            try {
                Processor.init(this);
                sender.sendMessage("Data reloaded.");
            } catch (IOException e) {
                RuntimeException ex = new RuntimeException(e);
                sender.sendMessage(ex.toString());
                throw ex;
            }
            return true;
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
