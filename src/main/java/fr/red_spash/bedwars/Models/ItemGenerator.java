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

public class ItemGenerator extends Generator {
    private int tier;
    private String name;
    private ItemStack itemStack;
    private Material displayItem;
    private Double speed;
    private Location location;
    private ArmorStand armorStandTime;
    private ArmorStand armorStandType;
    private ArmorStand armorStandTier;
    private double nextItem;
    private int limitItem;
    private BukkitTask bukkitTask;

    public ItemGenerator( ItemStack itemStack, Material displayItem, Location location, Double speed, int limitItem, String name){
        this.displayItem = displayItem;
        this.name = name;
        this.itemStack = itemStack;
        this.speed = speed;
        this.nextItem = speed;
        this.location = location;
        this.tier = 1;
        this.limitItem = limitItem;
        BedWarsGame.ItemGenerators.add(this);

        this.armorStandTier = (ArmorStand) this.location.getWorld().spawnEntity(this.location.clone().add(0,0.25+0.25+1,0),EntityType.ARMOR_STAND);
        Utils.removeArmorStandData(this.armorStandTier);
        this.armorStandTier.setCustomName("§eTier §c§l"+this.tier);

        this.armorStandType = (ArmorStand) this.location.getWorld().spawnEntity(this.location.clone().add(0,0.25+1,0),EntityType.ARMOR_STAND);
        Utils.removeArmorStandData(this.armorStandType);
        this.armorStandType.setCustomName(this.name);

        this.armorStandTime = (ArmorStand) this.location.getWorld().spawnEntity(this.location.clone().add(0,1,0),EntityType.ARMOR_STAND);
        Utils.removeArmorStandData(this.armorStandTime);
        this.armorStandTime.setCustomName("§cEn attente du lancement de la partie...");
        this.armorStandTime.setHelmet(new ItemStack(this.displayItem));

        BedWarsGame.protectZone(this.location,2);

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
                this.nextItem = this.speed+0.999;
                this.spawnItem();
            }
        }
    }

    public void startSpawn(){
        ItemGenerator itemGenerator = this;
        this.bukkitTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                itemGenerator.removeTimeNextItem(1);
                itemGenerator.updateArmorStands();
            }
        },0,20);
    }

    private void spawnItem() {
        //int i = 0;
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
        super.addSpawnedItem(1);
        Item item = this.location.getWorld().dropItem(this.location,this.itemStack);
        item.setVelocity(new Vector(0,0,0));
        item.teleport(this.location);
        BedWarsGame.itemSpawned.put(item.getUniqueId(),this);

    }


    public void updateArmorStands() {
        ArmorStand armorStand = this.armorStandTime;
        armorStand.setCustomName("§eProchain item dans §c"+Utils.twoCaractere(((int)this.nextItem))+" sec§e.");
        //armorStand.setHeadPose(armorStand.getHeadPose().add(0,0.5,0));
    }

    public void delete() {
        this.bukkitTask.cancel();
    }


}
