package org.Ch0p5h0p.chatAutoMod;

//import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.Ch0p5h0p.chatAutoMod.processor.Processor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class BookHandler implements Listener {

    public JavaPlugin plugin;

    public BookHandler(JavaPlugin plugin) {
        this.plugin = plugin;
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

}
