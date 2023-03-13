package fr.red_spash.bedwars.listeners;

import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;
import fr.red_spash.bedwars.Models.Base;
import fr.red_spash.bedwars.Models.PlayerData;
import fr.red_spash.bedwars.utils.GameState;
import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

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

    @EventHandler(priority = EventPriority.LOW)
    public void BlockPlace(BlockPlaceEvent e){
        Block block = e.getBlock();
        Player p = e.getPlayer();
        if(BedWarsGame.gameStat != GameState.Started && !p.isOp()){
            e.setCancelled(true);
            return;
        }
        if(BedWarsGame.locationNotPlaceable.contains(block.getLocation())){
            e.setCancelled(true);
            p.sendMessage("§cImpossible de placer une block ici !");
            return;
        }
        if(!p.isOp() || p.getGameMode() == GameMode.SURVIVAL){
            BedWarsGame.blockBreakable.add(block.getLocation());
        }
    }

    @EventHandler
    public void PlayerBreakBlock(BlockBreakEvent e){
        Player p = e.getPlayer();
        if(BedWarsGame.gameStat != GameState.Started && !p.isOp()){
            e.setCancelled(true);
            return;
        }
        if(BedWarsGame.inRespawn.containsKey(p.getUniqueId()) || BedWarsGame.playerSpectator.contains(p.getUniqueId())){
            if(p.getGameMode() != GameMode.CREATIVE){
                e.setCancelled(true);
                return;
            }
        }
        Block block = e.getBlock();
        Base base = null;

        if(block.getType() == Material.BED_BLOCK){
            for(Base b : BedWarsGame.bases){
                if(b.isMyBed(block)){
                    base = b;
                }
            }
            if(base != null){
                if(base.getPlayersUUID().contains(p.getUniqueId())){
                    e.setCancelled(true);
                    p.sendMessage("§cTu ne peux pas casser ton lit !");
                    return;
                }
                block.getWorld().strikeLightning(block.getLocation().add(0.5,0,0.5));
                PlayerData playerData = BedWarsGame.playersDatas.get(p.getUniqueId());
                Bukkit.broadcastMessage("§c\n§c§lDESTRUCTION DE LIT > §7Le "+ Utils.getChatColorOf(base.getColor())+"lit de l'équipe "+base.getTeamName()+" §7vient d'être détruit par "+playerData.getNameWithColor()+"!\n§f§o  §f  \n§f");
                base.setAsBed(false);
                for(Player pl : Bukkit.getOnlinePlayers()){
                    pl.playSound(pl.getLocation(), Sound.ENDERDRAGON_GROWL,1,1);
                }
                BedWarsGame.checkIsEnd();
                return;
            }

        }
        if(!BedWarsGame.blockBreakable.contains(e.getBlock().getLocation())){
            if(!p.isOp() ||p.getGameMode() == GameMode.SURVIVAL){
                e.setCancelled(true);
                p.sendMessage("§cImpossible de casser le block !");
            }
        }
    }

}
