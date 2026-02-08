package org.Ch0p5h0p.chatAutoMod;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.Ch0p5h0p.chatAutoMod.processor.Processor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.view.AnvilView;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class EventHandlers implements Listener {

    public JavaPlugin plugin;

    public EventHandlers(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncChatEvent e) {
        Player p = e.getPlayer();
        Component message = e.originalMessage();
        String plain = PlainTextComponentSerializer.plainText().serialize(message);
        String response = Processor.process(plain).replace("%s",p.getName());
        //p.sendMessage(Component.text("Response: "+response));
        if (!response.equals("None")) {
            if (!response.equals("CENSOR")) {
                Bukkit.getScheduler().runTask(plugin, () ->
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), response)
                );
                e.setCancelled(true);
            } else {
                e.message(Component.text(Processor.censor(plain)));
            }
        }
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

    @EventHandler
    public void onWrite(PlayerEditBookEvent e) {
        Player p = e.getPlayer();
        StringBuilder bookText= new StringBuilder();
        BookMeta book = e.getNewBookMeta();
        for (int i = 1; i<book.getPageCount()+1; i++) {
            bookText.append(PlainTextComponentSerializer.plainText().serialize(book.page(i))).append(" ");
        }
        //p.sendMessage(Component.text("Book text: "+bookText));

        String response = Processor.process(bookText.toString()).replace("%s", p.getName());
        //p.sendMessage(Component.text("Response: "+response));
        if (!response.equals("None")) {
            Bukkit.getScheduler().runTask(plugin, () ->
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), response)
            );
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onAnvilRename(PrepareAnvilEvent e) {
        ItemStack result = e.getResult();
        if (result == null) return;

        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            String censored = Processor.censor(PlainTextComponentSerializer.plainText().serialize(meta.displayName()));
            meta.displayName(Component.text(censored));
            result.setItemMeta(meta);
        }
    }
}
