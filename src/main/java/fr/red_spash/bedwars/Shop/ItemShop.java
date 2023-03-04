package fr.red_spash.bedwars.Shop;

import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemShop {
    private ItemStack itemStack;
    private String name;
    private String desription;
    private Prix prix;


    public ItemShop(ItemStack itemStack,String name, String desription, Prix prix){
        this.itemStack = itemStack;
        this.name = name;
        this.desription = desription;
        this.prix = prix;
    }


    public String getDesription() {
        return desription;
    }

    public String getName() {
        return name;
    }

    public Prix getPrix() {
        return prix;
    }

    public ItemStack getItemStack() {
        ItemStack itemStack = this.itemStack.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§a"+this.name);
        itemMeta.setLore(new ArrayList<>(Arrays.asList("§f"+this.desription,"§f","§7Prix: §6"+this.prix.getAmount()+" "+ Utils.upperCaseFirst(this.prix.getItemTypeNeed().toString().toLowerCase().replace("_"," ")))));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
