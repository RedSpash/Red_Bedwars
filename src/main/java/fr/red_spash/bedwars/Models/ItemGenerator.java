package fr.red_spash.bedwars.Models;

import fr.red_spash.bedwars.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitTask;

public class ItemGenerator {
    private ItemStack itemStack;
    private Double speed;
    private Location location;
    private ArmorStand armorStandTime;
    private ArmorStand armorStandType;
    private ArmorStand armorStandTier;
    private double nextItem;

    public ItemGenerator(ItemStack itemStack, Location location, Double speed){
        this.itemStack = itemStack;
        this.speed = speed;
        this.nextItem = speed;
        this.location = location;
        Main.ItemGenerators.add(this);

        this.armorStandTime = (ArmorStand) this.location.getWorld().spawnEntity(this.location,EntityType.ARMOR_STAND);
        this.armorStandTime.setSmall(true);
        this.armorStandTime.setVisible(false);
        this.armorStandTime.setCustomNameVisible(true);
        this.armorStandTime.setCustomName("§c§l....");
    }

    public void destroy(){

    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public void removeTimeNextItem(double time){
        this.nextItem = this.nextItem - time;
        if(this.nextItem <= 0){
            this.nextItem = this.speed;
            this.spawnItem();
        }
    }

    private void spawnItem() {
        this.location.getWorld().dropItemNaturally(this.location,this.itemStack);
    }


    public void updateArmorStands() {
        this.armorStandTime.setCustomName("§fProchain "+Main.upperCaseFirst(itemStack.getType().toString().toLowerCase())+" dans "+((int)this.nextItem)+" secondes.");
    }
}
