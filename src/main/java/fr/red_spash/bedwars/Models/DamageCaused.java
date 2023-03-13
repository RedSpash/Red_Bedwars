package fr.red_spash.bedwars.Models;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DamageCaused {
    private UUID damager;
    private UUID player;
    private Long time;
    private EntityType entity;

    public DamageCaused(UUID player, UUID damager){
        this(player,damager,System.currentTimeMillis()+1000*15);
    }

    public DamageCaused(Player p, Player Damager
    ){
        this(p.getUniqueId(), Damager.getUniqueId());
    }

    public DamageCaused(UUID p,UUID damager, long time) {
        this.player = p;
        this.damager = damager;
        this.time = time;
    }

    public DamageCaused() {
        this(null,null,-1);
    }

    public DamageCaused setEntity(EntityType entity) {
        this.entity = entity;
        return this;
    }

    public EntityType getEntity() {
        return entity;
    }

    public boolean isValid(){
        return this.time>= System.currentTimeMillis() && damager != player;
    }

    public UUID getDamager() {
        return damager;
    }
}
