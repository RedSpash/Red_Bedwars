package fr.red_spash.bedwars.listeners;

import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

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

    @EventHandler
    public void PlayerDrop(PlayerDropItemEvent e){
        Player p = e.getPlayer();

        if(BedWarsGame.inRespawn.containsKey(p.getUniqueId()) || BedWarsGame.playerSpectator.contains(p.getUniqueId())){
            if(p.getGameMode() != GameMode.CREATIVE){
                e.setCancelled(true);
                return;
            }
        }
    }
}
