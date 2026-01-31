package org.originsrebirth.chatAutoMod;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.originsrebirth.chatAutoMod.processor.Processor;

public class ChatHandler implements Listener {
    public JavaPlugin plugin;

    public ChatHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncChatEvent e) {
        Player p = e.getPlayer();
        Component message = e.originalMessage();
        String plain = PlainTextComponentSerializer.plainText().serialize(message);
        String response = Processor.process(plain).replace("%s",p.getName());
        p.sendMessage(Component.text("Response: "+response));
        if (!response.equals("None")) {
            Bukkit.getScheduler().runTask(plugin, () ->
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), response)
            );

        }
    }
}
