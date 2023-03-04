package fr.red_spash.bedwars.Models;

import fr.red_spash.bedwars.Main;
import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;
import fr.red_spash.bedwars.utils.GameState;
import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class Forge extends Generator{
    private int tier;
    private Double speed;
    private Location location;
    private double nextItem;
    private int limitItem;
    private BukkitTask bukkitTask;
    private int nextGold = 5;
    private int GOLDEACH = 5;

    public Forge(Location location, Double speed, int limitItem){
        this.speed = speed;
        this.nextItem = speed;
        this.location = location;
        this.tier = 1;
        this.limitItem = limitItem;
        BedWarsGame.Forges.add(this);
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public void removeTimeNextItem(double time){
        if(BedWarsGame.gameStat == GameState.Started){
            this.nextItem = this.nextItem - time;
            if((int) this.nextItem <= 0 ){
                this.nextItem = this.speed+0.99;
                this.spawnItem();
            }
        }
    }

    public void startSpawn(){
        Forge forge = this;
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                forge.removeTimeNextItem(0.05);
            }
        },0,1);
    }

    private void spawnItem() {
        int i = 0;
        //for(Entity entity : this.location.getWorld().getNearbyEntities(this.location,1.5,1,1.5)){
        //    if(entity instanceof Item){
        //        Item item = (Item) entity;
        //        i = i + item.getItemStack().getAmount();
        //        if(i >= this.limitItem){
        //            return;
        //        }
        //    }
        //}
        if(super.getItemSpawned() >= this.limitItem){
            return;
        }
        Location loca = this.location;
        nextGold = nextGold -1;
        if(nextGold <= 0){
            super.addSpawnedItem(1);
            Item gold = loca.getWorld().dropItemNaturally(loca,new ItemStack(Material.GOLD_INGOT));
            Main.cooldownItemStackable.put(gold,System.currentTimeMillis()+1000*4);
            BedWarsGame.itemSpawned.put(gold.getUniqueId(),this);
            this.nextGold = GOLDEACH;
        }
        if(super.getItemSpawned() >= this.limitItem){
            return;
        }
        super.addSpawnedItem(1);
        Item iron = loca.getWorld().dropItemNaturally(loca,new ItemStack(Material.IRON_INGOT));
        Main.cooldownItemStackable.put(iron,System.currentTimeMillis()+1000*4);
        BedWarsGame.itemSpawned.put(iron.getUniqueId(),this);


    }
}
