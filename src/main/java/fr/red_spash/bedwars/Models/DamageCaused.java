package fr.red_spash.bedwars.Models;

import org.bukkit.entity.Player;

import java.util.UUID;

public class DamageCaused {
    private UUID damager;
    private Long time;

    public DamageCaused(UUID damager){
        this(damager,System.currentTimeMillis()+1000*15);
    }

    public DamageCaused(Player p){
        this(p.getUniqueId());
    }

    public DamageCaused(UUID damager, long time) {
        this.damager = damager;
        this.time = time;
    }

    public boolean isValid(){
        return this.time>= System.currentTimeMillis();
    }

    public UUID getDamager() {
        return damager;
    }
}
