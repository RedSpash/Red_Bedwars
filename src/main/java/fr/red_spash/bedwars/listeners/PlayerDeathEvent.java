package fr.red_spash.bedwars.listeners;

import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;
import fr.red_spash.bedwars.Main;
import fr.red_spash.bedwars.Models.Base;
import fr.red_spash.bedwars.Models.DamageCaused;
import fr.red_spash.bedwars.utils.GameState;
import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PlayerDeathEvent implements Listener {

    private static final ArrayList<Material> LOOTABLE_MATERIALS = new ArrayList<>(Arrays.asList(Material.DIAMOND,Material.IRON_INGOT,Material.GOLD_INGOT,Material.EMERALD,Material.FIREBALL,Material.TNT));


    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        if(e.getTo().getBlock() != e.getFrom().getBlock()){
            Location from = e.getFrom();
            Location to = e.getTo();
            Player p = e.getPlayer();

            if(e.getFrom().getY()<=0){
                if(p.getWorld().getName().equalsIgnoreCase("bedwars")){
                    DamageCaused damageCaused = new DamageCaused(null,-1);
                    if(BedWarsGame.lastDamageCaused.containsKey(p.getUniqueId())){
                        damageCaused = BedWarsGame.lastDamageCaused.get(p.getUniqueId());
                    }
                    if(damageCaused.isValid()){
                        playerDead(p,"{DEAD} vient d'être poussé dans le vide par {DAMAGER}", Bukkit.getPlayer(damageCaused.getDamager()));
                    }else{
                        playerDead(p,"{DEAD} est tombé dans le vide !",null);
                    }

                }
            }
        }
    }

    @EventHandler
    public void DamageEvent(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            if(BedWarsGame.gameStat != GameState.Started){
                e.setCancelled(true);
                return;
            }
            Player p = (Player) e.getEntity();
            if(p.getHealth() <= e.getFinalDamage()){
                e.setCancelled(true);
                DamageCaused damageCaused = new DamageCaused(null,-1);
                if(BedWarsGame.lastDamageCaused.containsKey(p.getUniqueId())){
                    damageCaused = BedWarsGame.lastDamageCaused.get(p.getUniqueId());
                }
                String message = "";
                switch (e.getCause()){
                    case FALL:
                        if(damageCaused.isValid()){
                            message = "{DAMAGER} vient de pousser {DEAD} d'une falaise !";
                        }else{
                            message = "{DEAD} est tombé de haut !";
                        }
                        break;
                    case PROJECTILE:
                        if(damageCaused.isValid()){
                            message = "{DEAD} s'est pris un projectile de {DAMAGER} !";
                        }else{
                            message = "{DEAD} s'est pris un projectile ! !";
                        }
                        break;
                    case FIRE_TICK:
                        if(damageCaused.isValid()){
                            message = "{DAMAGER} à fait cuire {DEAD} jusqu'à la mort !";
                        }else{
                            message = "{DEAD} a brulé à mort !";
                        }
                        break;
                    case ENTITY_ATTACK:
                        if(damageCaused.isValid()){
                            message = "{DAMAGER} vient de tuer {DEAD} !";
                        }else{
                            message = "{DEAD} est mort !";
                        }
                        break;
                    case ENTITY_EXPLOSION:
                    case BLOCK_EXPLOSION:
                        if(damageCaused.isValid()){
                            message = "{DEAD} est mort d'une explosion causée par {DAMAGER} !";
                        }else{
                            message = "{DEAD} est mort d'une explosion !";
                        }
                        break;
                }

                if(damageCaused.isValid()){
                    playerDead(p,message,Bukkit.getPlayer(damageCaused.getDamager()));
                }else{
                    playerDead(p,message,null);
                }
            }
        }
    }

    @EventHandler
    public void PlayerDamagePlayer(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player){
            if(e.getDamager() instanceof Player){
                Player p = (Player) e.getEntity();
                Player damager = (Player) e.getDamager();
                BedWarsGame.lastDamageCaused.put(p.getUniqueId(),new DamageCaused(damager.getUniqueId()));
            }
        }
    }

    private void playerDead(Player p, String deathMessage,@Nullable Player Killer) {
        Base base = BedWarsGame.playerBase.get(p.getUniqueId());
        p.getLocation().getWorld().playSound(p.getLocation(), Sound.HURT_FLESH,1,1);
        deathMessage = deathMessage.replace("{DEAD}", Utils.getChatColorOf(base.getColor())+p.getName()+"§e");
        if(Killer != null){
            Base basedamager = BedWarsGame.playerBase.get(Killer.getUniqueId());
            deathMessage = deathMessage.replace("{DAMAGER}",Utils.getChatColorOf(basedamager.getColor())+Killer.getName()+"§e");
        }
        Bukkit.broadcastMessage(deathMessage);

        if(Killer != null){
            HashMap<ItemStack,Integer> Amount = new HashMap<>();
            for(ItemStack itemStack : p.getInventory()){
                if(itemStack != null){
                    if(LOOTABLE_MATERIALS.contains(itemStack.getType())){
                        if(Amount.containsKey(itemStack)){
                            Amount.put(itemStack,Amount.get(itemStack)+itemStack.getAmount());
                        }else{
                            Amount.put(itemStack,itemStack.getAmount());
                        }
                    }
                }
            }
            for(ItemStack itemStack : Amount.keySet()){
                int amount = Amount.get(itemStack);
                itemStack.setAmount(amount);
                Killer.getInventory().addItem(itemStack);
                Killer.sendMessage("§7"+amount+"x "+Utils.upperCaseFirst(itemStack.getType().toString().toLowerCase().replace("_"," ")));
            }

        }
        BedWarsGame.lastDamageCaused.remove(p.getUniqueId());
        p.teleport(BedWarsGame.spawn);
        p.setAllowFlight(true);
        for(Player pl : BedWarsGame.world.getPlayers()){
            if(pl.getUniqueId() != p.getUniqueId()){
                pl.hidePlayer(p);
            }
        }
        p.setFireTicks(0);
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,1000*20,1,false,false),true);
        p.setFlying(true);
        p.getInventory().clear();
        p.setHealth(20.0);
        for(PotionEffect effect : p.getActivePotionEffects()){
            p.removePotionEffect(effect.getType());
        }
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            int i = base.getRespawnTime();
            @Override
            public void run() {
                if(i == 0){
                    p.setFireTicks(0);
                    BedWarsGame.inRespawn.get(p.getUniqueId()).cancel();
                    p.setFallDistance(0);
                    p.teleport(base.getSpawnLocation());
                    for(Player pl : BedWarsGame.world.getPlayers()){
                        if(pl.getUniqueId() != p.getUniqueId()){
                            pl.showPlayer(p);
                        }
                    }
                    p.setAllowFlight(false);
                    p.setFlying(false);
                    p.removePotionEffect(PotionEffectType.INVISIBILITY);
                    base.setDefaultInventory(p);
                    return;
                }
                Utils.sendTitle(p,"§c§lVous êtes mort !","§eRespawn dans "+i+" secondes",0,21,0);
                i = i -1;
            }
        },0,20);
        BedWarsGame.inRespawn.put(p.getUniqueId(),bukkitTask);
    }

}
