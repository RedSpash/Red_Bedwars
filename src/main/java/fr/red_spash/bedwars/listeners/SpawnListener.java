package fr.red_spash.bedwars.listeners;

import fr.red_spash.bedwars.Main;
import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;
import fr.red_spash.bedwars.Scoreboard.ScoreboardManager;
import fr.red_spash.bedwars.utils.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class SpawnListener implements Listener {

    @EventHandler
    public void EntityInteract(PlayerInteractEntityEvent e){
        Player p = e.getPlayer();
        Entity entity = e.getRightClicked();
        if(entity instanceof Villager){
            Villager villager = (Villager) entity;
            if(villager.getCustomName() != null){
                if(villager.getCustomName().equalsIgnoreCase(Main.START_GAME_NAME)){
                    e.setCancelled(true);
                    if(p.isOp()){
                        BedWarsGame.startGame();
                    }else{
                        p.sendMessage("§cVous n'avez pas les permissions pour lancer une partie !");
                    }
                }
            }
        }
    }

    @EventHandler
    public void Join(PlayerJoinEvent e){
        if(Bukkit.getWorld("bedwars") != null && BedWarsGame.gameStat == GameState.Waiting){
            e.getPlayer().teleport(BedWarsGame.spawn);
            BedWarsGame.checkCanStart();
            ScoreboardManager.setScoreboard(e.getPlayer());
        }
    }

}
