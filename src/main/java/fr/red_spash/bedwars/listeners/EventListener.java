package fr.red_spash.bedwars.listeners;

import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;
import fr.red_spash.bedwars.Main;
import fr.red_spash.bedwars.Models.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {


    @EventHandler
    public void ItemStack(ItemMergeEvent e){
        Item item = e.getEntity();
        Item target = e.getTarget();
        if(Main.cooldownItemStackable.containsKey(item)){
            if(Main.cooldownItemStackable.get(item) > System.currentTimeMillis()){
                e.setCancelled(true);
                return;
            }
        } else if (Main.cooldownItemStackable.containsKey(target)) {
            if(Main.cooldownItemStackable.get(target) > System.currentTimeMillis() ){
                e.setCancelled(true);
                return;
            }
        }

        if(BedWarsGame.itemSpawned.containsKey(item.getUniqueId()) || BedWarsGame.itemSpawned.containsKey(target.getUniqueId())){
            if(BedWarsGame.itemSpawned.get(item.getUniqueId()) == BedWarsGame.itemSpawned.get(target.getUniqueId())){
                BedWarsGame.itemSpawned.remove(item.getUniqueId());
            }else{
                e.setCancelled(true);
                return;
            }
        }
    }
}
