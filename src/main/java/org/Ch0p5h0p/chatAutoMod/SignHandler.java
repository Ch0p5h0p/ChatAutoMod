package org.Ch0p5h0p.chatAutoMod;

//import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.Ch0p5h0p.chatAutoMod.processor.Processor;

import java.util.Objects;

public class SignHandler implements Listener {
    public JavaPlugin plugin;

    public SignHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Player p = e.getPlayer();
        StringBuilder signText = new StringBuilder();

        for (int i=0; i<4; i++) {
            String line = Objects.requireNonNull(e.line(i)).toString();
            signText.append(line).append(" ");
        }

        String response = Processor.process(signText.toString()).replace("%s", p.getName());
        //p.sendMessage(Component.text("Response: "+response));
        if (!response.equals("None")) {
            Bukkit.getScheduler().runTask(plugin, () ->
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), response)
            );
            e.setCancelled(true);
        }

    }
}
