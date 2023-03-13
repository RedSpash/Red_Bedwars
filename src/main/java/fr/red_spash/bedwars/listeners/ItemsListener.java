package fr.red_spash.bedwars.listeners;

import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;
import fr.red_spash.bedwars.Main;
import fr.red_spash.bedwars.Models.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class ItemsListener implements Listener {

    public static double FIREBALL_POWER = 1.25;

    @EventHandler
    public void PlayerDamageEvent(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if(e.getDamager() instanceof TNTPrimed || e.getDamager() instanceof Fireball){
                e.setDamage(e.getFinalDamage()/3.0);
                Vector vector = p.getLocation().subtract(e.getDamager().getLocation().add(0,-0.5,0)).toVector();
                vector = vector.normalize();
                p.setVelocity(vector.multiply(FIREBALL_POWER));
                Bukkit.broadcastMessage(vector+"");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void TNTPlaceEvent(BlockPlaceEvent e){
        Block block = e.getBlock();
        if(block.getType() == Material.TNT){
            e.setCancelled(true);
            Player p = e.getPlayer();
            p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
            TNTPrimed tntPrimed = (TNTPrimed) block.getWorld().spawnEntity(block.getLocation(), EntityType.PRIMED_TNT);
            tntPrimed.setFuseTicks(20*7);
            block.setType(Material.AIR);
        }
    }

    @EventHandler
    public void ProjectileHitEvent(ProjectileHitEvent e){
        if(e.getEntity() instanceof Snowball){
            Snowball snowball = (Snowball) e.getEntity();
            if(snowball.getShooter() instanceof Player){
                Player p = (Player) snowball.getShooter();
                PlayerData playerData = BedWarsGame.playersDatas.get(p.getUniqueId());
                if(playerData.getBase() != null){
                    Silverfish silverfish = (Silverfish) snowball.getWorld().spawnEntity(snowball.getLocation(), EntityType.SILVERFISH);
                    silverfish.setCustomName("§fSnowFish de l'équipe "+playerData.getBase().getTeamName());
                }
            }
        }
    }

    @EventHandler
    public void EntityTarget(EntityTargetEvent e){
        if(e.getEntity() instanceof Silverfish){
            if(e.getTarget() instanceof Player){
                Player p = (Player) e.getTarget();
                Silverfish silverfish = (Silverfish) e.getEntity();
                PlayerData playerData = BedWarsGame.playersDatas.get(p.getUniqueId());

                if(playerData.getBase() != null){
                    if(silverfish.getCustomName() != null){
                        if(silverfish.getCustomName().contains(playerData.getBase().getTeamName())){
                            e.setCancelled(true);
                        }
                    }
                }else{
                    e.setCancelled(true);
                }

            }
        }
    }

    @EventHandler
    public void Interact(PlayerInteractEvent e) {
        if (e.getItem() != null) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
                Player p = e.getPlayer();
                if (e.getItem().getType() == Material.FIREBALL) {
                    e.setCancelled(true);
                    p.getInventory().getItemInHand().setAmount(e.getItem().getAmount()-1);
                    Fireball fireball = p.launchProjectile(Fireball.class);
                    fireball.setVelocity(fireball.getVelocity().multiply(2.5));
                    fireball.setBounce(true);
                    fireball.setYield(2);
                    fireball.setIsIncendiary(false);
                } else if (e.getItem().getType().isBlock()) {
                    Block block = e.getClickedBlock();
                    if(block != null){
                        if(block.getType() == Material.BED_BLOCK){
                            if(!p.isSneaking()){
                                p.setSneaking(true);
                                Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                                    @Override
                                    public void run() {
                                        p.setSneaking(false);
                                    }
                                },1);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

}
