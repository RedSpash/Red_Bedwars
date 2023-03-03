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
import org.bukkit.util.Vector;

public class ItemGenerator {
    private int tier;
    private boolean isForge;
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

    public ItemGenerator( ItemStack itemStack, Material displayItem, Location location, Double speed, Boolean isForge, int limitItem, String name){
        this.displayItem = displayItem;
        this.isForge = isForge;
        this.name = name;
        this.itemStack = itemStack;
        this.speed = speed;
        this.nextItem = speed;
        this.location = location;
        this.tier = 1;
        this.limitItem = limitItem;
        BedWarsGame.ItemGenerators.add(this);

        if(!this.isForge){
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
        }
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
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                //itemGenerator.updateArmorStands();
                itemGenerator.removeTimeNextItem(1);
            }
        },0,20);
    }

    private void spawnItem() {
        int i = 0;
        for(Entity entity : this.location.getWorld().getNearbyEntities(this.location,1.5,1,1.5)){
            if(entity instanceof Item){
                Item item = (Item) entity;
                i = i + item.getItemStack().getAmount();
                if(i >= this.limitItem){
                    return;
                }
            }
        }
        if(this.isForge){
            Location loca = this.location.clone().add((float) Utils.random_number(-100,100)/100,0,(float) Utils.random_number(-100,100)/100);
            Item item = loca.getWorld().dropItem(loca,this.itemStack);
            item.setVelocity(new Vector(0,0,0));
            item.teleport(loca);
            Main.cooldownItemStackable.put(item,System.currentTimeMillis()+1000*4);
        }else{
            Item item = this.location.getWorld().dropItem(this.location,this.itemStack);
            item.setVelocity(new Vector(0,0,0));
            item.teleport(this.location);
        }

    }


    public void updateArmorStands() {
        if(!this.isForge && BedWarsGame.gameStat == GameState.Started){
            this.armorStandTime.setCustomName("§eProchain item dans §c"+Utils.twoCaractere(((int)this.nextItem))+" sec§e.");
            this.armorStandTime.setHeadPose(this.armorStandTime.getHeadPose().add(0,0.05,0));
        }
    }
}
