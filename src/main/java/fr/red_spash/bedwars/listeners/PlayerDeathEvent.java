package fr.red_spash.bedwars.listeners;

import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;
import fr.red_spash.bedwars.Main;
import fr.red_spash.bedwars.Models.Base;
import fr.red_spash.bedwars.Models.DamageCaused;
import fr.red_spash.bedwars.Models.PlayerData;
import fr.red_spash.bedwars.utils.GameState;
import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.*;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
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
            Player p = e.getPlayer();
            if(BedWarsGame.inRespawn.containsKey(p.getUniqueId()) || BedWarsGame.playerSpectator.contains(p.getUniqueId())){
                if(p.getGameMode() != GameMode.CREATIVE){
                    return;
                }
            }
            if(BedWarsGame.gameStat == GameState.Started){
                if(e.getFrom().getY()<=0){
                    if(p.getWorld().getName().equalsIgnoreCase("bedwars")){
                        DamageCaused damageCaused = new DamageCaused();
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
    }

    @EventHandler
    public void DamageEvent(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            if(e.getCause() == EntityDamageEvent.DamageCause.LIGHTNING){
                e.setCancelled(true);
                return;
            }
            if(BedWarsGame.gameStat != GameState.Started){
                e.setCancelled(true);
                return;
            }
            Player p = (Player) e.getEntity();
            if(BedWarsGame.inRespawn.containsKey(p.getUniqueId()) || BedWarsGame.playerSpectator.contains(p.getUniqueId())){
                if(p.getGameMode() != GameMode.CREATIVE){
                    e.setCancelled(true);
                    return;
                }
            }
            if(p.getHealth() <= e.getFinalDamage()){
                e.setCancelled(true);
                DamageCaused damageCaused = new DamageCaused();
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
                    case ENTITY_EXPLOSION:
                    case BLOCK_EXPLOSION:
                        if(damageCaused.isValid()){
                            if(damageCaused.getEntity() == EntityType.FIREBALL){
                                message = "{DEAD} est mort d'une fireball de {DAMAGER} !";
                            }else{
                                message = "{DEAD} est mort d'une explosion causée par {DAMAGER} !";
                            }

                        }else{
                            message = "{DEAD} est mort d'une explosion !";
                        }
                        break;
                    default:
                        if(damageCaused.isValid()){
                            message = "{DAMAGER} vient de tuer {DEAD} !";
                        }else{
                            message = "{DEAD} est mort !";
                        }
                }

                if(damageCaused.isValid()){
                    playerDead(p,message,Bukkit.getPlayer(damageCaused.getDamager()));
                }else{
                    playerDead(p,message,null);
                }
            }
        } else if (e.getEntity() instanceof Villager) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerDamagePlayer(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player){
            if(e.getDamager() instanceof Player){
                Player p = (Player) e.getEntity();
                Player damager = (Player) e.getDamager();
                BedWarsGame.lastDamageCaused.put(p.getUniqueId(),new DamageCaused(p.getUniqueId(),damager.getUniqueId()));
            }if(e.getDamager() instanceof Fireball){
                Player p = (Player) e.getEntity();
                Fireball damager = (Fireball) e.getDamager();
                if(damager.getShooter() instanceof Player){
                    BedWarsGame.lastDamageCaused.put(p.getUniqueId(),new DamageCaused(p.getUniqueId(),((Player) damager.getShooter()).getUniqueId()).setEntity(EntityType.FIREBALL));
                }

            }
        }
    }

    public static void playerDead(Player p, String deathMessage,@Nullable Player Killer) {
        if(BedWarsGame.inRespawn.containsKey(p.getUniqueId())){
            BedWarsGame.inRespawn.get(p.getUniqueId()).cancel();
        }
        Base base = BedWarsGame.playersDatas.get(p.getUniqueId()).getBase();
        String finalKill = "§b§lFINAL KILL";
        if(base.asBed()){
            finalKill = "";
        }
        p.getLocation().getWorld().playSound(p.getLocation(), Sound.HURT_FLESH,1,1);
        deathMessage = deathMessage.replace("{DEAD}", Utils.getChatColorOf(base.getColor())+p.getName()+"§e");
        if(Killer != null){
            if(!p.getName().equalsIgnoreCase(Killer.getName())){
                PlayerData killerData = BedWarsGame.playersDatas.get(Killer.getUniqueId());
                Base basedamager = killerData.getBase();
                if(!finalKill.equalsIgnoreCase("")){
                    killerData.addFinalKill();
                }else{
                    killerData.addKill();
                }
                deathMessage = deathMessage.replace("{DAMAGER}",Utils.getChatColorOf(basedamager.getColor())+Killer.getName()+"§e");
                Player damager = Bukkit.getPlayer(Killer.getUniqueId());
                if(damager.isOnline()){
                    damager.playSound(damager.getLocation(), Sound.ORB_PICKUP,1,2);
                }
            }

        }
        Bukkit.broadcastMessage(deathMessage+" "+finalKill);

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
        if(!base.asBed()){
            BedWarsGame.playersDatas.get(p.getUniqueId()).setDead(true);
            Utils.sendTitle(p,"§c§lVous n'avez plus de lit !","§cVous ne pouvez plus réapparaitre",0,20*5,20);
            if(!BedWarsGame.checkIsEnd()){
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,5*20,10,false,false));
                p.playSound(p.getLocation(), Sound.WITHER_SPAWN,1,0);
                BedWarsGame.addSpectator(p.getUniqueId());
            }
            return;
        }
        p.teleport(BedWarsGame.spawn);
        p.setAllowFlight(true);
        for(Player pl : BedWarsGame.world.getPlayers()){
            if(pl.getUniqueId() != p.getUniqueId()){
                pl.hidePlayer(p);
            }
        }
        p.setFireTicks(0);
        p.setFlying(true);
        p.getInventory().clear();
        Utils.clearArmor(p);
        p.setHealth(20.0);
        for(PotionEffect effect : p.getActivePotionEffects()){
            p.removePotionEffect(effect.getType());
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,1000*20,1,false,false),true);

        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            int i = base.getRespawnTime();
            @Override
            public void run() {
                if(i == 0){
                    p.setFireTicks(0);
                    BedWarsGame.inRespawn.get(p.getUniqueId()).cancel();
                    BedWarsGame.inRespawn.remove(p.getUniqueId());
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
                    BedWarsGame.playersDatas.get(p.getUniqueId()).downdradeTools();
                    if(p.isOnline()){
                        BedWarsGame.playersDatas.get(p.getUniqueId()).setDefaultInventory();
                    }
                    return;
                }
                Utils.sendTitle(p,"§c§lVous êtes mort !","§eRespawn dans "+i+" secondes",0,22,0);
                p.playSound(p.getLocation(), Sound.CLICK,1,1);
                i = i -1;
            }
        },0,20);
        BedWarsGame.inRespawn.put(p.getUniqueId(),bukkitTask);
    }

}
