package fr.red_spash.bedwars.Models;

import com.avaje.ebeaninternal.server.transaction.BulkEventListenerMap;
import fr.red_spash.bedwars.Shop.Applications.ItemShop;
import fr.red_spash.bedwars.Shop.Applications.UpgradableItem;
import fr.red_spash.bedwars.Shop.Shop;
import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class PlayerData {
    private UUID uuid;
    private Base base;
    private UpgradableItem pickaxe = null;
    private UpgradableItem axe = null;
    private boolean asShear = false;
    private ItemShop armor = null;
    private Integer kill = 0;
    private Integer finalKill = 0;
    private boolean isDead = false;

    public PlayerData(UUID uuid, Base base){
        this.uuid = uuid;
        this.base = base;
    }

    public String getNameWithColor(){
        Player p = Bukkit.getPlayer(this.uuid);
        if(this.base != null){
            return Utils.getChatColorOf(this.base.getColor())+p.getName();
        }
        return p.getName();
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public void addKill() {
        this.kill = kill +1;
    }

    public void addFinalKill() {
        this.finalKill = finalKill+1;
    }

    public void setSheart(Boolean as){
        this.asShear = as;
    }

    public void setArmor(ItemShop itemShop){
        this.armor = itemShop;
    }

    public void setAxe(UpgradableItem axe) {
        this.axe = axe;
    }

    public void setPickaxe(UpgradableItem pickaxe) {
        this.pickaxe = pickaxe;
    }

    public Base getBase() {
        return this.base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public void setDefaultInventory(){
       this.resetInventory();


        this.updateArmor();
    }

    public void resetInventory() {
        Player p = Bukkit.getPlayer(this.uuid);
        PlayerInventory inv = p.getInventory();

        inv.clear();
        inv.addItem(Utils.makeUnbreakable(new ItemStack(Material.WOOD_SWORD)));
        inv.addItem(Utils.makeUnbreakable(new ItemStack(Material.FIREBALL,64)));

        if(this.pickaxe != null){
            p.getInventory().addItem(this.pickaxe.givableItemStack());
        }
        if(this.axe != null){
            p.getInventory().addItem(this.axe.givableItemStack());
        }
        if(asShear){
            p.getInventory().addItem(Utils.makeUnbreakable(new ItemStack(Material.SHEARS)));
        }
    }

    public void updateArmor(){
        Player p = Bukkit.getPlayer(this.uuid);
        EntityEquipment equipment = p.getEquipment();
        DyeColor dyeColor = this.base.getColor();
        equipment.setHelmet(Utils.getArmorColor(Material.LEATHER_HELMET,dyeColor));
        equipment.setChestplate(Utils.getArmorColor(Material.LEATHER_CHESTPLATE,dyeColor));

        if(this.armor != null){
            String armor_name = this.armor.getName();
            if(armor_name.toLowerCase().contains("diamant")){
                equipment.setLeggings(Utils.getArmorColor(Material.DIAMOND_LEGGINGS,dyeColor));
                equipment.setBoots(Utils.getArmorColor(Material.DIAMOND_BOOTS,dyeColor));
            } else if (armor_name.toLowerCase().contains(" fer")) {
                equipment.setLeggings(Utils.getArmorColor(Material.IRON_LEGGINGS,dyeColor));
                equipment.setBoots(Utils.getArmorColor(Material.IRON_BOOTS,dyeColor));
            }else if(armor_name.toLowerCase().contains("maille")){
                equipment.setLeggings(Utils.getArmorColor(Material.CHAINMAIL_LEGGINGS,dyeColor));
                equipment.setBoots(Utils.getArmorColor(Material.CHAINMAIL_BOOTS,dyeColor));
            }
        }else{
            equipment.setLeggings(Utils.getArmorColor(Material.LEATHER_LEGGINGS,dyeColor));
            equipment.setBoots(Utils.getArmorColor(Material.LEATHER_BOOTS,dyeColor));
        }
    }

    public void downdradeTools(){
        if(this.pickaxe != null){
            if(this.pickaxe.getDownGrade() != null){
                this.pickaxe = this.pickaxe.getDownGrade();
                Shop.DEFAULT_PICKAXE.addRemoveOneLevel(this.uuid);
            }
        }
        if(this.axe != null){
            if(this.axe.getDownGrade() != null){
                this.axe = this.axe.getDownGrade();
                Shop.DEFAULT_AXE.addRemoveOneLevel(this.uuid);
            }

        }
    }

}
