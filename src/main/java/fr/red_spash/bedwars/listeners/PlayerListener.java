package fr.red_spash.bedwars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void FoodLost(FoodLevelChangeEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if(e.getFoodLevel() < p.getFoodLevel()) {
                e.setCancelled(true);
                e.setFoodLevel(20);
            }



        }


    }

}
