package fr.red_spash.bedwars.listeners;

import fr.red_spash.bedwars.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {

    @EventHandler
    public void d(PlayerJoinEvent e){
        Bukkit.broadcastMessage(e.getPlayer().getUniqueId().toString());
    }

    @EventHandler
    public void ItemStack(ItemMergeEvent e){
        Item item = e.getEntity();
        if(Main.cooldownItemStackable.containsKey(item)){
            if(Main.cooldownItemStackable.get(item) > System.currentTimeMillis()){
                e.setCancelled(true);
            }
        }
    }

}
